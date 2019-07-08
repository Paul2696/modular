package com.modular.persistence.dao.impl;

import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.dao.UserDAO;
import com.modular.persistence.model.User;
import org.apache.log4j.Logger;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private static final Logger logger = Logger.getLogger(UserDAOImpl.class);
    private EntityManager em = PersistenceManager.INSTANCE.getEntityManager();

    @Override
    public void create(User entity) throws DataBaseException {
        try {
            logger.info("Creating user " + entity);
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            logger.info("User " + entity + " created succesfully.");
        }
        catch (EntityExistsException eee){
            logger.debug("The user already exists", eee);
            throw new DataBaseException("El usuario " + entity + " ya existe");
        }
        catch (Exception e) {
            logger.debug("Something went wrong", e);
            throw new DataBaseException("No se pudo crear el usuario");
        }

    }

    @Override
    public User get(int key) throws DataBaseException {
        try{
            User user = em.find(User.class, key);
            if(user == null){
                throw new DataBaseException("No existe un usuario con la llave: " + key);
            }
            return user;
        }
        catch(IllegalArgumentException iae){
            throw new DataBaseException("No se pudo encontrar el elemento");
        }
    }

    @Override
    public void update(User entity) throws DataBaseException {
        try{
            em.getTransaction().begin();
            em.refresh(entity);
            em.getTransaction().commit();
        }
        catch(EntityNotFoundException enfe){
            throw new DataBaseException("El usuario " + entity + " no existe");
        }
        catch(Exception e){
            throw new DataBaseException("No se pudo actualizar al usuario: " + entity);
        }
    }

    @Override
    public void delete(User entity) throws DataBaseException {
        try{
            em.getTransaction().begin();
            em.remove(entity);
            em.getTransaction().commit();
        }
        catch(Exception e){
            throw new DataBaseException("No se pudo eliminar al usuario: " + entity);
        }
    }

    @Override
    public List<User> getAllUsers() throws DataBaseException{
        try{
            List<User> users = em.createNativeQuery("SELECT * FROM user").getResultList();
            return users;
        }
        catch(Exception e){
            throw new DataBaseException("Algo salio mal");
        }
    }
}
