package com.modular.persistence.model;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
    private User sender;
    private User receiver;
    private List<Message> conversation = new ArrayList<>();

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public List<Message> getConversation() {
        return conversation;
    }

    public void setConversation(List<Message> conversation) {
        this.conversation = conversation;
    }

    public static Conversation createConversation(List<Chat> chats){
        Conversation conversation = new Conversation();
        if(chats != null && !chats.isEmpty()){
            conversation.setSender(chats.get(0).getIdUser());
            conversation.setReceiver(chats.get(0).getIdUser1());
            for(Chat chat : chats){
                conversation.addMessage(new Message(chat.getDate(), chat.getMessage(), chat.getIdUser(), chat.getIdUser1()));
            }
        }
        return conversation;
    }

    public void addMessage(Message message){
        conversation.add(message);
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "sender=" + sender +
                ", receiver=" + receiver +
                ", conversation=" + conversation +
                '}';
    }
}
