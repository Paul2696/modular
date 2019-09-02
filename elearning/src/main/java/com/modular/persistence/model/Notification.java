package com.modular.persistence.model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idNotification;
    private String message;
    private boolean seen = false;
    private Date date;
    private byte[] resource;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idCourse")
    private Course course;
    private String fileExtension;

    public int getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(int idNotification) {
        this.idNotification = idNotification;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public byte[] getResource() {
        return resource;
    }

    public void setResource(byte[] resource) {
        this.resource = resource;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "idNotification=" + idNotification +
                ", message='" + message + '\'' +
                ", seen=" + seen +
                ", date=" + date +
                ", resource=" + Arrays.toString(resource) +
                ", course=" + course +
                ", fileExtension='" + fileExtension + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return idNotification == that.idNotification &&
                seen == that.seen &&
                Objects.equals(message, that.message) &&
                Objects.equals(date, that.date) &&
                Arrays.equals(resource, that.resource) &&
                Objects.equals(course, that.course) &&
                Objects.equals(fileExtension, that.fileExtension);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(idNotification, message, seen, date, course, fileExtension);
        result = 31 * result + Arrays.hashCode(resource);
        return result;
    }
}
