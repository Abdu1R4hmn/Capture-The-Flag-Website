package com.example.springboot.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto<T> {
    private String status;
    private T response;
    private int totalPages;


    // ✅ Existing constructor (no totalPages)
    public ApiResponseDto(String status, T response) {
        this.status = status;
        this.response = response;
    }
}
