package com.modular.persistence.dao;

import com.modular.persistence.model.Course;

import java.util.List;

public interface CourseDAO extends BaseDAO <Course> {
    List<Course> getAllCourses() throws DataBaseException;
}
