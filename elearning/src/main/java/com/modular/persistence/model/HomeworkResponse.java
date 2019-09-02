package com.modular.persistence.model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "homeworkResponse")
public class HomeworkResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idHomeworkResponse;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idHomework")
    private Homework idHomework;
    private int idUser;
    private int grade;
    private boolean sent;
    private String textResponse;
    private Date sended;
    private byte[] response;
    private String fileExtension;

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

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
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
                ", response=" + Arrays.toString(response) +
                ", fileExtension='" + fileExtension + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HomeworkResponse that = (HomeworkResponse) o;
        return idHomeworkResponse == that.idHomeworkResponse &&
                idUser == that.idUser &&
                grade == that.grade &&
                sent == that.sent &&
                Objects.equals(idHomework, that.idHomework) &&
                Objects.equals(textResponse, that.textResponse) &&
                Objects.equals(sended, that.sended) &&
                Arrays.equals(response, that.response) &&
                Objects.equals(fileExtension, that.fileExtension);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(idHomeworkResponse, idHomework, idUser, grade, sent, textResponse, sended, fileExtension);
        result = 31 * result + Arrays.hashCode(response);
        return result;
    }
}
