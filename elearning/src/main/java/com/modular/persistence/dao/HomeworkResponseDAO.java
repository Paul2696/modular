package com.modular.persistence.dao;

import com.modular.persistence.model.HomeworkResponse;

import java.util.List;

public interface HomeworkResponseDAO extends BaseDAO<HomeworkResponse>{
    public List<HomeworkResponse> getAllHomeworks(int idHomework) throws DataBaseException;
}
