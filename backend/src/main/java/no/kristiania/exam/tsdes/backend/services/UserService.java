package no.kristiania.exam.tsdes.backend.services;

import no.kristiania.exam.tsdes.backend.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;

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

    public boolean createUser(String userName, String name, String lastName, String password, String email, String role){
        String hashedPassword = passwordEncoder.encode(password);

        //If there is user with given username or email in database we can not create it again, return false
        if((em.find(User.class, userName) != null) || (em.find(User.class, email) != null)){
            return false;
        }

        User user = new User();
        user.setUsername(userName);
        user.setName(name);
        user.setLastName(lastName);
        user.setHashedPassword(hashedPassword);
        user.setRoles(Collections.singleton(role));
        user.setEnabled(true);
        user.setEmail(email);
        user.setRanked(new ArrayList<>());
        em.persist(user);

        return true;
    }

    public User findUserByUserName(String userName){
        User user = em.find(User.class,userName);
        if(user == null){
            throw new IllegalStateException("No user with given userName");
        }
        return user;
    }

}
