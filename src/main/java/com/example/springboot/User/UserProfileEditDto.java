package com.example.springboot.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileEditDto {
    private String username;
    private String email;

    // For password change
    private String oldPassword;
    private String newPassword;
}
