package no.kristiania.exam.tsdes.frontend.controller;

import no.kristiania.exam.tsdes.backend.entities.Item;
import no.kristiania.exam.tsdes.backend.entities.User;
import no.kristiania.exam.tsdes.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.List;

/**
 * This class is adaptation of:
 * https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/intro/exercise-solutions/quiz-game/part-11/frontend/src/main/java/org/tsdes/intro/exercises/quizgame/frontend/controller/UserInfoController.java
 */
@Named
@RequestScoped
public class UserInfoController {


    @Autowired
    private UserService userService;

    public String getUserName() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    public User getUser() {
        return userService.findUserByUserNameWithCollections(getUserName());
    }

    public String buyLootBox(String username) {
        userService.buyLootBox(username);
        return "/store.jsf?faces-redirect=true";
    }

    public boolean hasDuplicates(String username) {
        return userService.findUserByUserName(username).getOwnedCopies().size() > 0;
    }


}
