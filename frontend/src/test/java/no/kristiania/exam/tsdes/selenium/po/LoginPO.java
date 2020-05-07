package no.kristiania.exam.tsdes.selenium.po;

import no.kristiania.exam.tsdes.selenium.PageObject;
import org.openqa.selenium.WebDriver;

public class LoginPO extends LayoutPO {
    public LoginPO(WebDriver driver, String host, int port) {
        super(driver, host, port);
    }

    public LoginPO(PageObject other) {
        super(other);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Log in");
    }
}
