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
    private byte[] resource;
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


    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
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

    @Override
    public String toString() {
        return "Homework{" +
                "idHomework=" + idHomework +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", resource=" + Arrays.toString(resource) +
                ", course=" + course +
                ", end=" + end +
                '}';
    }

}
