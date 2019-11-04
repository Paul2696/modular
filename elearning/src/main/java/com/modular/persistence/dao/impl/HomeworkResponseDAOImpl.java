package com.modular.persistence.dao.impl;

import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.dao.HomeworkResponseDAO;
import com.modular.persistence.model.HomeworkResponse;
import org.apache.log4j.Logger;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class HomeworkResponseDAOImpl implements HomeworkResponseDAO {
    private static final Logger logger = Logger.getLogger(HomeworkDAOImpl.class);
    @PersistenceUnit(unitName = "PERSISTENCE")
    private EntityManagerFactory ef;

    @Override
    public void create(HomeworkResponse entity) throws DataBaseException {
        try{
            EntityManager em = ef.createEntityManager();
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        }
        catch(EntityExistsException eee){
            logger.debug(eee.getMessage(), eee);
            throw new DataBaseException("La tarea " + entity + "ya se subio");
        }
        catch(Exception e){
            logger.debug(e.getMessage());
            throw new DataBaseException("No se pudo subir la tarea");
        }
    }

    @Override
    public HomeworkResponse get(int key) throws DataBaseException{
        try {
            EntityManager em = ef.createEntityManager();
            HomeworkResponse homework = em.find(HomeworkResponse.class, key);
            if(homework == null){
                throw new DataBaseException("No existe una tarea con la llave: " + key);
            }
            return homework;
        }
        catch(IllegalArgumentException iae){
            logger.debug(iae.getMessage(), iae);
            throw new DataBaseException("No se pudo encontrar el elemento");
        }
    }

    @Override
    public void update(HomeworkResponse entity) throws DataBaseException{
        try{
            EntityManager em = ef.createEntityManager();
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        }
        catch(EntityNotFoundException enfe){
            logger.debug(enfe.getMessage(), enfe);
            throw new DataBaseException("La tarea " + entity + "no existe");
        }
        catch(Exception e){
            logger.debug(e.getMessage());
            throw new DataBaseException("No se pudo actualizar la tarea");
        }
    }


    @Override
    public void delete(HomeworkResponse entity) throws DataBaseException{

    }

    public List<HomeworkResponse> getAllHomeworks(int idHomework)throws DataBaseException{
        try{
            EntityManager em = ef.createEntityManager();
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<HomeworkResponse> criteriaQuery = criteriaBuilder.createQuery(HomeworkResponse.class);
            Root<HomeworkResponse> root = criteriaQuery.from(HomeworkResponse.class);
            Predicate predicate1 = criteriaBuilder.equal(root.get("homework").get("idHomework"), idHomework);
            criteriaQuery.select(root).where(predicate1);
            return em.createQuery(criteriaQuery).getResultList();
        }
        catch(Exception e){
            logger.debug(e);
            throw new DataBaseException("Algo salio mal");
        }
    }

    @Override
    public void setGRades(List<HomeworkResponse> responses) throws DataBaseException {
        EntityManager em = ef.createEntityManager();
        try {
            em.getTransaction().begin();
            for(HomeworkResponse hr : responses) {
               em.merge(hr);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.debug(e);
            throw new DataBaseException("Algo salio mal");
        }
    }


}
