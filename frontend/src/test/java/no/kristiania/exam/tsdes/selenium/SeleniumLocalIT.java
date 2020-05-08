package no.kristiania.exam.tsdes.selenium;

import no.kristiania.exam.tsdes.Application;
import no.kristiania.exam.tsdes.backend.entities.Item;
import no.kristiania.exam.tsdes.backend.entities.User;
import no.kristiania.exam.tsdes.backend.services.ItemService;
import no.kristiania.exam.tsdes.backend.services.UserService;
import no.kristiania.exam.tsdes.selenium.po.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * All tests are ORIGINAL
 * ONLY configuration for tests is copied from
 * : https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/intro/exercise-solutions/quiz-game/part-11/frontend/src/test/java/org/tsdes/intro/exercises/quizgame/selenium/SeleniumTestBase.java
 */

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
public class SeleniumLocalIT {
    private static WebDriver driver;

    private static final Double DELTA = 0.001;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @LocalServerPort
    private int port;

    @BeforeAll
    public static void initClass() {

        driver = SeleniumDriverHandler.getChromeDriver();

        assumeTrue(driver != null, "Cannot find/initialize Chrome driver");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.close();
        }
    }

    protected WebDriver getDriver() {
        return driver;
    }

    protected String getServerHost() {
        return "localhost";
    }

    protected int getServerPort() {
        return port;
    }

    private static final AtomicInteger counter = new AtomicInteger(0);

    private String getUniqueId() {
        return "foo_SeleniumLocalIT_" + counter.getAndIncrement();
    }


    private IndexPO home;


    private IndexPO createNewUser(String username, String password) {

        home.toStartingPage();

        SignUpPO signUpPO = home.toSignUp();

        //For simplicity just reuse given information to build missing stuff such as email...
        IndexPO indexPO = signUpPO.signUp(username, password, username, username + "last_name", username + "@email.com");
        assertNotNull(indexPO);

        return indexPO;
    }

    @BeforeEach
    public void initTest() {

        /*
            we want to have a new session, otherwise the tests
            will share the same Session beans
         */
        getDriver().manage().deleteAllCookies();

        home = new IndexPO(getDriver(), getServerHost(), getServerPort());

        home.toStartingPage();

        assertTrue(home.isOnPage(), "Failed to start from Home Page");
    }

    @Test
    public void testDisplayHomePage() {
        //My database hold 15 items
        //On home page we should see 10 out of those 15
        assertEquals(10, home.getNumberOfCardsDisplayed());
    }

    @Test
    public void testEmptyCollection() {
        home = createNewUser(getUniqueId(), "123");
        CollectionPO collectionPO = home.toCollection();
        assertEquals(0, collectionPO.getNumberOfItemsInCollection());
        assertEquals(4, collectionPO.getNumberOfLootBoxes());
    }

    @Test
    public void testRedeemLootBox() {
        home = createNewUser(getUniqueId(), "123");
        CollectionPO collectionPO = home.toCollection();
        assertEquals(0, collectionPO.getNumberOfItemsInCollection());
        assertEquals(4, collectionPO.getNumberOfLootBoxes());

        collectionPO.redeemLootBox();
        assertEquals(3, collectionPO.getNumberOfLootBoxes());
        //If we get unlucky we could get duplicates even on first loot box that is why we need to count also duplicates
        //We know one loot box has 3 items
        assertEquals(3, collectionPO.getNumberOfItemsInCollection() + collectionPO.getNumberOfDuplicates());

    }

    @Test
    public void testFailedRedeemLootBox() {
        home = createNewUser(getUniqueId(), "123");
        CollectionPO collectionPO = home.toCollection();
        assertEquals(0, collectionPO.getNumberOfItemsInCollection());
        assertEquals(4, collectionPO.getNumberOfLootBoxes());

        //We have 4 boxes, lets open them all
        collectionPO.redeemLootBox();
        collectionPO.redeemLootBox();
        collectionPO.redeemLootBox();
        collectionPO.redeemLootBox();//Last one opened

        //There should not be any more boxes and we should have gotten 12 cards in total
        assertEquals(0, collectionPO.getNumberOfLootBoxes());
        assertEquals(12, collectionPO.getNumberOfItemsInCollection() + collectionPO.getNumberOfDuplicates());

        //After we have opened last box button for opening of boxes should be removed
        //That is why I expect for selenium to throw exception
        assertThrows(NoSuchElementException.class, () -> collectionPO.redeemLootBox());
    }

    @Test
    public void testMillItem() {
        //We need some cards to play around with
        //So we have to open some boxes
        int numberOfDuplicates = 0;
        boolean wasLogged = false;
        //We also have to have duplicates that is why we are looping to get them if needed
        while (numberOfDuplicates == 0) {
            if (wasLogged)
                home.doLogout();
            home = createNewUser(getUniqueId(), "123");
            numberOfDuplicates = openLootBoxes();
            wasLogged = true;
        }

        StorePO storePO = home.toStore();
        int numberOfDisplayed = storePO.getNumberOfDisplayed();

        //Lets try to sell one random card, I add +1 as index in xpath start from 1 not 0
        int index = new Random().nextInt(numberOfDisplayed) + 1;
        double balance = storePO.getBalance();
        long numberOfCopies = storePO.getNumberOfCopiesForSingleItem(index);

        //Title is unique so we are using it to check if value for card has changed or card has been removed
        String title = storePO.getTitle(index);

        storePO.sellCard(index);

        assertEquals(storePO.getBalance(), balance + 25);
        //If there is only one copy card with same title should not be found
        if (numberOfCopies == 1) {
            //At same index we should not have card with same title as it should have been removed,
            // but just if there are more then one duplicate we own
            assertFalse(storePO.isThereTitle(title));
            assertEquals(storePO.getNumberOfDisplayed(), numberOfDisplayed - 1);
        } else {
            //If we had more then one copy we expect that one should be found
            assertEquals(storePO.getNumberOfCopiesForSingleItem(index), numberOfCopies - 1, DELTA);
        }

    }

    @Test
    public void testBuyLootBox() {
        home = createNewUser(getUniqueId(), "123");
        StorePO storePO = home.toStore();
        int originalNumberOfBoxes = storePO.getNumberOfLootBoxes();
        double originalBalance = storePO.getBalance();
        storePO.buyLootBox();

        assertEquals(originalNumberOfBoxes + 1, storePO.getNumberOfLootBoxes());
        assertEquals(originalBalance - 100, storePO.getBalance());
    }


    @Test
    public void testUserPageInfo() {
        String username = getUniqueId();
        home = createNewUser(username, "345");
        UserPO userPO = home.toUserInfo();

        assertEquals(userPO.getDisplayedUsername(), username);
        assertEquals(userPO.getNumberOfCopies(), 0);
    }



    @Test
    void testUsersMissingCards() {
        home = createNewUser(getUniqueId(), "3232");
        //New user should be missing all cards, in my cas 15
        UserPO userPO = home.toUserInfo();
        assertEquals(15, userPO.getNumberOfMissingDisplayed());
    }

    private int openLootBoxes() {
        CollectionPO collectionPO = home.toCollection();
        assertEquals(0, collectionPO.getNumberOfItemsInCollection());
        assertEquals(4, collectionPO.getNumberOfLootBoxes());

        //We have 4 boxes, lets open them all
        collectionPO.redeemLootBox();
        collectionPO.redeemLootBox();
        collectionPO.redeemLootBox();
        collectionPO.redeemLootBox();//Last one opened
        return collectionPO.getNumberOfDuplicates();
    }


}
