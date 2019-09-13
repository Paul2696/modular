package com.modular.persistence.dao;

import com.modular.persistence.model.Homework;

import java.util.List;

public interface HomeworkDAO extends BaseDAO <Homework> {
    public boolean exists(int id);
    List<Homework> getAll() throws DataBaseException;
}
