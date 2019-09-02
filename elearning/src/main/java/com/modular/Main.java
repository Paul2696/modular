package com.modular;

import com.google.gson.Gson;
import com.modular.persistence.dao.impl.PersistenceManager;
import com.modular.persistence.model.Course;
import com.modular.persistence.model.User;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.Set;

public class Main {
    public static void main(String[] args){
        Gson gson = new Gson();
        Course course = new Course();
        course.setIdCourse(0);
        course.setName("Mi super curso");
        course.setUser(new User());
        course.setStart(new Date());
        course.setEnd(new Date());
        System.out.println(gson.toJson(course));
    }
}
