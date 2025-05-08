package com.example.springboot.User;

import com.example.springboot.Progress.Progress;
import com.example.springboot.exceptions.UserAlreadyExistsException;
import com.example.springboot.exceptions.UserNotFoundException;
import com.example.springboot.exceptions.UserServiceLogicException;
import com.example.springboot.responses.ApiResponseDto;
import com.example.springboot.responses.ApiResponseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.awt.print.Pageable;
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
        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getRegDateAndTime(),user.getProgress());
    }

//  CRUD

    //  GETs All Users Data (for Admin).
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getAllUsers(int page, int size) throws UserServiceLogicException {

        try {
//            Pagination
            Page<User> usersPage = userRepo.findAll(PageRequest.of(page, size, Sort.by("id").descending()));

//        Get all users
            List<User> users = userRepo.findAll();

//        Convert users to Dtos
            List<UserResponseDto> userDtos = usersPage.getContent().stream().map(this::convertToDto).toList();

//        wrap the Dtos in the ApiResponseDto
            ApiResponseDto<List<UserResponseDto>> wrappedDtos = new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), userDtos, usersPage.getTotalPages());

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

//            Attaching Progress for new user.
            Progress progress = new Progress();

            User user = new User(newUser.getUsername(), newUser.getEmail(), newUser.getPassword(), newUser.getRole(), progress);

//            Saving Progress for new user.
            user.setProgress(progress);

            userRepo.save(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), "New user account has been successfully created!"));

        } catch (UserAlreadyExistsException e) {
            throw new UserAlreadyExistsException(e.getMessage());
        } catch (Exception e) {
            throw new UserServiceLogicException();
        }

    }

    //   EDIT/Put User.
    public ResponseEntity<ApiResponseDto<?>> updateUser(UserEditDto user, long id) throws UserAlreadyExistsException, UserNotFoundException, UserServiceLogicException {
        try {

            User existingUser = userRepo.findByEmail(user.getEmail());
            if (existingUser != null && existingUser.getId() != id) {
                throw new UserAlreadyExistsException("Update failed: Another user already exists with email " + user.getEmail());
            }

            User orginalUser = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id " + id));

            if(user.getUsername() != null) {
                orginalUser.setUsername(user.getUsername());
            }
            if(user.getEmail() != null) {
                orginalUser.setEmail(user.getEmail());
            }
            if (user.getPassword() != null) {
                orginalUser.setPassword(user.getPassword());
            }

            if (user.getRole() != null) {
                orginalUser.setRole(user.getRole());
            }

            userRepo.save(orginalUser);

            return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), "User account updated successfully!"));

        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (UserAlreadyExistsException e) {
            throw new UserAlreadyExistsException(e.getMessage());
        }catch (Exception e) {
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

//      GET TOTAL NUMBER OF USERS
    public long totalUsers() {
        return userRepo.findAll().stream().count();
    }
}
