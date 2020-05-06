package no.kristiania.exam.tsdes.backend.services;

import no.kristiania.exam.tsdes.backend.entities.Item;
import no.kristiania.exam.tsdes.backend.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.function.Supplier;

//This class is adaptation of:
//https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/intro/exercise-solutions/quiz-game/part-11/backend/src/main/java/org/tsdes/intro/exercises/quizgame/backend/service/DefaultDataInitializerService.java
@Service
public class DefaultDataInitializerService {

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;


    @PostConstruct
    public void init() {
        //Create some users with same user name and name
        String firstUserName = "kitkat";
        String secondUserName = "lollipop";
        String thirdUserName = "oreo";

        attempt(() ->
                userService.createUser(
                        firstUserName,
                        firstUserName,
                        firstUserName + "_last_name",
                        "123123",
                        firstUserName + "@email.com",
                        "user")
        );
        attempt(() ->
                userService.createUser(
                        secondUserName,
                        secondUserName,
                        secondUserName + "_last_name",
                        "123123",
                        secondUserName + "@email.com",
                        "user")
        );
        attempt(() ->
                userService.createUser(
                        thirdUserName,
                        thirdUserName,
                        thirdUserName + "_last_name",
                        "123123",
                        thirdUserName + "@email.com",
                        "user")
        );

        //Now lets create some hearthstone cards to collect

        Long shadowHoof = attempt(() ->
                itemService.createItem(
                        "Shadowhoof Slayer",
                        "Was a humble Elven Archer before Sargeras got to him.",
                        2,
                        1,
                        "standard",
                        false)
        );
        Long berserker = attempt(() ->
                itemService.createItem(
                        "Aberrant Berserker",
                        "I berserk, therefore I am.",
                        3,
                        5,
                        "common",
                        true)
        );
        Long bowman = attempt(() ->
                itemService.createItem(
                        "Abominable Bowman",
                        "Frosty the Bowman... was an angry hateful soul... with a fresh hewn bow and a missing nose...",
                        6,
                        7,
                        "epic",
                        false)
        );
        Long abomination = attempt(() ->
                itemService.createItem(
                        "Abomination",
                        "Abominations enjoy Fresh Meat and long walks on the beach.",
                        4,
                        4,
                        "common",
                        false)
        );
        Long sergent = attempt(() ->
                itemService.createItem(
                        "Abusive Sergeant",
                        "ADD ME TO YOUR DECK, MAGGOT!",
                        1,
                        1,
                        "common",
                        true)
        );
        Long enforcet = attempt(() ->
                itemService.createItem(
                        "Abyssal Enforcer",
                        "The Kabal print this on every package of illicit " +
                                "Mana Crystals: WARNING - DO NOT PUT WITHIN REACH OF ABYSSALS." +
                                " THIS IS NOT APPROVED FOR USE BY FLAMING DEMONS OF ANY KIND.",
                        6,
                        6,
                        "common",
                        false)
        );
        Long summoner = attempt(() ->
                itemService.createItem(
                        "Abyssal Summoner ",
                        "But gnomes have tiny hands...",
                        2,
                        2,
                        "rare",
                        false)
        );
        Long ventra = attempt(() ->
                itemService.createItem(
                        "Acherus Veteran",
                        "This is my Runeblade. There are many like it but this one is mine.",
                        2,
                        1,
                        "common",
                        true)
        );
        Long acidmaw = attempt(() ->
                itemService.createItem(
                        "Acidmaw",
                        "With the help of his trusty sidekick Dreadscale, the giant jormungar Acidmaw is ready to face any knight!\n",
                        4,
                        2,
                        "legendary",
                        true)
        );
        Long acolyte = attempt(() ->
                itemService.createItem(
                        "Acolyte of Agony",
                        "It takes many years of practiced study in order to fully master agony.",
                        3,
                        3,
                        "epic",
                        false)
        );
        Long reaver = attempt(() ->
                itemService.createItem(
                        "Aeon Reaver",
                        "Ripping centuries asunder helps him manage his busy schedule.",
                        4,
                        4,
                        "rare",
                        false)
        );
        Long grizzly = attempt(() ->
                itemService.createItem(
                        "Addled Grizzly",
                        "Druids who spend too long in bear form are more susceptible to the whispers of the Old Gods. " +
                                "Right now they are whispering the lyrics to La Bamba",
                        2,
                        2,
                        "rare",
                        false)
        );
        Long archmage = attempt(() ->
                itemService.createItem(
                        "Archmage",
                        "You earn the title of Archmage when you can destroy anyone who calls you on it.",
                        4,
                        7,
                        "standard",
                        false)
        );
        Long hound = attempt(() ->
                itemService.createItem(
                        "Core Hound",
                        "You don’t tame a Core Hound. You just train it to eat someone else before it eats you.",
                        9,
                        5,
                        "standard",
                        false)
        );
        Long mage = attempt(() ->
                itemService.createItem(
                        "Dalaran Mage",
                        "You don't see a lot of Dalaran warriors.",
                        1,
                        4,
                        "standard",
                        true)
        );

        //Lets add some cards and duplicates to users
        //First user
        userService.addItemToUser(firstUserName,mage);
        userService.addItemToUser(firstUserName,mage);
        userService.addItemToUser(firstUserName,mage);

        userService.addItemToUser(firstUserName,grizzly);
        userService.addItemToUser(firstUserName,grizzly);

        userService.addItemToUser(firstUserName,acidmaw);
        userService.addItemToUser(firstUserName,acidmaw);

        userService.addItemToUser(firstUserName,berserker);
        userService.addItemToUser(firstUserName,berserker);

        userService.addItemToUser(firstUserName, enforcet);
        userService.addItemToUser(firstUserName, summoner);
        userService.addItemToUser(firstUserName, ventra);

        //Second user
        userService.addItemToUser(secondUserName,shadowHoof);
        userService.addItemToUser(secondUserName,shadowHoof);

        userService.addItemToUser(secondUserName, abomination);
        userService.addItemToUser(secondUserName, acolyte);

        userService.addItemToUser(secondUserName, bowman);
        userService.addItemToUser(secondUserName, bowman);

        userService.addItemToUser(secondUserName, reaver);
        userService.addItemToUser(secondUserName, archmage);
        userService.addItemToUser(secondUserName, hound);


        //Third user
        userService.addItemToUser(thirdUserName, sergent);
        userService.addItemToUser(thirdUserName, sergent);
        userService.addItemToUser(thirdUserName, sergent);

        userService.addItemToUser(thirdUserName, enforcet);
        userService.addItemToUser(thirdUserName, summoner);

        userService.addItemToUser(thirdUserName, ventra);
        userService.addItemToUser(thirdUserName, ventra);
        userService.addItemToUser(thirdUserName, ventra);

        userService.addItemToUser(thirdUserName, reaver);

        userService.addItemToUser(thirdUserName, archmage);
        userService.addItemToUser(thirdUserName, archmage);

        userService.addItemToUser(thirdUserName, hound);
        userService.addItemToUser(thirdUserName, hound);
        userService.addItemToUser(thirdUserName, hound);
        userService.addItemToUser(thirdUserName, hound);
        userService.addItemToUser(thirdUserName, hound);
    }

    private <T> T attempt(Supplier<T> lambda) {
        try {
            return lambda.get();
        } catch (Exception e) {
            return null;
        }
    }

}
