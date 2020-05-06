package no.kristiania.exam.tsdes.backend.entities;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
public class CopyId implements Serializable {
    //One user can have many copies of different items
    //But one copy is owned by one user
    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    @NotNull
    private Item item;

    public CopyId() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
