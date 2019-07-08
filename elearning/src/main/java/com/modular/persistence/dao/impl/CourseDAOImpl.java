package com.modular.persistence.dao.impl;

import com.modular.persistence.dao.CourseDAO;
import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.model.Course;
import org.apache.log4j.Logger;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

public class CourseDAOImpl implements CourseDAO {
    private static final Logger logger = Logger.getLogger(CourseDAOImpl.class);
    private EntityManager em = PersistenceManager.INSTANCE.getEntityManager();

    @Override
    public void create(Course entity) throws DataBaseException {
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        }
        catch (EntityExistsException eee){
            throw new DataBaseException("El curso " + entity + " ya existe");
        }
        catch (Exception e) {
            throw new DataBaseException("No se pudo crear el curso");
        }
    }

    @Override
    public Course get(int key) throws DataBaseException {
        try {
            Course course = em.find(Course.class, key);
            if(course == null){
                throw new DataBaseException("No existe un curso con la llave: " + key);
            }

            return course;
        }
        catch(IllegalArgumentException iae){
            throw new DataBaseException("No se pudo encontrar el elemento");
        }
    }

    @Override
    public void update(Course entity) throws DataBaseException {
        try{
            em.getTransaction().begin();
            em.refresh(entity);
            em.getTransaction().commit();
        }
        catch(EntityNotFoundException enfe){
            throw new DataBaseException("El curso " + entity + " no existe");
        }
        catch(Exception e){
            throw new DataBaseException("No se pudo actualizar el curso: " + entity);
        }
    }

    @Override
    public void delete(Course entity) throws DataBaseException {
        try{
            em.getTransaction().begin();
            em.remove(entity);
            em.getTransaction().commit();
        }
        catch(Exception e){
            throw new DataBaseException("No se pudo eliminar el curso: " + entity);
        }
    }
}
