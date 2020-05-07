package no.kristiania.exam.tsdes.backend.services;

import no.kristiania.exam.tsdes.backend.TestApplication;
import no.kristiania.exam.tsdes.backend.entities.Copy;
import no.kristiania.exam.tsdes.backend.entities.Item;
import no.kristiania.exam.tsdes.backend.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CopyServiceTest extends ServiceTestBase {

    @Autowired
    private CopyService copyService;

    @Test
    public void testSellCopy() {
        User user = getRandomUser();
        assertEquals(0, user.getOwnedItems().size());
        userService.buyLootBox(user.getUsername());//200 ducats after
        //Lets simulate one loot box with 3 items
        List<Item> lootBox = new ArrayList<>();
        Item item = getRandomItem();
        lootBox.add(item);
        lootBox.add(item);
        lootBox.add(item);
        userService.redeemLootBox(user.getUsername(), lootBox);
        copyService.getAllCopies();
        user = userService.findUserByUserNameWithCollections(user.getUsername());

        //Lets get latest user data
        user = userService.findUserByUserNameWithCollections(user.getUsername());
        Double originalBalance = user.getBalance();
        assertEquals(1, user.getOwnedItems().size());
        assertEquals(2, user.getOwnedCopies().get(0).getNumberOfCopies(), DELTA);

        List<Copy> listOfCopies = copyService.getAllCopies();
        assertEquals(1, listOfCopies.size(), DELTA);

        //Lets get latest user data
        copyService.sellOneCopyOfItem(user.getUsername(),item.getId()) ;
        user = userService.findUserByUserNameWithCollections(user.getUsername());
        ;
        assertEquals(originalBalance + 25, user.getBalance(), DELTA);


    }

    @Test
    public void testWithoutAnyCopies() {
        User user = getRandomUser();
        Item item = getRandomItem();
        assertThrows(IllegalStateException.class, () -> copyService.sellOneCopyOfItem(user.getUsername(), item.getId()));
    }
}
