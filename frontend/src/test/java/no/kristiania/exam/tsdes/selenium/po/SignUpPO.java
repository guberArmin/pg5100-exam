package no.kristiania.exam.tsdes.selenium.po;

import no.kristiania.exam.tsdes.selenium.PageObject;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class is adaptation of:
 * https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/intro/exercise-solutions/quiz-game/part-11/frontend/src/test/java/org/tsdes/intro/exercises/quizgame/selenium/po/SignUpPO.java
 */
public class SignUpPO extends LayoutPO  {
    public SignUpPO(WebDriver driver, String host, int port) {
        super(driver, host, port);
    }

    public SignUpPO(PageObject other) {
        super(other);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Sign Up");
    }

    public IndexPO signUp(String userID, String password, String name, String lastName, String email){
        setText("username", userID);
        setText("password", password);
        setText("nameInput", name);
        setText("lastNameInput", lastName);
        setText("email", email);
        clickAndWait("signUpBtn");

        IndexPO indexPO = new IndexPO(this);
        assertTrue(getDriver().getTitle().contains("Home page"));

        return indexPO;
    }
}
