package com.modular.persistence.dao;

public class IncorrectPasswordException extends Exception{
    public IncorrectPasswordException(String message){
        super(message);
    }
}
