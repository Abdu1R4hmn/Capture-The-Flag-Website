package com.example.springboot.User;

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
    @GetMapping
    public List<User> getAllUsersData(){
        return userService.getAllUsersData();
    }

//  GET a specific User by id.
    @GetMapping(path = "/{id}")
    public List<UserDTO> getUser(@PathVariable("id")long id){
        return userService.getUser(id);
    }

//   POST/Add User.
    @PostMapping
    public User addUser(@RequestBody User user){
        return userService.addUser(user);
    }

//   Edit User.
    @PutMapping(path = "edit/{id}")
    public void editUser(@RequestBody User user,@PathVariable("id")long id){
        userService.editUser(user, id);
    }

//   Delete User.
    @DeleteMapping(path = "/delete/{id}")
    public void deleteUser(@PathVariable("id")long id){
        userService.deleteUser(id);
    }
}