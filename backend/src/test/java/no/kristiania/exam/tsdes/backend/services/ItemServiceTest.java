package no.kristiania.exam.tsdes.backend.services;

import no.kristiania.exam.tsdes.backend.TestApplication;
import no.kristiania.exam.tsdes.backend.entities.Item;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemServiceTest extends ServiceTestBase {

    @Autowired
    private ItemService itemService;

    //Get 5 random items, it felt like good enough number, but can be easily adjusted in case it is not enough
    private void getFiveItems() {
        for (int i = 0; i < 5; i++) {
            getRandomItem();
        }
    }

    @Test
    public void testCreateAndRetrieveItem() {
        String title = "title";
        String description = "description";
        Integer attack = 30;
        Integer defense = 20;
        String rarity = "common";
        boolean isGolden = false;
        Long itemId = itemService.createItem(title, description, attack, defense, rarity, isGolden);
        assertNotNull(itemId);
        //Lets now try to fetch it back and compare all fields
        Item item = itemService.getItem(itemId,false);
        assertEquals(item.getTitle(),title);
        assertEquals(item.getAttack(),attack);
        assertEquals(item.getDefense(),defense);
    }

    @Test
    public void testRetrieveItemWithOwnedBy(){
        //Lets add one random item to database using getRandomItem from ServiceTestBase
        Item item = getRandomItem();
        //Lets get it back with owned by
        Item itemWithOwnedBy = itemService.getItem(item.getId(),true);
        //We should have empty list as no user owns this new item
        assertNotNull(itemWithOwnedBy.getOwnedBy());
        assertEquals(0,itemWithOwnedBy.getOwnedBy().size());
    }

    @Test
    public void testRetrieveLimitedNumberOfItems(){
        getFiveItems();
        //Lets try to get back two
        List<Item> twoItems= itemService.getLimitedNumberOfItems(2,false);
        assertEquals(2,twoItems.size());
    }
    @Test
    public void testRetrieveAllItems(){
        //Lest add five items to database
        getFiveItems();
        //Lets try to get back two
        List<Item> allItems= itemService.getAllItems(false);
        assertEquals(5,allItems.size());
    }



    @Test
    public void testGetLootBoxWithItems(){
        List<Item> lootBox = itemService.getLootBox(4);
        //We expect null as there is no data in database
        assertNull(lootBox);
        //Now add items and check
        getFiveItems();
        lootBox = itemService.getLootBox(8);
        //Lets try to get more items then there is in database
        //This should be fine, as they are random, and we can have duplicates
        assertEquals(8,lootBox.size());
    }
}
