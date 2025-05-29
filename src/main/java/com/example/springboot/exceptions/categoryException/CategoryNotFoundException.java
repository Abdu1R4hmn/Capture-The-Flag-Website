package com.example.springboot.exceptions.categoryException;

// This exception is thrown when attempting to retrieve a user from the database, but the user does not exist.
public class CategoryNotFoundException extends Exception{

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
