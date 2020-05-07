package no.kristiania.exam.tsdes.selenium.po;

import no.kristiania.exam.tsdes.selenium.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class StorePO extends LayoutPO {
    public StorePO(WebDriver driver, String host, int port) {
        super(driver, host, port);
    }

    public StorePO(PageObject other) {
        super(other);
    }

    public void sellCard(int index) {
        //As ui repeat doesnt allow putting id on button dynamically we can find it by xpath
        String xpath = "((//*[contains(@class, 'cardContainer')])[" + index + "])//*[contains(@id,'sellOneCopyBtn')]";
        String id = getDriver().findElements(By.xpath(xpath)).get(0).getAttribute("id");
        clickAndWait(id);
    }

    public Long getNumberOfCopiesForSingleItem(int index) {
        //As ui repeat doesnt allow putting id on button dynamically we can find it by xpath
        String xpath = "((//*[contains(@class, 'cardContainer')])[" + index + "])//*[contains(@id,'numberOfCopies')]";
        String value = getDriver().findElements(By.xpath(xpath)).get(0).getAttribute("innerHTML");
        String copies = value.substring(value.indexOf(":") + 1).trim();
        return Long.valueOf(copies);
    }

    //Title is unique so if card is removed we should not find one with same title on page
    public String getTitle(int index) {
        //As ui repeat doesnt allow putting id on button dynamically we can find it by xpath
        String xpath = "((//*[contains(@class, 'cardContainer')])[" + index + "])//h3";
        return getDriver().findElements(By.xpath(xpath)).get(0).getAttribute("innerHTML");
    }

    public boolean isThereTitle(String title) {
        List<WebElement> allTitles = getDriver().findElements(By.xpath("//*[contains(@class, 'cardContainer')]//h3"));
        long numberOfOccurrences = allTitles.stream().filter(t -> t.getAttribute("innerHTML").equals(title)).count();
        return numberOfOccurrences != 0;

    }

    public int getNumberOfDisplayed() {
        return getDriver().findElements(By.xpath("//*[contains(@class, 'cardContainer')]")).size();
    }

    public double getBalance() {
        return Double.parseDouble((getText("balanceLabel")));
    }

    public void buyLootBox() {
        clickAndWait("buyLootBoxBtn");
    }

    public int getNumberOfLootBoxes() {
        return Integer.parseInt(getText("numberOfLootBoxLbl"));
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Store");
    }
}
