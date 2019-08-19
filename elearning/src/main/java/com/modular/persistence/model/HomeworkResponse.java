package com.modular.persistence.model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;

@Entity
@Table(name = "homeworkResponse")
public class HomeworkResponse {
    @Id
    private int idHomeworkResponse;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idHomework")
    private Homework idHomework;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUser")
    private User idUser;
    private int grade;
    private boolean sent;
    private String textResponse;
    private Date sended;
    private byte[] resource;
    private byte[] response;

    public int getIdHomeworkResponse() {
        return idHomeworkResponse;
    }

    public void setIdHomeworkResponse(int idHomeworkResponse) {
        this.idHomeworkResponse = idHomeworkResponse;
    }

    public Homework getIdHomework() {
        return idHomework;
    }

    public void setIdHomework(Homework idHomework) {
        this.idHomework = idHomework;
    }

    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getTextResponse() {
        return textResponse;
    }

    public void setTextResponse(String textResponse) {
        this.textResponse = textResponse;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public Date getSended() {
        return sended;
    }

    public void setSended(Date sended) {
        this.sended = sended;
    }

    public byte[] getResource() {
        return resource;
    }

    public void setResource(byte[] resource) {
        this.resource = resource;
    }

    public byte[] getResponse() {
        return response;
    }

    public void setResponse(byte[] response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "HomeworkResponse{" +
                "idHomeworkResponse=" + idHomeworkResponse +
                ", idHomework=" + idHomework +
                ", idUser=" + idUser +
                ", grade=" + grade +
                ", sent=" + sent +
                ", textResponse='" + textResponse + '\'' +
                ", sended=" + sended +
                ", resource=" + Arrays.toString(resource) +
                ", response=" + Arrays.toString(response) +
                '}';
    }
}
