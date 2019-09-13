package com.modular.persistence.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "chat")
public class Chat implements Serializable{
    @Id
    private int idChat;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUser")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUser1")
    private User user1;
    private String message;
    private Date date = Calendar.getInstance().getTime();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIdChat() {
        return idChat;
    }

    public void setIdChat(int idChat) {
        this.idChat = idChat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return idChat == chat.idChat &&
                Objects.equals(user, chat.user) &&
                Objects.equals(user1, chat.user1) &&
                Objects.equals(message, chat.message) &&
                Objects.equals(date, chat.date);
    }


    @Override
    public String toString() {
        return "Chat{" +
                ", idChat=" + idChat +
                ", user=" + user +
                ", user1=" + user1 +
                ", message='" + message + '\'' +
                ", date=" + date +
                '}';
    }
}
