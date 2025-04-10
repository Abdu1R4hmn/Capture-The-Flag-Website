package com.example.springboot.exceptions;

// This exception is thrown when attempting to create a new user, but a user with the same identifier (e.g., username, email) already exists in the database.
public class UserAlreadyExistsException extends Exception{

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
