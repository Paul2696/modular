package com.modular.persistence.model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;

@Entity
@Table(name = "homework")
public class Homework {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idHomework;
    private String name;
    private String description;
    private int grade;
    private boolean sent;
    private byte[] resource;
    private byte[] response;
    private String textResponse;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idCourse")
    private Course course;
    private Date end;

    public int getIdHomework() {
        return idHomework;
    }

    public void setIdHomework(int idHomework) {
        this.idHomework = idHomework;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
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

    public String getTextResponse() {
        return textResponse;
    }

    public void setTextResponse(String textResponse) {
        this.textResponse = textResponse;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Date getEnd() {
        return end;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Homework{" +
                "idHomework=" + idHomework +
                ", name='" + name + '\'' +
                ", grade=" + grade +
                ", resource=" + Arrays.toString(resource) +
                ", response=" + Arrays.toString(response) +
                ", textResponse='" + textResponse + '\'' +
                ", course=" + course +
                ", end=" + end +
                ", sent=" + sent +
                ", description=" + description +
                '}';
    }

}
