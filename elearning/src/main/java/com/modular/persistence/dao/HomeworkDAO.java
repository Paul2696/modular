package com.modular.persistence.dao;

import com.modular.persistence.model.Homework;

public interface HomeworkDAO extends BaseDAO <Homework> {
    public boolean exists(int id);
}
