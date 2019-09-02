package com.modular.persistence.dao;

import com.modular.persistence.model.HomeworkResponse;

import java.util.Set;

public interface HomeworkResponseDAO extends BaseDAO<HomeworkResponse>{
    public Set<HomeworkResponse> getAllHomeworks(int idHomework) throws DataBaseException;
}
