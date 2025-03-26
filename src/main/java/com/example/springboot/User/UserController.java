package com.example.springboot.User;

import com.example.springboot.Exceptions.UserAlreadyExistsException;
import com.example.springboot.Exceptions.UserNotFoundException;
import com.example.springboot.Exceptions.UserServiceLogicException;
import com.example.springboot.Responses.ApiResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping(path = "get/all")
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getAllUsersData() throws UserServiceLogicException {
        return userService.getAllUsers();
    }

//  GET a specific User by id.
    @GetMapping(path = "get/{id}")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getUser(@PathVariable("id")long id) throws UserNotFoundException, UserServiceLogicException {
        return userService.getUser(id);
    }

//   POST/Add User.
    @PostMapping(path = "add")
    public ResponseEntity<ApiResponseDto<?>> addUser (@Valid @RequestBody UserRequestDto user) throws UserAlreadyExistsException, UserServiceLogicException {
        return userService.addUser(user);
    }

//   Edit User.
    @PutMapping(path = "edit/{id}")
    public ResponseEntity<ApiResponseDto<?>> updateUser(@RequestBody UserRequestDto user,@PathVariable("id")long id) throws UserNotFoundException, UserServiceLogicException {
        return userService.updateUser(user, id);
    }

//   Delete User.
    @DeleteMapping(path = "delete/{id}")
    public ResponseEntity<ApiResponseDto<?>> deleteUser(@PathVariable("id")long id)throws UserNotFoundException, UserServiceLogicException{
        return userService.deleteUser(id);
    }
}