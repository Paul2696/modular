package com.modular.persistence.dao.impl;

import com.modular.persistence.dao.ChatDAO;
import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.model.Chat;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class ChatDAOImpl implements ChatDAO {
    private static final Logger logger = Logger.getLogger(ChatDAOImpl.class);
    @PersistenceUnit(unitName = "PERSISTENCE")
    private EntityManagerFactory ef;

    @Override
    public void create(Chat entity) throws DataBaseException {
        try {
            EntityManager em = ef.createEntityManager();
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
            EntityManager em = ef.createEntityManager();
            em.getTransaction().begin();
            em.remove(em.contains(entity) ? entity : em.merge(entity));
            em.getTransaction().commit();
        }
        catch(Exception e){
            throw new DataBaseException("No se pudo eliminar el mensaje");
        }
    }

    @Override
    public List<Chat> getConversation(int userId1, int userId2) throws DataBaseException{
        try{
            EntityManager em = ef.createEntityManager();
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Chat> criteriaQuery = criteriaBuilder.createQuery(Chat.class);
            Root<Chat> root = criteriaQuery.from(Chat.class);
            Predicate predicate1 = criteriaBuilder.equal(root.get("user").get("idUser"), userId1);
            Predicate predicate2 = criteriaBuilder.equal(root.get("user").get("idUser"), userId2);
            Predicate predicate3 = criteriaBuilder.equal(root.get("user1").get("idUser"), userId1);
            Predicate predicate4 = criteriaBuilder.equal(root.get("user1").get("idUser"), userId2);
            Predicate predicate5 = criteriaBuilder.or(predicate1, predicate2);
            Predicate predicate6 = criteriaBuilder.or(predicate3, predicate4);
            Predicate predicate7 = criteriaBuilder.and(predicate5, predicate6);
            criteriaQuery.select(root).where(predicate7);
            return em.createQuery(criteriaQuery).getResultList();
        }
        catch(Exception e){
            logger.debug(e.getMessage(), e);
            throw new DataBaseException("No se pudo obtener la conversacion");
        }

    }

    @Override
    public List<Chat> getAllConversations(int userId) throws DataBaseException{
        try{
            EntityManager em = ef.createEntityManager();
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Chat> criteriaQuery = criteriaBuilder.createQuery(Chat.class);
            Root<Chat> root = criteriaQuery.from(Chat.class);
            Predicate predicate1 = criteriaBuilder.equal(root.get("user").get("idUser"), userId);
            Predicate predicate2 = criteriaBuilder.equal(root.get("user1").get("idUser"), userId);
            Predicate predicate3 = criteriaBuilder.or(predicate1, predicate2);
            criteriaQuery.select(root).where(predicate3);
            return em.createQuery(criteriaQuery).getResultList();
        }
        catch(Exception e){
            logger.debug(e.getMessage(), e);
            throw new DataBaseException("No se pudo obtener la conversacion");
        }

    }
}
