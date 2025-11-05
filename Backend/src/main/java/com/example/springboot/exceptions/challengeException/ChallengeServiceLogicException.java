package com.example.springboot.exceptions.challengeException;


public class ChallengeServiceLogicException extends RuntimeException {
    public ChallengeServiceLogicException() {
        super("Unexpected challenge service error occurred.");
    }

    public ChallengeServiceLogicException(String message) {
        super(message);
    }
}
