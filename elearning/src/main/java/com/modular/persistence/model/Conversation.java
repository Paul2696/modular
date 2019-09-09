package com.modular.persistence.model;

import java.util.Iterator;
import java.util.TreeSet;
import java.util.Set;
import java.util.Objects;

public class Conversation {
    private User sender;
    private User receiver;
    private Set<Message> conversation = new TreeSet<>();

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

    public Set<Message> getConversation() {
        return conversation;
    }

    public void setConversation(Set<Message> conversation) {
        this.conversation = conversation;
    }

    public static Conversation createConversation(Set<Chat> chats){
        Conversation conversation = new Conversation();
        if(chats != null && !chats.isEmpty()){
            Iterator<Chat> it = chats.iterator();
            Chat c = it.next();
            conversation.setSender(c.getIdUser());
            conversation.setReceiver(c.getIdUser1());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        return Objects.equals(sender, that.sender) &&
                Objects.equals(receiver, that.receiver) &&
                Objects.equals(conversation, that.conversation);
    }

}
