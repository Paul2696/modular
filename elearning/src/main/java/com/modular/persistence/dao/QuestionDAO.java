package com.modular.persistence.dao;

import com.modular.persistence.model.Question;

import java.util.List;

public interface QuestionDAO extends BaseDAO<Question> {
    List<Question> getAllQuestions() throws DataBaseException;
}
