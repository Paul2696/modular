package com.modular.persistence.dao.impl;

import com.modular.persistence.dao.ChatDAO;
import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.model.Chat;
import org.apache.log4j.Logger;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class ChatDAOImpl implements ChatDAO {
    private static final Logger logger = Logger.getLogger(ChatDAOImpl.class);
    private EntityManager em = PersistenceManager.INSTANCE.getEntityManager();

    @Override
    public void create(Chat entity) throws DataBaseException {
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            throw new DataBaseException("No se pudo crear el mensaje");
        }
    }

    @Override
    public Chat get(int key) throws DataBaseException {
        //Not implemented
        return null;
    }

    @Override
    public void update(Chat entity) throws DataBaseException {
        //Not implemented
    }

    @Override
    public void delete(Chat entity) throws DataBaseException {
        try{
            em.getTransaction().begin();
            em.remove(entity);
            em.getTransaction().commit();
        }
        catch(Exception e){
            throw new DataBaseException("No se pudo eliminar el mensaje");
        }
    }

    @Override
    public List<Chat> getConversation(int userId1, int userId2) {
        //List<Chat> conversation = em.createNativeQuery("SELECT * FROM chat WHERE iduser = " + userId1 + "AND iduser1 = " + userId2).getResultList();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Chat> criteriaQuery = criteriaBuilder.createQuery(Chat.class);
        Root<Chat> root = criteriaQuery.from(Chat.class);
        Predicate predicate1 = criteriaBuilder.equal(root.get("iduser"), userId1);
        Predicate predicate2 = criteriaBuilder.equal(root.get("iduser1"), userId2);
        Predicate predicate3 = criteriaBuilder.and(predicate1, predicate2);
        criteriaQuery.select(root).where(predicate3);
        List<Chat> conversation = em.createQuery(criteriaQuery).getResultList();
        return null;
    }
}
