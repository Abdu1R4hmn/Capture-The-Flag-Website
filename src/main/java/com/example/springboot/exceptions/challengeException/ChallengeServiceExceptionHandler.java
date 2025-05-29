package com.example.springboot.exceptions.challengeException;

import com.example.springboot.exceptions.challengeException.*;
import com.example.springboot.responses.ApiResponseDto;
import com.example.springboot.responses.ApiResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChallengeServiceExceptionHandler {

    @ExceptionHandler(ChallengeNotFoundException.class)
    public ResponseEntity<ApiResponseDto<?>> handleChallengeNotFound(ChallengeNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDto<>(ApiResponseStatus.FAIL.name(), e.getMessage()));
    }

    @ExceptionHandler(ChallengeAlreadyExistsException.class)
    public ResponseEntity<ApiResponseDto<?>> handleChallengeAlreadyExists(ChallengeAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponseDto<>(ApiResponseStatus.FAIL.name(), e.getMessage()));
    }

    @ExceptionHandler(ChallengeServiceLogicException.class)
    public ResponseEntity<ApiResponseDto<?>> handleGenericChallengeError(ChallengeServiceLogicException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDto<>(ApiResponseStatus.FAIL.name(), e.getMessage()));
    }
}
