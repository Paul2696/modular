package com.modular.persistence.dao.impl;

import com.modular.persistence.dao.AnswerDAO;
import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.model.Answer;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class AnswerDAOImpl implements AnswerDAO {
    private static final Logger logger = Logger.getLogger(AnswerDAOImpl.class);
    @PersistenceUnit(unitName = "PERSISTENCE")
    private EntityManagerFactory ef;

    @Override
    public List<Answer> getAllAnswer() throws DataBaseException {
        try{
            EntityManager em = ef.createEntityManager();
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Answer> criteriaQuery = criteriaBuilder.createQuery(Answer.class);
            Root<Answer> root = criteriaQuery.from(Answer.class);
            criteriaQuery.select(root);
            return em.createQuery(criteriaQuery).getResultList();
        }
        catch(Exception e){
            logger.debug("Query Failed", e);
            throw new DataBaseException("Algo salio mal");
        }
    }

    @Override
    public void create(Answer entity) throws DataBaseException {

    }

    @Override
    public Answer get(int key) throws DataBaseException {
        return null;
    }

    @Override
    public void update(Answer entity) throws DataBaseException {

    }

    @Override
    public void delete(Answer entity) throws DataBaseException {

    }
}
