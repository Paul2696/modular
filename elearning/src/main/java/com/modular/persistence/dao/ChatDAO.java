package com.modular.persistence.dao;

import com.modular.persistence.model.Chat;

import java.util.Set;

public interface ChatDAO extends BaseDAO <Chat> {
    public Set<Chat> getConversation(int userId1, int userId2) throws DataBaseException;
}
