package com.example.springboot.User;


import com.example.springboot.exceptions.userException.UserAlreadyExistsException;
import com.example.springboot.exceptions.userException.UserNotFoundException;
import com.example.springboot.exceptions.userException.UserServiceLogicException;
import com.example.springboot.responses.ApiResponseDto;
import com.example.springboot.responses.ApiResponseStatus;
import com.example.springboot.util.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/user")
public class UserController {

    //  Variables
    private final UserService userService;
    private final EmailService mailService;

    //  Constructor
    public UserController(UserService userService, EmailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }

//    CRUD

    //  GETs All Users Data (for Admin).
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "get/all")
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getAllUsersData(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws UserServiceLogicException {
        return userService.getAllUsers(page, size);
    }

    //  GET a specific User by id. (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "get/{id}")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getUser(@PathVariable("id")long id) throws UserNotFoundException, UserServiceLogicException {
        return userService.getUser(id);
    }

    //  GET a specific User by Email. (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "get/email/{email}")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getUserEmail(@PathVariable("email")String email) throws UserNotFoundException, UserServiceLogicException {
        return userService.getUserEmail(email);
    }
    
    //   POST/Add User. (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "post")
    public ResponseEntity<ApiResponseDto<?>> addUser (@Valid @RequestBody UserRequestDto user) throws UserAlreadyExistsException, UserServiceLogicException {
        return userService.addUser(user);
    }

    //   Edit any user (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(path = "edit/{id}")
    public ResponseEntity<ApiResponseDto<?>> updateUser(@Valid @RequestBody UserEditDto user,@PathVariable("id")long id) throws UserAlreadyExistsException, UserNotFoundException, UserServiceLogicException {
        return userService.updateUser(user, id);
    }
    
    //  Edit own profile (User, Lecturer, Admin)
    @PreAuthorize("hasAnyRole('USER','LECTURER','ADMIN')")
    @PatchMapping("/profile/edit")
    public ResponseEntity<ApiResponseDto<?>> editProfile(
            @Valid @RequestBody UserProfileEditDto dto,
            Authentication authentication
    ) throws UserNotFoundException {
        String email = authentication.getName();
        return userService.editProfile(dto, email);
    }
    
    //   Delete own account (User, Lecturer, Admin)
    @PreAuthorize("hasAnyRole('USER','LECTURER','ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponseDto<?>> deleteCurrentUser(Authentication authentication) throws UserNotFoundException, UserServiceLogicException {
        String email = authentication.getName();
        return userService.deleteUserByEmail(email);
    }

    //   Get own profile (User, Lecturer, Admin)
    @PreAuthorize("hasAnyRole('USER','LECTURER','ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getMyProfile(Authentication authentication) throws UserNotFoundException, UserServiceLogicException {
        String email = authentication.getName();
        return userService.getUserEmail(email);
    }

    //   Delete any user (ADMIN only)
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER', 'USER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponseDto<?>> deleteUserById(@PathVariable("id") long id) throws UserNotFoundException, UserServiceLogicException {
        return userService.deleteUser(id);
    }

//    GET TOTAL NUMBER OF USERS (Admin only)
@PreAuthorize("hasAnyRole('ADMIN','LECTURER')")
    @GetMapping(path = "total")
    public long totalUsers(){
        return userService.totalUsers();
    }


    //   Register User. (Public)
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<?>> registerUser(@Valid @RequestBody UserRequestDto newUser)
            throws UserAlreadyExistsException, UserServiceLogicException {
        return userService.addUser(newUser);
    }


    //   Forgot Password (Public)
    @PostMapping("/auth/forgot-password")
    public ResponseEntity<ApiResponseDto<?>> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        try {
            userService.resetPasswordAndSendEmail(email);
            return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), "If this email exists, a reset link has been sent."));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDto<>(ApiResponseStatus.FAIL.name(), "User doesn't exist"));
        }
    }

}