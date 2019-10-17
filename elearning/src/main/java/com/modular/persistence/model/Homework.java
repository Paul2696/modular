package com.modular.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.Objects;

@Entity
@Table(name = "homework")
public class Homework {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idHomework;
    private String name;
    private String description;
    private byte[] resource;
    private Date end;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "idHomework")
    @JsonIgnoreProperties("idHomework")
    private Set<HomeworkResponse> responses;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idCourse")
    @JsonIgnoreProperties("homework")
    private Course course;

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

    public Date getEnd() {
        return end;
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

    public byte[] getResource() {
        return resource;
    }

    public void setResource(byte[] resource) {
        this.resource = resource;
    }

    public Set<HomeworkResponse> getResponses() {
        return responses;
    }

    public void setResponses(Set<HomeworkResponse> responses) {
        this.responses = responses;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    @Override
    public String toString() {
        return "Homework{" +
                "idHomework=" + idHomework +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", idCourse=" + course.getIdCourse() +
                ", end=" + end +
                ", homeworkResponse=" + responses +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Homework homework = (Homework) o;
        return idHomework == homework.idHomework &&
                course.getIdCourse() == homework.getCourse().getIdCourse() &&
                Objects.equals(name, homework.name) &&
                Objects.equals(description, homework.description) &&
                Objects.equals(end, homework.end) &&
                Objects.equals(responses, homework.responses);
    }

}
