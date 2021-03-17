package com.example.demo.Exceptions;

public class EmptyDBException extends RuntimeException{

    public EmptyDBException(){
        super("There are no records in the database.");
    }
}