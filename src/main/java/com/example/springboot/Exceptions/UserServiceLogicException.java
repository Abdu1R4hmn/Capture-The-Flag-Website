package com.example.springboot.Exceptions;

// This exception serves as a generic exception for any unexpected errors or business logic violations that occur within the user service layer.
public class UserServiceLogicException extends Exception{

    public UserServiceLogicException(){
        super("Something went wrong. Please try again later!");
    }
}
