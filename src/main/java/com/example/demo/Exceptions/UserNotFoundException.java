package com.example.demo.Exceptions;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String name){
        super(String.format("Cannot find user with name %s",name));
    }
}