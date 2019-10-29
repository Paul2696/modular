package com.modular.persistence.dao.impl;

import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.dao.NotificationDAO;
import com.modular.persistence.model.Notification;
import org.apache.log4j.Logger;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class NotificationDAOImpl implements NotificationDAO {
    private static final Logger logger = Logger.getLogger(NotificationDAOImpl.class);
    @PersistenceUnit(unitName = "PERSISTENCE")
    private EntityManagerFactory ef;

    @Override
    public void create(Notification entity) throws DataBaseException {
        try {
            EntityManager em = ef.createEntityManager();
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        }
        catch (EntityExistsException eee){
            throw new DataBaseException("El post " + entity + " ya existe");
        }
        catch (Exception e) {
            throw new DataBaseException("No se pudo crear el post");
        }
    }

    @Override
    public Notification get(int key) throws DataBaseException {
        try {
            EntityManager em = ef.createEntityManager();
            Notification notification = em.find(Notification.class, key);
            if(notification == null){
                throw new DataBaseException("No existe el post con la llave: " + key);
            }

            return notification;
        }
        catch(IllegalArgumentException iae){
            throw new DataBaseException("No se pudo encontrar el elemento");
        }
    }

    @Override
    public void update(Notification entity) throws DataBaseException {
        try{
            EntityManager em = ef.createEntityManager();
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        }
        catch(EntityNotFoundException enfe){
            throw new DataBaseException("La notification " + entity + " no existe");
        }
        catch(Exception e){
            throw new DataBaseException("No se pudo actualizar el post: " + entity);
        }
    }

    @Override
    public void delete(Notification entity) throws DataBaseException {
        try{
            EntityManager em = ef.createEntityManager();
            em.getTransaction().begin();
            em.remove(em.contains(entity) ? entity : em.merge(entity));
            em.getTransaction().commit();
        }
        catch(Exception e){
            throw new DataBaseException("No se pudo eliminar la notificacion: " + entity);
        }
    }

    @Override
    public List<Notification> getAllNotifications(int courseId) throws DataBaseException {
        try{
            EntityManager em = ef.createEntityManager();
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Notification> criteriaQuery = criteriaBuilder.createQuery(Notification.class);
            Root<Notification> root = criteriaQuery.from(Notification.class);
            Predicate predicate = criteriaBuilder.equal(root.get("course"), courseId);
            criteriaQuery.select(root).where(predicate);
            return em.createQuery(criteriaQuery).getResultList();
        }
        catch(Exception e){
            throw new DataBaseException("Hubo un error al cargar las notificaciones");
        }
    }
    public boolean exists(int id){
        try{
            get(id);
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return false;
        }
        return true;
    }
}
