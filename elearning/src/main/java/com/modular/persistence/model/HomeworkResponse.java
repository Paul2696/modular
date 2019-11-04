package com.modular.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.johnzon.mapper.JohnzonIgnore;
import org.apache.johnzon.mapper.JohnzonIgnoreNested;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "homeworkResponse")
@JsonIgnoreProperties(ignoreUnknown = true)
public class HomeworkResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idHomeworkResponse;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idHomework")
    @JsonIgnoreProperties("responses")
    private Homework homework;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUser")
    @JsonIgnoreProperties({"courses", "responses"})
    private User user;
    private int grade;
    private boolean sent;
    private String textResponse;
    private Date sended;
    @JsonIgnore
    private byte[] response;
    private String fileExtension;

    public int getIdHomeworkResponse() {
        return idHomeworkResponse;
    }

    public void setIdHomeworkResponse(int idHomeworkResponse) {
        this.idHomeworkResponse = idHomeworkResponse;
    }

    public Homework getHomework() {
        return homework;
    }

    public void setHomework(Homework homework) {
        this.homework = homework;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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


    public byte[] getResponse() {
        return response;
    }

    public void setResponse(byte[] response) {
        this.response = response;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    @JsonProperty
    public boolean isGraded() {
        return grade > 0;
    }

    @Override
    public String toString() {
        return "HomeworkResponse{" +
                "idHomeworkResponse=" + idHomeworkResponse +
                ", homework=" + homework +
                ", user=" + user +
                ", grade=" + grade +
                ", sent=" + sent +
                ", textResponse='" + textResponse + '\'' +
                ", sended=" + sended +
                ", fileExtension='" + fileExtension + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HomeworkResponse that = (HomeworkResponse) o;
        return idHomeworkResponse == that.idHomeworkResponse &&
                grade == that.grade &&
                sent == that.sent &&
                Objects.equals(homework, that.homework) &&
                Objects.equals(user, that.user) &&
                Objects.equals(textResponse, that.textResponse) &&
                Objects.equals(sended, that.sended) &&
                Objects.equals(fileExtension, that.fileExtension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idHomeworkResponse, homework, user, grade, sent, textResponse, sended, fileExtension);
    }
}
