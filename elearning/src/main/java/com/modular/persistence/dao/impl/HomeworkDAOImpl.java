package com.modular.persistence.dao.impl;

import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.dao.HomeworkDAO;
import com.modular.persistence.model.Homework;
import com.modular.persistence.model.HomeworkResponse;
import com.modular.persistence.model.User;
import org.apache.log4j.Logger;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Set;
import java.util.TreeSet;

public class HomeworkDAOImpl implements HomeworkDAO {
    private static final Logger logger = Logger.getLogger(HomeworkDAOImpl.class);
    private EntityManager em = PersistenceManager.INSTANCE.getEntityManager();

    @Override
    public void create(Homework entity) throws DataBaseException {
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        }
        catch (EntityExistsException eee){
            throw new DataBaseException("La tarea " + entity + " ya existe");
        }
        catch (Exception e) {
            throw new DataBaseException("No se pudo crear la tarea");
        }
    }

    @Override
    public Homework get(int key) throws DataBaseException {
        try {
            Homework homework = em.find(Homework.class, key);
            if(homework == null){
                throw new DataBaseException("No existe una tarea con la llave: " + key);
            }

            return homework;
        }
        catch(IllegalArgumentException iae){
            throw new DataBaseException("No se pudo encontrar el elemento");
        }
    }

    @Override
    public void update(Homework entity) throws DataBaseException {
        try{
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        }
        catch(EntityNotFoundException enfe){
            throw new DataBaseException("La tarea " + entity + " no existe");
        }
        catch(Exception e){
            throw new DataBaseException("No se pudo actualizar la tarea: " + entity);
        }
    }

    @Override
    public void delete(Homework entity) throws DataBaseException {
        try{
            em.getTransaction().begin();
            em.remove(entity);
            em.getTransaction().commit();
        }
        catch(Exception e){
            throw new DataBaseException("No se pudo eliminar la tarea: " + entity);
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

    public Set<Homework> getAll()throws DataBaseException{
        try{
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Homework> criteriaQuery = criteriaBuilder.createQuery(Homework.class);
            Root<Homework> root = criteriaQuery.from(Homework.class);
            criteriaQuery.select(root);
            return new TreeSet<Homework>(em.createQuery(criteriaQuery).getResultList());
        }
        catch(Exception e){
            throw new DataBaseException("Algo salio mal");
        }
    }

}
