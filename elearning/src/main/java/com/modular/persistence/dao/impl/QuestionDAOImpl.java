package com.modular.persistence.dao.impl;

import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.dao.QuestionDAO;
import com.modular.persistence.model.Answer;
import com.modular.persistence.model.Question;
import org.apache.log4j.Logger;
import com.modular.persistence.model.Question_;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
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
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Question> cq = cb.createQuery(Question.class);
            Root<Question> root = cq.from(Question.class);
            cq.select(root);
            return em.createQuery(cq).getResultList();
        }
        catch(Exception e){
            logger.debug("Query Failed", e);
            throw new DataBaseException("Algo salio mal");
        }
    }
}
