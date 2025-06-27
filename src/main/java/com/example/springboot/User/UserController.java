package com.example.springboot.User;


import com.example.springboot.exceptions.userException.UserAlreadyExistsException;
import com.example.springboot.exceptions.userException.UserNotFoundException;
import com.example.springboot.exceptions.userException.UserServiceLogicException;
import com.example.springboot.responses.ApiResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication; // <-- Use this import
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {

    //  Variables
    private final UserService userService;

    //  Constructor
    public UserController(UserService userService){
        this.userService = userService;
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

    //   Delete own account (User, Lecturer, Admin)
    @PreAuthorize("hasAnyRole('USER','LECTURER','ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponseDto<?>> deleteCurrentUser(Authentication authentication) throws UserNotFoundException, UserServiceLogicException {
        String email = authentication.getName();
        return userService.deleteUserByEmail(email);
    }

    //   Delete any user (ADMIN only)
    @PreAuthorize("hasRole('ADMIN', 'LECTURER', 'USER')")
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
    public ResponseEntity<ApiResponseDto<?>> registerUser(@RequestBody UserRequestDto newUser)
            throws UserAlreadyExistsException, UserServiceLogicException {
        return userService.addUser(newUser);
    }

    //   Get own profile (User, Lecturer, Admin)
    @PreAuthorize("hasAnyRole('USER','LECTURER','ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getMyProfile(Authentication authentication) throws UserNotFoundException, UserServiceLogicException {
        String email = authentication.getName();
        return userService.getUserEmail(email);
    }
}