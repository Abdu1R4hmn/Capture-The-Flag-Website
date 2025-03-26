package com.example.springboot.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.management.relation.Role;

@Data
@AllArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "Username is required!")
    @Size(min = 3, message = "Username must be at least 3 Characters!")
    @Size(max = 20, message = "Username must be at most 20 Characters!")
    private String username;

    @NotBlank(message = "Email is required!")
    @Email(message = "Email is not in valid format!")
    private String email;

    @NotBlank(message = "Password is required!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "Password must contain 1 Uppercase letter, 1 Lowercase letter and 1 Number")
    @Size(min = 8, message = "Password must contain at least 8 characters!")
    private String password;


    private Role role;
}
