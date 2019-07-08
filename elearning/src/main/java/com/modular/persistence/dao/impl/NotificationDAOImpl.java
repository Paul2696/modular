package com.modular.persistence.dao.impl;

import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.dao.NotificationDAO;
import com.modular.persistence.model.Notification;
import org.apache.log4j.Logger;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

public class NotificationDAOImpl implements NotificationDAO {
    private static final Logger logger = Logger.getLogger(NotificationDAOImpl.class);
    private EntityManager em = PersistenceManager.INSTANCE.getEntityManager();

    @Override
    public void create(Notification entity) throws DataBaseException {
        try {
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
            em.getTransaction().begin();
            em.refresh(entity);
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
            em.getTransaction().begin();
            em.remove(entity);
            em.getTransaction().commit();
        }
        catch(Exception e){
            throw new DataBaseException("No se pudo eliminar la notificacion: " + entity);
        }
    }
}
