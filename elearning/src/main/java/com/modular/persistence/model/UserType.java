package com.modular.persistence.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "userType")
public class UserType {
    public static final int TEACHER = 1;
    public static final int STUDENT = 2;
    public static final int ADMIN = 3;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUserType;
    private String name;

    public int getIdUserType() {
        return idUserType;
    }

    public void setIdUserType(int idUserType) {
        this.idUserType = idUserType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserType{" +
                "idUserType=" + idUserType +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserType userType = (UserType) o;
        return idUserType == userType.idUserType &&
                Objects.equals(name, userType.name);
    }

}
