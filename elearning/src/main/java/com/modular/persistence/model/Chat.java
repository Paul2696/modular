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
    private User idUser;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUser1")
    private User idUser1;
    private String message;
    private Date date = Calendar.getInstance().getTime();

    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    public User getIdUser1() {
        return idUser1;
    }

    public void setIdUser1(User idUser1) {
        this.idUser1 = idUser1;
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
                Objects.equals(idUser, chat.idUser) &&
                Objects.equals(idUser1, chat.idUser1) &&
                Objects.equals(message, chat.message) &&
                Objects.equals(date, chat.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idChat, idUser, idUser1, message, date);
    }

    @Override
    public String toString() {
        return "Chat{" +
                ", idChat=" + idChat +
                ", idUser=" + idUser +
                ", idUser1=" + idUser1 +
                ", message='" + message + '\'' +
                ", date=" + date +
                '}';
    }
}
