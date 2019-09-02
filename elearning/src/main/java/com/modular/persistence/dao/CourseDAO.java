package com.modular.persistence.dao;

import com.modular.persistence.model.Course;

import java.util.Set;

public interface CourseDAO extends BaseDAO <Course> {
    Set<Course> getAllCourses() throws DataBaseException;
    Set<Course> getAllFromUser(int idUser) throws DataBaseException;
    boolean exists(int id);
}
