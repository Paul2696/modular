package com.modular.persistence.dao;

import com.modular.persistence.model.Homework;

import java.util.Set;

public interface HomeworkDAO extends BaseDAO <Homework> {
    public boolean exists(int id);
    Set<Homework> getAll() throws DataBaseException;
}
