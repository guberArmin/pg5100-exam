package no.kristiania.exam.tsdes.selenium.po;

import no.kristiania.exam.tsdes.selenium.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CollectionPO extends LayoutPO {
    public CollectionPO(WebDriver driver, String host, int port) {
        super(driver, host, port);
    }

    public CollectionPO(PageObject other) {
        super(other);
    }

    public int getNumberOfItemsInCollection(){
        return getDriver().findElements(By.xpath("//*[@id=\"myCollectionDiv\"]/div")).size();
    }
    public int getNumberOfDuplicates(){
        List<WebElement> numberOfCopiesForEach = getDriver().findElements(By.xpath("//*[@id=\"myDuplicatesDiv\"]//p"));
        return numberOfCopiesForEach.stream().mapToInt(e-> Integer.parseInt(e.getAttribute("innerHTML"))).sum();
    }

    public int getNumberOfLootBoxes(){
        return Integer.parseInt(getText("numberOfBoxesParagraph"));
    }

    public void redeemLootBox(){
        clickAndWait("redeemLootBoxBtn");
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("My collection");
    }
}
