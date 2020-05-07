package no.kristiania.exam.tsdes.backend.entities;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(name = Item.GET_ALL_ITEMS, query = "SELECT item FROM Item item"),
        @NamedQuery(name = Item.GET_ITEMS_BY_RARITY, query = "SELECT item FROM Item item WHERE item.rarity = :rarity"),
        @NamedQuery(name = Item.COUNT_NUMBER_OF_ITEMS, query = "SELECT COUNT (item) FROM Item item")
})
public class Item {
    public static final String GET_ALL_ITEMS="GET_ALL_ITEMS";
    public static final String GET_ITEMS_BY_RARITY="GET_ITEMS_BY_RARITY";
    public static final String COUNT_NUMBER_OF_ITEMS="COUNT_NUMBER_OF_ITEMS";
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Column(unique = true)//No two cards can have same name, at least not in Hearthstone I am creating simulation of
    @Size(max = 128) //I feel that this is more then enough for title
    private String title;

    @NotBlank
    private String description;

    @NotNull
    @Min(0)
    @Max(50) //Should be more then enough for hearthstone cards
    private Integer attack;

    @NotNull
    @Min(0)
    @Max(50) //Should be more then enough for hearthstone cards
    private Integer defense;

    //Standard, common, rare ...
    @NotNull
    private String rarity;

    //Especially valuable type of cards
    @NotNull
    private Boolean isGolden;

    //One user can have multiple cards
    //One card can be owned by many users
    //Lazy fetch is fine here, no need to set FetchType.EAGER as I do not expect to access this data often
    @ManyToMany(mappedBy = "ownedItems")
    private List<User> ownedBy;

    //This can be null, as there could be that no user owns copy of this card, yet
    @OneToMany(mappedBy = "copyId.item", cascade = CascadeType.ALL)
    private List<Copy> copies;

    public Item() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getDefense() {
        return defense;
    }

    public void setDefense(Integer defense) {
        this.defense = defense;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public Boolean getGolden() {
        return isGolden;
    }

    public void setGolden(Boolean golden) {
        isGolden = golden;
    }

    public List<User> getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(List<User> ownedBy) {
        this.ownedBy = ownedBy;
    }

    public List<Copy> getCopies() {
        return copies;
    }

    public void setCopies(List<Copy> copies) {
        this.copies = copies;
    }

}
