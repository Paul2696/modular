package com.modular;

import com.modular.persistence.dao.impl.PersistenceManager;
import com.modular.persistence.model.User;

import javax.persistence.EntityManager;
import java.util.List;

public class Main {
    public static void main(String[] args){
        EntityManager em = PersistenceManager.INSTANCE.getEntityManager();
        List<User> l = em.createNativeQuery("SELECT * FROM user;", User.class).getResultList();
        System.out.println(l);
        PersistenceManager.INSTANCE.close();
    }
}
