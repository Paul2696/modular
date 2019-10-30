package com.modular.persistence.dao.impl;

import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.dao.QuestionDAO;
import com.modular.persistence.model.Question;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class QuestionDAOImpl implements QuestionDAO {
    private static final Logger logger = Logger.getLogger(QuestionDAOImpl.class);
    @PersistenceUnit(unitName = "PERSISTENCE")
    private EntityManagerFactory ef;

    @Override
    public void create(Question entity) throws DataBaseException {

    }

    @Override
    public Question get(int key) throws DataBaseException {
        return null;
    }

    @Override
    public void update(Question entity) throws DataBaseException {

    }

    @Override
    public void delete(Question entity) throws DataBaseException {

    }

    @Override
    public List<Question> getAllQuestions() throws DataBaseException {
        try{
            EntityManager em = ef.createEntityManager();
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Question> criteriaQuery = criteriaBuilder.createQuery(Question.class);
            Root<Question> root = criteriaQuery.from(Question.class);
            criteriaQuery.select(root);
            return em.createQuery(criteriaQuery).getResultList();
        }
        catch(Exception e){
            logger.debug("Query Failed", e);
            throw new DataBaseException("Algo salio mal");
        }
    }
}
