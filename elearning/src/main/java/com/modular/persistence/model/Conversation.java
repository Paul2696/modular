package com.modular.persistence.model;

import java.util.*;

public class Conversation {
    private Set<User> users = new HashSet<>(2);
    private List<Message> conversation = new ArrayList<>();

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUser(User user){
        users.add(user);
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
            Iterator<Chat> it = chats.iterator();
            Chat c = it.next();
            conversation.addUser(c.getUser());
            conversation.addUser(c.getUser1());
            for(Chat chat : chats){
                conversation.addMessage(new Message(chat.getDate(), chat.getMessage(), chat.getUser(), chat.getUser1()));
            }
        }
        return conversation;
    }

    public static List<Conversation> createListOfConversations(List<Chat> chat) {
        List<Conversation> conversation = new ArrayList<>();
        if(chat != null && !chat.isEmpty()){
            Iterator<Chat> it = chat.iterator();
            while (it.hasNext()) {
                Chat c = it.next();
                Conversation conv = new Conversation();
                conv.addUser(c.getUser());
                conv.addUser(c.getUser1());
                if(conversation.isEmpty()) {
                    conv.addMessage(new Message(c.getDate(), c.getMessage(), c.getUser(), c.getUser1()));
                    conversation.add(conv);
                }   else {
                    if(conversation.contains(conv)) {
                        int i = conversation.indexOf(conv);
                        conv = conversation.get(i);
                        conv.addMessage(new Message(c.getDate(), c.getMessage(), c.getUser(), c.getUser1()));
                    } else {
                        conv.addMessage(new Message(c.getDate(), c.getMessage(), c.getUser(), c.getUser1()));
                        conversation.add(conv);
                    }
                }
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
                "users=" + users +
                ", conversation=" + conversation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        return Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(users);
    }
}
