package com.modular;

import com.modular.persistence.dao.PersistenceManager;
import com.modular.persistence.model.User;
import com.modular.persistence.model.UserType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class Main {
    public static void main(String[] args){
        EntityManager em = PersistenceManager.INSTANCE.getEntityManager();
        List<User> l = em.createNativeQuery("SELECT * FROM user;", User.class).getResultList();
        System.out.println(l);
        PersistenceManager.INSTANCE.close();
    }
}
