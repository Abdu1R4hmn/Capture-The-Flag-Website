package com.example.springboot.exceptions.categoryException;


public class CategoryDeleteConstraintException extends RuntimeException {
    public CategoryDeleteConstraintException(String message) {
        super(message);
    }
}
