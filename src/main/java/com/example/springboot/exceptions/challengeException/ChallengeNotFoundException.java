package com.example.springboot.exceptions.challengeException;

// This exception is thrown when attempting to retrieve a user from the database, but the user does not exist.
public class ChallengeNotFoundException extends Exception{

    public ChallengeNotFoundException(String message) {
        super(message);
    }
}
