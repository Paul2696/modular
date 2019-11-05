package com.modular.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.StringUtils;
import org.apache.johnzon.mapper.JohnzonIgnoreNested;

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
    private String learningType;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type")
    private UserType userType;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "userCourse",
        joinColumns = @JoinColumn(name = "idUser"),
        inverseJoinColumns = @JoinColumn(name = "idCourse")
    )
    @JsonIgnoreProperties("users")
    private Set<Course> courses;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUser")
    @JsonIgnoreProperties("user")
    private Set<HomeworkResponse> responses;

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

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
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

    public Set<HomeworkResponse> getResponses() {
        return responses;
    }

    public void setResponses(Set<HomeworkResponse> responses) {
        this.responses = responses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public String getLearningType() {
        return learningType;
    }

    public void setLearningType(String learningType) {
        this.learningType = learningType;
    }

    @JsonProperty
    public boolean hasLearningType(){
        return !StringUtils.isBlank(learningType);
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
                ", learningType='" + learningType + '\'' +
                ", userType=" + userType +
                ", courses=" + courses +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, name, password, email, learningType, userType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return idUser == user.idUser;
    }

}
