package no.kristiania.exam.tsdes.backend.services;

import no.kristiania.exam.tsdes.backend.entities.Item;
import no.kristiania.exam.tsdes.backend.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

/**
 * This class is adaptation of:
 * https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/intro/exercise-solutions/quiz-game/part-11/backend/src/test/java/org/tsdes/intro/exercises/quizgame/backend/service/ServiceTestBase.java
 */
public class ServiceTestBase {

    //Enough with one counter for user and items, only point with it is uniqueness
    int counter = 0;

    //Needed to avoid ambiguity e.g. when comparing two Long objects, as they can be casted to primitive long or seen as object
    protected final static double DELTA =0.0001;


    @Autowired
    private ResetService resetService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected ItemService itemService;

    @BeforeEach
    public void cleanDatabase() {
        resetService.resetDatabase();
    }

    protected User getRandomUser() {
        String userId = "foo_" + counter++;
        userService.createUser(userId,
                userId + "_name",
                userId + "_lastName",
                "123456",
                userId + "@email.com",
                "user"
        );
        return userService.findUserByUserNameWithCollections(userId);
    }

    protected Item getRandomItem() {
        String title = "title_" + ++counter;
        Long itemId = itemService.createItem(title, title + "_description", getRandomStat(), getRandomStat(), getRandomRarity(), isGolden());
        return itemService.getItem(itemId,false);
    }

    //Get random attack/defense
    private int getRandomStat() {
        return new Random().nextInt(50);
    }

    private String getRandomRarity() {
        String[] rarity = {
                "standard",
                "common",
                "rare",
                "epic",
                "legendary"
        };
        return rarity[new Random().nextInt(rarity.length)];
    }

    private boolean isGolden() {
        return (new Random().nextInt(10) % 2) == 0;
    }

}
