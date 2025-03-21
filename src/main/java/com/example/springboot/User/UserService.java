package com.example.springboot.User;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    //  Variables
    private final UserRepo userRepo;

    //  Constructor
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    //   DTO
    private UserDTO entityToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());

        return userDTO;
    }

    //   Check Edits
    public void checkEdit(User user, User theUser) {
//        Username can't be the same when editing, but can be NULL aka unchanged.
        if (!Objects.equals(user.getUsername(), theUser.getUsername())) {
            theUser.setUsername(user.getUsername());
        } else {
            throw new IllegalStateException("Username is the same or empty, please use a different name");
        }

        if (user.getEmail() != null && !Objects.equals(user.getEmail(), theUser.getEmail())) {
            theUser.setEmail(user.getEmail());
        } else {
            throw new IllegalStateException("Email is the same or empty, please use a different Email");
        }

        if (user.getPassword() != null && !Objects.equals(user.getPassword(), theUser.getPassword())) {
            theUser.setPassword(user.getPassword());
        } else {
            throw new IllegalStateException("Password is the same or empty, please use a different password");
        }

        if (!Objects.equals(user.getRole(), theUser.getRole())) {
            theUser.setRole(user.getRole());
        } else {
            throw new IllegalStateException("Role is the same or empty, please use a different Role");
        }
    }

//  CRUD

    //  GETs All Users Data (for Admin).
    public List<User> getAllUsersData() {
        return userRepo.findAll();
    }

    //  GET a specific User by id.
    public List<UserDTO> getUser(long id) {
        Optional<User> user = userRepo.findById(id);
        return user.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    //   POST/Add User.
    public User addUser(User user) {
        return userRepo.save(user);
    }

    //   EDIT/Put User.
    public void editUser(User user, long id) {
        User theUser = Optional.ofNullable(userRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("User Not Found"))).orElse(null);
//                .orElseThrow(
//                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(NOT_FOUND,id)));

        checkEdit(user, theUser);
        userRepo.save(theUser);
    }

    //   Delete User.
    public void deleteUser(long id) {
        userRepo.deleteById(id);
    }
}
