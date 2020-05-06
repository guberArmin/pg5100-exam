package no.kristiania.exam.tsdes.backend.services;

import no.kristiania.exam.tsdes.backend.TestApplication;
import no.kristiania.exam.tsdes.backend.entities.Item;
import no.kristiania.exam.tsdes.backend.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserServiceTest extends ServiceTestBase {
    @Autowired
    private UserService userService;


    @Test
    public void testCreateAndGetUser() {
        String userName = "foo";
        String name = "foo_name";
        String lastName = "foo_last_name";
        String email = "foo@email.com";
        String password = "123";
        String role = "user";

        boolean isCreated = userService.createUser(userName, name, lastName, password, email, role);
        assertTrue(isCreated);

        User user = userService.findUserByUserName(userName);
        assertNotNull(user);
        assertEquals(userName, user.getUsername());
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testGetUserWithOwnedItems() {
        String userName = "foo";
        String name = "foo_name";
        String lastName = "foo_last_name";
        String email = "foo@email.com";
        String password = "123";
        String role = "user";

        userService.createUser(userName, name, lastName, password, email, role);

        User user = userService.findUserByUserNameWithCollections(userName);
        assertEquals(0, user.getOwnedItems().size());

        Item firstItem = getRandomItem();
        Item secondItem = getRandomItem();
        userService.addItemToUser(user, firstItem);
        userService.addItemToUser(user, secondItem);


        assertEquals(2, user.getOwnedItems().size());
    }


    @Test
    public void testAddItemToUser() {
        User user = getRandomUser();
        Item firstItem = getRandomItem();
        Item secondItem = getRandomItem();
        assertEquals(0, user.getOwnedItems().size());
        userService.addItemToUser(user, firstItem);
        userService.addItemToUser(user, secondItem);
        assertEquals(2, user.getOwnedItems().size());
    }

    @Test
    public void testBuyLootBox() {
        //Since every new user gets 400 ducats we know we can buy loot boxes
        User user = getRandomUser();
        assertEquals(0,  user.getNumberOfLootBoxes(), DELTA);
        assertEquals(400,  user.getBalance(), DELTA);
        userService.buyLootBox(user.getUsername());
        //Lets get latest user data
        user = userService.findUserByUserNameWithCollections(user.getUsername());
        assertEquals(1,  user.getNumberOfLootBoxes(),DELTA);
        assertEquals(300,  user.getBalance(), DELTA);
        //Lets spend all of our ducats and see what happens when we try to buy
        userService.buyLootBox(user.getUsername());//200 ducats after
        userService.buyLootBox(user.getUsername());//100 ducats after
        userService.buyLootBox(user.getUsername());//0 ducats after
        //We have 0 ducats expect exception
        User finalUser = user;
        assertThrows(IllegalStateException.class, () -> {
            userService.buyLootBox(finalUser.getUsername());
        });
    }


    @Test
    public void testRedeemLootBox() {
        User user = getRandomUser();
        assertEquals(0,user.getOwnedItems().size());
        userService.buyLootBox(user.getUsername());//200 ducats after
        //Lets simulate one loot box with 3 items
        List<Item> lootBox = new ArrayList<>();
        lootBox.add(getRandomItem());
        lootBox.add(getRandomItem());
        lootBox.add(getRandomItem());
        userService.redeemLootBox(user.getUsername(),lootBox);
        //Lets get latest user data
        int numberOfNew = (int) lootBox.stream().distinct().count();
        user = userService.findUserByUserNameWithCollections(user.getUsername());
        assertEquals(numberOfNew,user.getOwnedItems().size());

    }
    @Test
    public void testRedeemDuplicates() {
        User user = getRandomUser();
        assertEquals(0,user.getOwnedItems().size());
        userService.buyLootBox(user.getUsername());//200 ducats after
        //Lets simulate one loot box with 3 items
        List<Item> lootBox = new ArrayList<>();
        Item item = getRandomItem();
        lootBox.add(item);
        lootBox.add(item);
        lootBox.add(item);
        userService.redeemLootBox(user.getUsername(),lootBox);
        //Lets get latest user data
        user = userService.findUserByUserNameWithCollections(user.getUsername());
        assertEquals(1,user.getOwnedItems().size());
        assertEquals(2,user.getOwnedCopies().get(0).getNumberOfCopies(),DELTA);

    }
}
