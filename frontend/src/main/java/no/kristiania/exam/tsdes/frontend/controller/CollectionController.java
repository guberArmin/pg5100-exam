package no.kristiania.exam.tsdes.frontend.controller;

import no.kristiania.exam.tsdes.backend.entities.Item;
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

    public List<Item> getItems(int numberOfItems){
        return itemService.getLimitedNumberOfItems(numberOfItems,true);
    }

    public String isGolden(Long itemId){
        if(itemService.getItem(itemId,false).getGolden())
            return "cardContainer golden";
        else
            return "cardContainer regular";
    }

}
