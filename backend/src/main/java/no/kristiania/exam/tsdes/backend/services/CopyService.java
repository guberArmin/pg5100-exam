package no.kristiania.exam.tsdes.backend.services;

import no.kristiania.exam.tsdes.backend.entities.Copy;
import no.kristiania.exam.tsdes.backend.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CopyService {
    @Autowired
    private EntityManager entityManager;

    public void sellOneCopyOfItem(String username,Long itemId){
        TypedQuery<Copy> query = entityManager.createNamedQuery(Copy.GET_COPY_FOR_ITEM_AND_USER,Copy.class);
        query.setParameter("username", username);
        query.setParameter("itemId", itemId);

        try{
            Copy copy = query.getSingleResult();
            copy.setNumberOfCopies(copy.getNumberOfCopies()-1);
            User user = entityManager.find(User.class,username);
            //When you sell copy you get 40 ducats
            user.increaseBalance(40);
        }catch (Exception e){
            throw new IllegalStateException("No duplicates for given item!");
        }
    }

    public List<Copy> getAllCopies(){
        TypedQuery<Copy> query = entityManager.createNamedQuery(Copy.GET_ALL_COPIES,Copy.class);
        return query.getResultList();
    }

}
