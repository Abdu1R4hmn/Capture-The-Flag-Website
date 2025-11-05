package com.example.springboot.exceptions.categoryException;

import com.example.springboot.responses.ApiResponseDto;
import com.example.springboot.responses.ApiResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;

@RestControllerAdvice
public class CategoryServiceExceptionHandler {

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiResponseDto<?>> handleCategoryNotFound(CategoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDto<>(ApiResponseStatus.FAIL.name(), ex.getMessage()));
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ApiResponseDto<?>> handleCategoryExists(CategoryAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponseDto<>(ApiResponseStatus.FAIL.name(), ex.getMessage()));
    }

    @ExceptionHandler(CategoryDeleteConstraintException.class)
    public ResponseEntity<ApiResponseDto<?>> handleDeleteConstraint(CategoryDeleteConstraintException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDto<>(ApiResponseStatus.FAIL.name(), ex.getMessage()));
    }


}
