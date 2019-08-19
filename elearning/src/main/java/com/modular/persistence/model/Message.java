package com.modular.persistence.model;

import java.util.Date;

public class Message {
    private Date date;
    private String message;
    private String sender;
    private String receiver;

    public Message(){}

    public Message(Date date, String message, User sender, User receiver){
        this.date = date;
        this.message = message;
        this.sender = sender.getName();
        this.receiver = receiver.getName();
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

    @Override
    public String toString() {
        return "Message{" +
                "date=" + date +
                ", message='" + message + '\'' +
                ", sender=" + sender +
                ", receiver=" + receiver +
                '}';
    }
}
