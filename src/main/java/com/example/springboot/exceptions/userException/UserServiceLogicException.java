package com.example.springboot.exceptions.userException;

// This exception serves as a generic exception for any unexpected errors or business logic violations that occur within the user service layer.
public class UserServiceLogicException extends Exception{

    public UserServiceLogicException(){
        super("Something went wrong. Please try again later!");
    }
}
