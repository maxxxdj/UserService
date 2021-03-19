package com.example.demo.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String userNotFoundHandler(UserNotFoundException ex){
        return ex.getMessage();
    }
    @ResponseBody
    @ExceptionHandler(EmptyDBException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String emptyDBException(EmptyDBException ex){
        return ex.getMessage();
    }

}