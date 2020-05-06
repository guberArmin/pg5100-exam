package no.kristiania.exam.tsdes.backend.services;

import no.kristiania.exam.tsdes.backend.entities.Copy;
import no.kristiania.exam.tsdes.backend.entities.CopyId;
import no.kristiania.exam.tsdes.backend.entities.Item;
import no.kristiania.exam.tsdes.backend.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

/**
 * This java class is adaptation
 * https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/intro/exercise-solutions/quiz-game/part-11/backend/src/main/java/org/tsdes/intro/exercises/quizgame/backend/service/UserService.java
 */
@Service
@Transactional
public class UserService {
    @Autowired
    private EntityManager em;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean createUser(String userName, String name, String lastName, String password, String email, String role) {
        String hashedPassword = passwordEncoder.encode(password);

        //If there is user with given username or email in database we can not create it again, return false
        if ((em.find(User.class, userName) != null) || (em.find(User.class, email) != null)) {
            return false;
        }

        User user = new User();
        user.setUsername(userName);
        user.setName(name);
        user.setLastName(lastName);
        user.setHashedPassword(hashedPassword);
        user.setRoles(Collections.singleton(role));
        user.setNumberOfLootBoxes(0L);
        user.setEnabled(true);
        user.setEmail(email);
        user.setBalance(400D);//Lets give all new users some money to spend :)
        em.persist(user);

        return true;
    }

    public User findUserByUserName(String userName) {
        User user = em.find(User.class, userName);
        if (user == null) {
            throw new IllegalStateException("No user with given userName");
        }
        return user;
    }

    //Get owned items and copies back too
    public User findUserByUserNameWithCollections(String userName) {
        User user = em.find(User.class, userName);
        if (user == null) {
            throw new IllegalStateException("No user with given userName");
        }
        user.getOwnedItems().size();
        user.getOwnedCopies().size();
        return user;
    }

    /**
     * Loot box could have been entity, but it feels that it would be bad use of server resources
     * Since all loot boxes are random, we can just create content for one on demand (when user wants to open it)
     * So it is enough to hold data about number of loot boxes user owns
     */
    public boolean buyLootBox(String username) {
        User user = em.find(User.class, username);
        if (user == null) {
            throw new IllegalArgumentException("No existing user: " + username);
        }

        //One loot box costs 100 ducat
        if (user.getBalance() < 100) {
            throw new IllegalStateException("Insufficient funds" + user.getBalance());
        }

        user.reduceBalance(100);
        user.increaseNumberOfLootBoxes(1);

        return true;
    }

    public void redeemLootBox(String username, List<Item> items) {
        User user = em.find(User.class, username);

        if (user == null) {
            throw new IllegalArgumentException("No existing user: " + username);
        }

        if (user.getNumberOfLootBoxes() < 1) {
            throw new IllegalArgumentException("You have no loot boxes to open.");
        }

        user.reduceNumberOfLootBoxes(1);
        items.forEach(item -> {
            //If user owns this item, we have to update number of copies
            addItemToUser(user, item);
        });
    }

    public void addItemToUser(String username, Long itemId) {
        User user = findUserByUserName(username);
        Item item = em.find(Item.class, itemId);
        addItemToUser(user, item);
    }

    public void addItemToUser(User user, Item item) {
        if (user.getOwnedItems().contains(item)) {
            TypedQuery<Copy> query = em.createNamedQuery(Copy.GET_COPY_FOR_ITEM_AND_USER, Copy.class);
            query.setParameter("username", user.getUsername());
            query.setParameter("itemId", item.getId());

            List<Copy> copyList = query.getResultList();
            if (copyList.isEmpty()) {
                Copy copy = new Copy();

                CopyId copyId = new CopyId();
                copyId.setItem(item);
                copyId.setUser(user);

                copy.setNumberOfCopies(1L);
                copy.setCopyId(copyId);
                em.persist(copy);
            } else {
                Copy copy = copyList.get(0);
                long newNumberOfCopies = copy.getNumberOfCopies() + 1;
                copy.setNumberOfCopies(newNumberOfCopies);
            }
        } else {
            List<Item> list = user.getOwnedItems();
            list.add(item);
            user.setOwnedItems(list);
        }
    }

}
