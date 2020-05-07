package no.kristiania.exam.tsdes.selenium.po;

import no.kristiania.exam.tsdes.selenium.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IndexPO extends LayoutPO {
    public IndexPO(WebDriver driver, String host, int port) {
        super(driver, host, port);
    }

    public IndexPO(PageObject other) {
        super(other);
    }

    public void toStartingPage(){
        getDriver().get(host + ":" + port);
    }


    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Home page");
    }

    public int getNumberOfCardsDisplayed(){
        return getDriver().findElements(By.className("cardContainer")).size();
    }

    public CollectionPO toCollection(){
        //If not logged in
        if(!isLoggedIn())
            return null;
        clickAndWait("myCollectionBtn");
        CollectionPO collectionPO = new CollectionPO(this);
        assertTrue(collectionPO.isOnPage());
        return collectionPO;
    }

    public LoginPO toLogin(){
       clickAndWait("linkToLoginId");
        LoginPO loginPO = new LoginPO(this);
        assertTrue(loginPO.isOnPage());
        return loginPO;
    }

    public StorePO toStore(){
        //If not logged in
        if(!isLoggedIn())
            return null;
        clickAndWait("storeBtn");
        StorePO storePO = new StorePO(this);
        assertTrue(storePO.isOnPage());
        return storePO;

    }

    public UserPO toUserInfo(){
        if(!isLoggedIn())
            return null;
        clickAndWait("userID");
        UserPO userPO = new UserPO(this);
        assertTrue(userPO.isOnPage());
        return userPO;
    }

}
