package com.modular.persistence.model;

import javax.persistence.*;
import java.util.Arrays;
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
    private int idCourse;
    private Date end;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "idHomework")
    private Set<HomeworkResponse> homeworkResponse;

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


    public int getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(int idCourse) {
        this.idCourse = idCourse;
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

    public Set<HomeworkResponse> getHomeworkResponse() {
        return homeworkResponse;
    }

    public void setHomeworkResponse(Set<HomeworkResponse> homeworkResponse) {
        this.homeworkResponse = homeworkResponse;
    }

    @Override
    public String toString() {
        return "Homework{" +
                "idHomework=" + idHomework +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", resource=" + Arrays.toString(resource) +
                ", idCourse=" + idCourse +
                ", end=" + end +
                ", homeworkResponse=" + homeworkResponse +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Homework homework = (Homework) o;
        return idHomework == homework.idHomework &&
                idCourse == homework.idCourse &&
                Objects.equals(name, homework.name) &&
                Objects.equals(description, homework.description) &&
                Arrays.equals(resource, homework.resource) &&
                Objects.equals(end, homework.end) &&
                Objects.equals(homeworkResponse, homework.homeworkResponse);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(idHomework, name, description, idCourse, end, homeworkResponse);
        result = 31 * result + Arrays.hashCode(resource);
        return result;
    }
}
