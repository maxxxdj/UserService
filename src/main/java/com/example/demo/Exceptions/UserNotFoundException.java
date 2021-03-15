package com.example.demo.Exceptions;

public class UserNotFoundException extends RuntimeException{

    UserNotFoundException(String name){
        super("Cannot find user with name" + name);
    }
}