package no.kristiania.exam.tsdes.frontend.controller;

import no.kristiania.exam.tsdes.backend.entities.Item;
import no.kristiania.exam.tsdes.backend.services.ItemService;
import no.kristiania.exam.tsdes.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@SessionScoped
@Named
public class LootBoxController implements Serializable {
    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    private List<Item> lootBoxContent;

    public String redeemLootBox(String username) {
        this.lootBoxContent = itemService.getLootBox(3);
        userService.redeemLootBox(username, lootBoxContent);
        return "/collection.jsf?isLooted=true&faces-redirect=true";
    }

    public List<Item> getLootBoxContent() {
        return lootBoxContent;
    }

    public void setLootBoxContent(List<Item> lootBoxContent) {
        this.lootBoxContent = lootBoxContent;
    }
}
