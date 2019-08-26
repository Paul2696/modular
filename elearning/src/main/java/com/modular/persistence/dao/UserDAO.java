package com.modular.persistence.dao;

import com.modular.persistence.model.Course;
import com.modular.persistence.model.User;

import java.util.List;

public interface UserDAO extends BaseDAO <User> {
    List<User> getAllUsers() throws DataBaseException;
    boolean exist(int id);
    void enrollCourse(Course course, User user);
}
