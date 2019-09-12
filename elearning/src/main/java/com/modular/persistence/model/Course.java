package com.modular.persistence.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.Objects;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCourse")
    private int idCourse;
    private String name;
    private String password;
    private Date start;
    private Date end;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUser")
    private User idUser;
    @ManyToMany(mappedBy = "courses")
    private Set<User> users;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "idCourse")
    private Set<Homework> homework;


    public int getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(int idCourse) {
        this.idCourse = idCourse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public User getUser() {
        return idUser;
    }

    public void setUser(User user) {
        this.idUser = user;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Homework> getHomework() {
        return homework;
    }

    public void setHomework(Set<Homework> homework) {
        this.homework = homework;
    }

    @Override
    public String toString() {
        return "Course{" +
                "idCourse=" + idCourse +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", user=" + idUser +
                ", users=" + users +
                ", homework=" + homework +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return idCourse == course.idCourse &&
                Objects.equals(name, course.name) &&
                Objects.equals(password, course.password) &&
                Objects.equals(start, course.start) &&
                Objects.equals(end, course.end) &&
                Objects.equals(idUser, course.idUser) &&
                Objects.equals(users, course.users) &&
                Objects.equals(homework, course.homework);
    }
}
