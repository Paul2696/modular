package com.modular.persistence.dao;

import com.modular.persistence.model.Answer;

import java.util.List;

public interface AnswerDAO extends BaseDAO<Answer> {
    List<Answer> getAllAnswer() throws DataBaseException;
}
