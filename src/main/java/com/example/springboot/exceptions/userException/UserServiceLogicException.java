package com.example.springboot.exceptions.userException;

public class UserServiceLogicException extends Exception {
    public UserServiceLogicException() {
        super();
    }
    public UserServiceLogicException(String message) {
        super(message);
    }
}
