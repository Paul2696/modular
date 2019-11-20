package com.modular.persistence.dao;

import com.modular.persistence.model.Chat;

import java.util.List;

public interface ChatDAO extends BaseDAO <Chat> {
    List<Chat> getConversation(int userId1, int userId2) throws DataBaseException;
    List<Chat> getAllConversations(int userId) throws DataBaseException;
}
