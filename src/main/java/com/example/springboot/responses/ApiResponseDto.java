package com.example.springboot.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT) // hides int = 0, boolean = false, etc.
public class ApiResponseDto<T> {
    private String status;
    private T response;
    private int totalPages;

    // Existing constructor
    public ApiResponseDto(String status, T response, int totalPages) {
        this.status = status;
        this.response = response;
        this.totalPages = totalPages;
    }

    public ApiResponseDto(String status, T response) {
        this.status = status;
        this.response = response;
    }
}
