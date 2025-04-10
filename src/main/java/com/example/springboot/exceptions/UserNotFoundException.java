package com.example.springboot.exceptions;

// This exception is thrown when attempting to retrieve a user from the database, but the user does not exist.
public class UserNotFoundException extends Exception{

    public UserNotFoundException(String message) {
        super(message);
    }
}
