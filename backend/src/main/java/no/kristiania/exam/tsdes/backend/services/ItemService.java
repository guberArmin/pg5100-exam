package no.kristiania.exam.tsdes.backend.services;

import no.kristiania.exam.tsdes.backend.entities.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class ItemService {

    @Autowired
    private EntityManager entityManager;

    public Long createItem(
            String title,
            String description,
            Integer attack,
            Integer defense,
            String rarity,
            Boolean isGolden
    ) {
        Item item = new Item();
        item.setTitle(title);
        item.setDescription(description);
        item.setAttack(attack);
        item.setDefense(defense);
        item.setRarity(rarity);
        item.setGolden(isGolden);

        entityManager.persist(item);

        return item.getId();
    }

    /**
     * Get item from database
     *
     * @param id          Item id in database
     * @param withOwnedBy to fetch or not fetch all users that own this item, since we use lazy fetch
     * @return item if found or throw exception if not
     */
    public Item getItem(Long id, Boolean withOwnedBy) {
        Item item = entityManager.find(Item.class, id);
        if (item == null) {
            throw new IllegalArgumentException("No existing item with id: " + id);
        }

        if (withOwnedBy) {
            item.getOwnedBy().size();
        }

        return item;
    }

    public List<Item> getLimitedNumberOfItems(int numberOfItems,boolean withOwners) {
        //Could check if numberOfItems is negative,
        //but there is no need, as setMaxResults is throwing exception in case it is
        TypedQuery<Item> query = entityManager.createNamedQuery(Item.GET_ALL_ITEMS, Item.class);

        List<Item> items = query.setMaxResults(numberOfItems).getResultList();
        if(withOwners){
            items.forEach(e->e.getOwnedBy().size());
        }
        return items;
    }

    public List<Item> getAllItems(boolean withOwners) {
        TypedQuery<Item> query = entityManager.createNamedQuery(Item.GET_ALL_ITEMS, Item.class);
        List<Item> allItems = query.getResultList();
        if(withOwners){
            allItems.forEach(e -> e.getOwnedBy().size());
        }

        return allItems;
    }


    /**
     * @param numberOfItemsInBox Lets say we want to run special offer where loot box has more that regular number of items
     * @return List of items that represent loot box
     */
    public List<Item> getLootBox(int numberOfItemsInBox) {
        //In case database is empty no point to do any search
        //Same goes if we have no enough items in database
        Long numberOfItemsInDatabase = entityManager.createNamedQuery(Item.COUNT_NUMBER_OF_ITEMS, Long.class).getSingleResult();
        if (numberOfItemsInDatabase == 0)
            return null;
        List<Item> lootBox = new ArrayList<>();
        for (int i = 0; i < numberOfItemsInBox; i++) {
            lootBox.add(getRandomItem());
        }
        return lootBox;
    }

    //For creation of loot boxes we are going to need some random items
    private Item getRandomItem() {

        //This is simple rarity implementation
        //if we get number from [0.0 to 0.5) you get standard card
        //if we get number from [0.5 to 0.7) you get common card
        //If we get number from [0.7 to 0.85) you get rare card
        //If we get number from [0.85 to 0.95) you get epic card
        //If we get number from [0.95 to 1) you get legendary card
        String rarityParameter;
        List<Item> allItemsWithRarity = new ArrayList<>();
        //In case we have no cards of given rarity try again. This could be useful if we plan to add some rarity later
        //but would like to have it from start
        while (allItemsWithRarity.size() == 0) {
            double randomNumber = ThreadLocalRandom.current().nextDouble(0, 1);
            if (randomNumber < 0.5) {
                rarityParameter = "standard";
            } else if (compareDoubles(randomNumber, 0.5, 0.7)) {
                rarityParameter = "common";
            } else if (compareDoubles(randomNumber, 0.7, 0.85)) {
                rarityParameter = "rare";
            } else if (compareDoubles(randomNumber, 0.85, 0.95)) {
                rarityParameter = "epic";
            } else {
                rarityParameter = "legendary";
            }

            TypedQuery<Item> query = entityManager.createNamedQuery(Item.GET_ITEMS_BY_RARITY, Item.class);
            query.setParameter("rarity", rarityParameter);
            allItemsWithRarity = query.getResultList();
        }

        return allItemsWithRarity.get(ThreadLocalRandom.current().nextInt(0, allItemsWithRarity.size()));
    }

    //Direct comparison of two doubles is not good idea
    //We have to take in account that it could be some minor differences that we are not interested in
    private boolean compareDoubles(double number, double lowerBound, double upperBound) {
        //Inspired by https://howtodoinjava.com/java/basics/correctly-compare-float-double/
        //In my case 3 decimal places is just good enough when it comes to accuracy
        final double THRESHOLD = .001;
        if (number - lowerBound > THRESHOLD)
            return upperBound - number > 0;
        else
            return false;
    }


}
