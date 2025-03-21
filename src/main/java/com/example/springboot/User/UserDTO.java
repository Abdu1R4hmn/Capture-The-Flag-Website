package com.example.springboot.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {

    @NotBlank(message = "Username is required!")
    @Size(min = 3, message = "Username must be at least 3 Characters!")
    @Size(min = 20, message = "Username must be at most 20 Characters!")
    private String username;

    @NotBlank(message = "Email is required!")
    @Email(message = "Email is not in valid format!")
    private String email;

    @NotBlank(message = "Password is required!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])$",
            message = "Password must contain 1 Uppercase letter, 1 Lowercase letter and 1 number")
    private String password;
}
