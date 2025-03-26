package com.example.springboot.Exceptions;

import com.example.springboot.Responses.ApiResponseDto;
import com.example.springboot.Responses.ApiResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;

@RestControllerAdvice
public class UserServiceExceptionHandler {

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<ApiResponseDto<?>> UserNotFoundExceptionHandler(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDto<>(ApiResponseStatus.FAIL.name(), exception.getMessage()));
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponseDto<?>> UserAlreadyExistsExceptionHandler(UserAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponseDto<>(ApiResponseStatus.FAIL.name(), exception.getMessage()));
    }

    @ExceptionHandler(value = UserServiceLogicException.class)
    public ResponseEntity<ApiResponseDto<?>> UserServiceLogicExceptionHandler(UserServiceLogicException exception) {
        return ResponseEntity.badRequest().body(new ApiResponseDto<>(ApiResponseStatus.FAIL.name(), exception.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<?>> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {

        ArrayList<String> errorMessage = new ArrayList<>();

        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessage.add(error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(new ApiResponseDto<>(ApiResponseStatus.FAIL.name(), errorMessage.toString()));
    }

}
