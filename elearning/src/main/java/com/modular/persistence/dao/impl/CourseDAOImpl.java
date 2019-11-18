package com.modular.persistence.dao.impl;

import com.modular.persistence.dao.CourseDAO;
import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.model.Course;
import com.modular.persistence.model.User;
import org.apache.log4j.Logger;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CourseDAOImpl implements CourseDAO {
    private static final Logger logger = Logger.getLogger(CourseDAOImpl.class);
    @PersistenceUnit(unitName = "PERSISTENCE")
    private EntityManagerFactory ef;

    @Override
    public void create(Course entity) throws DataBaseException {
        try {
            EntityManager em = ef.createEntityManager();
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        }
        catch (EntityExistsException eee){
            throw new DataBaseException("El curso " + entity + " ya existe");
        }
        catch (Exception e) {
            logger.debug("Query Failed", e);
            throw new DataBaseException("No se pudo crear el curso");
        }
    }

    @Override
    public Course get(int key) throws DataBaseException {
        try {
            EntityManager em = ef.createEntityManager();
            Course course = em.find(Course.class, key);
            if(course == null){
                throw new DataBaseException("No existe un curso con la llave: " + key);
            }

            return course;
        }
        catch(IllegalArgumentException iae){
            logger.debug("Query Failed", iae);
            throw new DataBaseException("No se pudo encontrar el elemento");
        }
    }

    @Override
    public void update(Course entity) throws DataBaseException {
        try{
            EntityManager em = ef.createEntityManager();
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        }
        catch(EntityNotFoundException enfe){
            throw new DataBaseException("El curso " + entity + " no existe");
        }
        catch(Exception e){
            logger.debug("Query Failed", e);
            throw new DataBaseException("No se pudo actualizar el curso: " + entity);
        }
    }

    @Override
    public void delete(Course entity) throws DataBaseException {
        try{
            EntityManager em = ef.createEntityManager();
            em.getTransaction().begin();
            em.remove(em.contains(entity) ? entity : em.merge(entity));
            em.getTransaction().commit();
        }
        catch(Exception e){
            logger.debug("delete failed", e);
            throw new DataBaseException("No se pudo eliminar el curso: " + entity);
        }
    }

    public List<Course> getAllCourses() throws DataBaseException{
        try{
            EntityManager em = ef.createEntityManager();
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);
            Root<Course> root = criteriaQuery.from(Course.class);
            criteriaQuery.select(root);
            return em.createQuery(criteriaQuery).getResultList();
        }
        catch(Exception e){
            logger.debug("Query Failed", e);
            throw new DataBaseException("Algo salio mal");
        }
    }

    public List<Course> getAllFromTeacher(int idUser) throws DataBaseException{
        try{
            EntityManager em = ef.createEntityManager();
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);
            Root<Course> root = criteriaQuery.from(Course.class);
            Predicate predicate = criteriaBuilder.equal(root.get("user").get("idUser"), idUser);
            criteriaQuery.select(root).where(predicate);
            return em.createQuery(criteriaQuery).getResultList();
        }
        catch(Exception e){
            logger.debug("Query Failed", e);
            throw new DataBaseException("Algo salio mal");
        }
    }

    @Override
    public List<Course> getAllFromStudent(int idUser) throws DataBaseException {
        return null;
    }

    @Override
    public List<User> getAllUsersFromCourse(int idCourse) throws DataBaseException {
        try{
            EntityManager em = ef.createEntityManager();
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);
            Predicate predicate = criteriaBuilder.equal(root.get("userCourse").get("idCourse"), idCourse);
            criteriaQuery.select(root).where(predicate);
            return em.createQuery(criteriaQuery).getResultList();
        }
        catch(Exception e){
            logger.debug("Query Failed", e);
            throw new DataBaseException("Algo Salio mal");
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
