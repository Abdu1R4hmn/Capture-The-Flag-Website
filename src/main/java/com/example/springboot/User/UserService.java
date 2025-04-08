package com.example.springboot.User;

import com.example.springboot.Exceptions.UserAlreadyExistsException;
import com.example.springboot.Exceptions.UserNotFoundException;
import com.example.springboot.Exceptions.UserServiceLogicException;
import com.example.springboot.Responses.ApiResponseDto;
import com.example.springboot.Responses.ApiResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import com.example.springboot.Role.Role;
import java.util.List;


@Service
public class UserService {

    //  Variables
    private final UserRepo userRepo;

    //  Constructor
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    //   DTO
    private UserResponseDto convertToDto(User user) {
        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getRegDateAndTime());
    }

//  CRUD

    //  GETs All Users Data (for Admin).
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getAllUsers() throws UserServiceLogicException {

        try {
//        Get all users
            List<User> users = userRepo.findAll();

//        Convert users to Dtos
            List<UserResponseDto> userDtos = users.stream().map(this::convertToDto).toList();

//        wrap the Dtos in the ApiResponseDto
            ApiResponseDto<List<UserResponseDto>> wrappedDtos = new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), userDtos);

            return ResponseEntity.ok(wrappedDtos);

        } catch (Exception e) {
            throw new UserServiceLogicException();
        }
    }

    //  GET a specific User by id.
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getUser(long id) throws UserNotFoundException, UserServiceLogicException {
        try {

//        Get the user
            User user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User Not Found With The Id " + id));

//        Convert user to Dto
            UserResponseDto userDto = convertToDto(user);

//        wrap the Dto in the ApiResponseDto
            ApiResponseDto<UserResponseDto> wrappedDto = new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), userDto);

            return ResponseEntity.ok(wrappedDto);

        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new UserServiceLogicException();
        }
    }

    //  GET a specific User by Email.
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getUser(String email) throws UserNotFoundException, UserServiceLogicException {
        try {

            if (userRepo.findByEmail(email) == null) {
                throw new UserNotFoundException("User Not Found With The Id " + email);
            }

//        Get the user
            User user = userRepo.findByEmail(email);

//        Convert user to Dto
            UserResponseDto userDto = convertToDto(user);

//        wrap the Dto in the ApiResponseDto
            ApiResponseDto<UserResponseDto> wrappedDto = new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), userDto);

            return ResponseEntity.ok(wrappedDto);

        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new UserServiceLogicException();
        }
    }

    //   POST/Add User.
    public ResponseEntity<ApiResponseDto<?>> addUser(UserRequestDto newUser) throws UserAlreadyExistsException, UserServiceLogicException {
        try {

            if (userRepo.findByEmail(newUser.getEmail()) != null) {
                throw new UserAlreadyExistsException("Registration failed: User already exists with email " + newUser.getEmail());
            }

            User user = new User(newUser.getUsername(), newUser.getEmail(), newUser.getPassword());

            userRepo.save(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), "New user account has been successfully created!"));

        } catch (UserAlreadyExistsException e) {
            throw new UserAlreadyExistsException(e.getMessage());
        } catch (Exception e) {
            throw new UserServiceLogicException();
        }

    }

    //   EDIT/Put User.
    public ResponseEntity<ApiResponseDto<?>> updateUser(UserRequestDto user, long id) throws UserNotFoundException, UserServiceLogicException {
        try {

            User orginalUser = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id " + id));

            orginalUser.setUsername(user.getUsername());
            orginalUser.setEmail(user.getEmail());
            orginalUser.setPassword(user.getPassword());
            orginalUser.setRole(user.getRole());

            userRepo.save(orginalUser);

            return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), "User account updated successfully!"));

        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new UserServiceLogicException();
        }
    }

    //   Delete User.
    public ResponseEntity<ApiResponseDto<?>> deleteUser(long id) throws UserNotFoundException, UserServiceLogicException {

        try {

            User orginalUser = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id " + id));

            userRepo.deleteById(id);

            return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), "User account deleted successfully!"));

        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new UserServiceLogicException();
        }

    }
}
