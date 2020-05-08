package no.kristiania.exam.tsdes.selenium.po;

import no.kristiania.exam.tsdes.selenium.PageObject;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginPO extends LayoutPO {
    public LoginPO(WebDriver driver, String host, int port) {
        super(driver, host, port);
    }

    public LoginPO(PageObject other) {
        super(other);
    }

    public IndexPO doLogin(String username, String password){
        setText("username",username);
        setText("password",password);
        clickAndWait("submit");
        IndexPO indexPO = new IndexPO(this);

        assertTrue(indexPO.isOnPage());
        return indexPO;
    }
    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Log In");
    }
}
