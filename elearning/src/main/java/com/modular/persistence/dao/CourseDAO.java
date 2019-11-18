package com.modular.persistence.dao;

import com.modular.persistence.model.Course;
import com.modular.persistence.model.User;

import java.util.List;
import java.util.Set;

public interface CourseDAO extends BaseDAO <Course> {
    List<Course> getAllCourses() throws DataBaseException;
    List<Course> getAllFromTeacher(int idUser) throws DataBaseException;
    List<Course> getAllFromStudent(int idUser) throws DataBaseException;
    List<User> getAllUsersFromCourse (int idCourse) throws DataBaseException;
    boolean exists(int id);
}
