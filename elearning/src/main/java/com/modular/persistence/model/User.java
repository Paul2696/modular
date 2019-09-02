package com.modular.persistence.model;

import javax.persistence.*;
import java.util.TreeSet;
import java.util.Set;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUser;
    private String name;
    private String password;
    private String email;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type")
    private UserType userType;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "userCourse",
        joinColumns = @JoinColumn(name = "idUser"),
        inverseJoinColumns = @JoinColumn(name = "idCourse")
    )
    private Set<Course> courses;

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
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

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Course> getCourses() {
        return courses;
    }


    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public void addCourse(Course course) {
        if(courses == null){
            courses = new TreeSet<>();
        }
        courses.add(course);
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", userType=" + userType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return idUser == user.idUser &&
                Objects.equals(name, user.name) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) &&
                Objects.equals(userType, user.userType) &&
                Objects.equals(courses, user.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, name, password, email, userType, courses);
    }
}
