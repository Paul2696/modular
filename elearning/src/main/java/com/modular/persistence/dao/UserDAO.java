package com.modular.persistence.dao;

import com.modular.persistence.model.User;

import java.util.List;

public interface UserDAO extends BaseDAO <User> {
    List<User> getAllUsers() throws DataBaseException;
}
