package no.kristiania.exam.tsdes.selenium.po;

import no.kristiania.exam.tsdes.selenium.PageObject;
import org.openqa.selenium.WebDriver;

public class UserPO extends LayoutPO {
    public UserPO(WebDriver driver, String host, int port) {
        super(driver, host, port);
    }

    public UserPO(PageObject other) {
        super(other);
    }

    public String getDisplayedUsername(){
        return getText("userNameLabel");
    }

    public long getNumberOfCopies(){
        return Long.parseLong(getText("numberOfCopies"));
    }
    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("User info");
    }
}
