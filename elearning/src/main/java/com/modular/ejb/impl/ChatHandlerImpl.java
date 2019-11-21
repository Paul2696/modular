package com.modular.ejb.impl;

import com.modular.ejb.ChatHandler;
import com.modular.persistence.dao.ChatDAO;
import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.dao.UserDAO;
import com.modular.persistence.model.Chat;
import com.modular.persistence.model.Conversation;
import com.modular.persistence.model.Message;
import com.modular.persistence.model.User;

import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.*;

@Singleton
public class ChatHandlerImpl implements ChatHandler {
    private Map<Integer, Queue<Message>> newMessages = new HashMap<>();
    @Inject
    private ChatDAO chatDAO;

    @Override
    public void createNewMessage(Chat message) throws DataBaseException {
        Queue<Message> messages = newMessages.get(message.getUser1().getIdUser());
        if(messages == null) {
            messages = new LinkedList<>();
        }
        messages.add(new Message(message.getDate(), message.getMessage(),message.getUser(), message.getUser1()));
        chatDAO.create(message);
    }

    @Override
    public Message getNewMessage(User user) {
        Queue<Message> messages = newMessages.get(user.getIdUser());
        if(messages != null && messages.size() > 0) {
            return messages.poll();
        }
        return null;
    }

    @Override
    public List<Conversation> getConversation(User user) throws DataBaseException {
        List<Chat> chats = chatDAO.getAllConversations(user.getIdUser());
        List<Conversation> conversation = Conversation.createListOfConversations(chats);

        return conversation;
    }
}
