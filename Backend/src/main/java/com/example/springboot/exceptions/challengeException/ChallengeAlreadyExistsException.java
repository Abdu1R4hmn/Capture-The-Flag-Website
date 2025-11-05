package com.example.springboot.exceptions.challengeException;

// This exception is thrown when attempting to create a new user, but a user with the same identifier (e.g., username, email) already exists in the database.
public class ChallengeAlreadyExistsException extends Exception{

    public ChallengeAlreadyExistsException(String message) {
        super(message);
    }
}
