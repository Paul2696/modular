package com.modular.ejb;

import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.model.Chat;
import com.modular.persistence.model.Conversation;
import com.modular.persistence.model.Message;
import com.modular.persistence.model.User;

import javax.ejb.Local;
import java.util.List;

@Local
public interface ChatHandler {
    void createNewMessage(Chat message) throws DataBaseException;
    Message getNewMessage(User receiver);
    List<Conversation> getConversation(User user) throws DataBaseException;
}
