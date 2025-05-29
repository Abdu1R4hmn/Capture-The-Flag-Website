package com.example.springboot.User;


import com.example.springboot.exceptions.userException.UserAlreadyExistsException;
import com.example.springboot.exceptions.userException.UserNotFoundException;
import com.example.springboot.exceptions.userException.UserServiceLogicException;
import com.example.springboot.responses.ApiResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
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
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getAllUsersData(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) throws UserServiceLogicException {
        return userService.getAllUsers(page, size);
    }

    //  GET a specific User by id.
    @GetMapping(path = "get/{id}")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getUser(@PathVariable("id")long id) throws UserNotFoundException, UserServiceLogicException {
        return userService.getUser(id);
    }

    //  GET a specific User by Email.
    @GetMapping(path = "get/email/{email}")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getUserEmail(@PathVariable("email")String email) throws UserNotFoundException, UserServiceLogicException {
        return userService.getUser(email);
    }

    //   POST/Add User.
    @PostMapping(path = "post")
    public ResponseEntity<ApiResponseDto<?>> addUser (@Valid @RequestBody UserRequestDto user) throws UserAlreadyExistsException, UserServiceLogicException {
        return userService.addUser(user);
    }

    //   Edit User.
    @PatchMapping(path = "edit/{id}")
    public ResponseEntity<ApiResponseDto<?>> updateUser(@Valid @RequestBody UserEditDto user,@PathVariable("id")long id) throws UserAlreadyExistsException, UserNotFoundException, UserServiceLogicException {
        return userService.updateUser(user, id);
    }

    //   Delete User.
    @DeleteMapping(path = "delete/{id}")
    public ResponseEntity<ApiResponseDto<?>> deleteUser(@PathVariable("id")long id)throws UserNotFoundException, UserServiceLogicException{
        return userService.deleteUser(id);
    }

//    GET TOTAL NUMBER OF USERS
    @GetMapping(path = "total")
    public long totalUsers(){
        return userService.totalUsers();
    }
}