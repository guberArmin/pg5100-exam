package no.kristiania.exam.tsdes.backend.services;

import no.kristiania.exam.tsdes.backend.entities.Copy;
import no.kristiania.exam.tsdes.backend.entities.Item;
import no.kristiania.exam.tsdes.backend.entities.User;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Service
@Transactional
//This class is adaptation of class from:
//https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/intro/exercise-solutions/quiz-game/part-11/backend/src/test/java/org/tsdes/intro/exercises/quizgame/backend/service/ResetService.java
public class ResetService {
    @PersistenceContext
    private EntityManager entityManager;

    public void resetDatabase(){
        Query query = entityManager.createNativeQuery("DELETE FROM user_roles");
        query.executeUpdate();

        deleteEntities(Copy.class);
        deleteEntities(User.class);
        deleteEntities(Item.class);
    }

    private void deleteEntities(Class<?> entity) {
        if(entity == null || entity.getAnnotation(Entity.class) == null){
            throw new IllegalArgumentException("Invalid non-entity class");
        }

        String name = entity.getSimpleName();

        /*
            Note: we passed as input a Class<?> instead of a String to
            avoid SQL injection. However, being here just test code, it should
            not be a problem. But, as a good habit, always be paranoiac about
            security, above all when you have code that can delete the whole
            database...
         */

        Query query = entityManager.createQuery("delete from " + name);
        query.executeUpdate();
    }
}
