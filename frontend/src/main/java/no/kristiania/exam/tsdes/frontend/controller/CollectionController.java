package no.kristiania.exam.tsdes.frontend.controller;

import no.kristiania.exam.tsdes.backend.entities.Item;
import no.kristiania.exam.tsdes.backend.entities.User;
import no.kristiania.exam.tsdes.backend.services.CopyService;
import no.kristiania.exam.tsdes.backend.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@RequestScoped
@Named
public class CollectionController implements Serializable {
    @Autowired
    private ItemService itemService;

    @Autowired
    private CopyService copyService;

    public List<Item> getItems(int numberOfItems) {
        return itemService.getLimitedNumberOfItems(numberOfItems, true);
    }

    public String isGolden(Long itemId) {
        if (itemId == null)
            return "";
        if (itemService.getItem(itemId, false).getGolden())
            return "cardContainer golden";
        else
            return "cardContainer regular";
    }

    public boolean isDisabled(User user) {
        return user.getBalance() < 100;
    }

    public boolean hasLootBoxes(User user) {
        return user.getNumberOfLootBoxes() > 0;
    }


    public String sellCopies(User user, Long itemId, Long numberOfCopies, Boolean sellAll) {
        if (sellAll) {
            for (int i = 0; i < numberOfCopies; i++) {
                copyService.sellOneCopyOfItem(user.getUsername(), itemId);
            }
        } else {
            copyService.sellOneCopyOfItem(user.getUsername(), itemId);
        }
        return "/store.jsf?faces-redirect=true";
    }

    public int getNumberOfCardsAvailable(User user){
        return itemService.getAllItems(false).size();
    }

}
