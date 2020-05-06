package no.kristiania.exam.tsdes.backend.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;


@NamedQueries({
        @NamedQuery(name = Copy.GET_COPY_FOR_ITEM_AND_USER, query = "SELECT c FROM Copy c WHERE c.copyId.user.username = :username AND c.copyId.item.id = :itemId"),
        @NamedQuery(name = Copy.GET_ALL_COPIES, query = "SELECT c FROM Copy c ")
})
@Entity
public class Copy {
    public static final String GET_COPY_FOR_ITEM_AND_USER = "GET_COPY_FOR_ITEM_AND_USER";
    public static final String GET_ALL_COPIES = "GET_ALL_COPIES";

    @EmbeddedId
    private CopyId copyId;

    @NotNull
    private Long numberOfCopies;

    public Copy() {
    }

    public CopyId getCopyId() {
        return copyId;
    }

    public void setCopyId(CopyId copyId) {
        this.copyId = copyId;
    }

    public Long getNumberOfCopies() {
        return numberOfCopies;
    }

    public void setNumberOfCopies(Long numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }
}
