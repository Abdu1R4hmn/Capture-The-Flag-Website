package com.example.springboot.User;

import com.example.springboot.Progress.Progress;
import lombok.AllArgsConstructor;
import lombok.Data;

import com.example.springboot.Role.Role;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private long id;
    private String username;
    private String email;
    private Role role;
    private LocalDateTime regDateAndTime;
    private List<Progress> progressList;

}
