package no.kristiania.exam.tsdes.backend.entities;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

/**
 * This class is inspired by: https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/intro/exercise-solutions/quiz-game/part-11/backend/src/main/java/org/tsdes/intro/exercises/quizgame/backend/entity/User.java
 */

@Entity
@Table(name = "USERS")
public class User {
    @Id
    @NotBlank
    private String username;

    //Allow registration without name, as many websites allow,
    @Size(max = 128)
    private String name;

    //Allow registration without information about last name, as many websites allow,
    @Size(max = 128)
    private String lastName;

    @NotBlank
    private String hashedPassword;

    @NotNull
    private Boolean enabled;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotNull
    @Column(columnDefinition = "REAL FLOAT")
    private Double balance;

    //At start we own no items
    @ManyToMany
    private List<Item> ownedItems;

    //It can happen that we have no copies
    @OneToMany(mappedBy = "copyId.user",cascade = CascadeType.ALL)
    private List<Copy> ownedCopies;

    private Long numberOfLootBoxes;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;

    public User() {
    }

    //Could have used setter but felt easier and cleaner doing it this way
    public void reduceBalance(int amount){
        this.balance -= amount;
    }

    public void increaseBalance(int amount){
        this.balance += amount;
    }

    public void reduceNumberOfLootBoxes(int amount){
        this.numberOfLootBoxes -= amount;
    }

    public void increaseNumberOfLootBoxes(int amount){
        this.numberOfLootBoxes += amount;
    }

    public Long getNumberOfLootBoxes() {
        return numberOfLootBoxes;
    }

    public void setNumberOfLootBoxes(Long numberOfLootBoxes) {
        this.numberOfLootBoxes = numberOfLootBoxes;
    }

    public List<Copy> getOwnedCopies() {
        return ownedCopies;
    }

    public void setOwnedCopies(List<Copy> ownedCopies) {
        this.ownedCopies = ownedCopies;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public List<Item> getOwnedItems() {
        return ownedItems;
    }

    public void setOwnedItems(List<Item> ownedItems) {
        this.ownedItems = ownedItems;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userID) {
        this.username = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
