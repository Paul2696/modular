package com.modular.persistence.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "chat")
public class Chat implements Serializable{
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "iduser")
    @EmbeddedId
    private User user1;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "iduser1")
    private User user2;
    private String message;
    private Date date;

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
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

    @Override
    public String toString() {
        return "Chat{" +
                "user1=" + user1 +
                ", user2=" + user2 +
                ", message='" + message + '\'' +
                ", date=" + date +
                '}';
    }
}
