package com.example.springboot.exceptions.categoryException;

// This exception serves as a generic exception for any unexpected errors or business logic violations that occur within the user service layer.
public class CategoryServiceLogicException extends Exception{

    public CategoryServiceLogicException(String message){
        super(message);
    }
}
