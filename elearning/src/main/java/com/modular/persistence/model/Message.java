package com.modular.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.Objects;

public class Message {
    private Date date;
    private String message;
    private String sender;
    private String receiver;
    private int idSender;
    private int idReceiver;
    @JsonIgnore
    private User userSender;
    @JsonIgnore
    private User userReceiver;

    public Message(){}

    public Message(Date date, String message, User sender, User receiver){
        this.date = date;
        this.message = message;
        this.sender = sender.getName();
        this.receiver = receiver.getName();
        this.idSender = sender.getIdUser();
        this.idReceiver = receiver.getIdUser();
        this.userSender = sender;
        this.userReceiver = receiver;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getIdSender() {
        return idSender;
    }

    public void setIdSender(int idSender) {
        this.idSender = idSender;
    }

    @Override
    public String toString() {
        return "Message{" +
                "date=" + date +
                ", message='" + message + '\'' +
                ", sender=" + sender +
                ", receiver=" + receiver +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(date, message1.date) &&
                Objects.equals(message, message1.message) &&
                Objects.equals(sender, message1.sender) &&
                Objects.equals(receiver, message1.receiver);
    }

}
