package com.example.springboot.exceptions.categoryException;

// This exception is thrown when attempting to create a new user, but a user with the same identifier (e.g., username, email) already exists in the database.
public class CategoryAlreadyExistsException extends Exception{

    public CategoryAlreadyExistsException(String message) {
        super(message);
    }
}
