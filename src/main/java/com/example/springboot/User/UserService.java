package com.example.springboot.User;

import com.example.springboot.Progress.Progress;
import com.example.springboot.Role.Role;
import com.example.springboot.exceptions.userException.UserAlreadyExistsException;
import com.example.springboot.exceptions.userException.UserNotFoundException;
import com.example.springboot.exceptions.userException.UserServiceLogicException;
import com.example.springboot.responses.ApiResponseDto;
import com.example.springboot.responses.ApiResponseStatus;
import com.example.springboot.util.EmailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


@Service
public class UserService {
    //  Variables
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder; // Add this
    private final EmailService emailService; // Add this

    //  Constructor
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder; // Add this
        this.emailService = emailService; // Add this
    }

    //   DTO
    private UserResponseDto convertToDto(User user) {
        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getRegDateAndTime(), user.getProgressList());
    }

//  CRUD

    //  GETs All Users Data (for Admin).
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getAllUsers(int page, int size) throws UserServiceLogicException {

        try {
//            Pagination
            Page<User> usersPage = userRepo.findAll(PageRequest.of(page, size, Sort.by("id").descending()));


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
            e.printStackTrace(); // Add this line for debugging
            throw new UserServiceLogicException();
        }
    }

    //  GET a specific User by Email.
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getUserEmail(String email) throws UserNotFoundException, UserServiceLogicException {
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
            e.printStackTrace(); // Add this line for debugging
            throw new UserServiceLogicException();
        }
    }

    //    RAW
    public User getUserByEmail(String email) throws UserNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found with email: " + email);
        }
        return user;
    }

    public ResponseEntity<ApiResponseDto<?>> editProfile(UserProfileEditDto dto, String email) throws UserNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user == null) throw new UserNotFoundException("User not found");

        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());

        if (dto.getOldPassword() != null && dto.getNewPassword() != null) {
            // Use passwordEncoder to check old password
            if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
                throw new RuntimeException("Old password does not match");
            }
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }

        userRepo.save(user);
        return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), "Profile updated successfully"));
    }


    //   POST/Add User.
    public ResponseEntity<ApiResponseDto<?>> addUser(UserRequestDto newUser) throws UserAlreadyExistsException, UserServiceLogicException {
        try {
            if (userRepo.findByEmail(newUser.getEmail()) != null) {
                throw new UserAlreadyExistsException("Registration failed: User already exists with email " + newUser.getEmail());
            }

            // Hash the password before saving
            String hashedPassword = passwordEncoder.encode(newUser.getPassword());

            // Use the role from the DTO, default to ROLE_USER if null
            Role role = newUser.getRole() != null ? newUser.getRole() : Role.ROLE_USER;

            User user = new User(
                newUser.getUsername(),
                newUser.getEmail(),
                hashedPassword,
                role
            );

            userRepo.save(user);

            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), "New user account has been successfully created!"));

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
                throw new UserAlreadyExistsException("User already exists with email " + user.getEmail());
            }

            User orginalUser = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id " + id));

            if(user.getUsername() != null) {
                orginalUser.setUsername(user.getUsername());
            }
            if(user.getEmail() != null) {
                orginalUser.setEmail(user.getEmail());
            }
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                orginalUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            if (user.getRole() != null) {
                orginalUser.setRole(user.getRole());
            }

            userRepo.save(orginalUser);

            return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), "User account updated successfully!"));

        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (Exception e) {
            // Pass the exception message to UserServiceLogicException
            throw new UserServiceLogicException(e.getMessage() != null ? e.getMessage() : "Something went wrong. Please try again later!");
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
            // Pass the exception message to UserServiceLogicException
            throw new UserServiceLogicException(e.getMessage() != null ? e.getMessage() : "Something went wrong. Please try again later!");
        }

    }

    public ResponseEntity<ApiResponseDto<?>> deleteUserByEmail(String email) throws UserNotFoundException, UserServiceLogicException {
        User user = userRepo.findByEmail(email);
        if (user == null) throw new UserNotFoundException("User not found with email: " + email);

        userRepo.delete(user);
        return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), "User account deleted successfully!"));
    }

//      GET TOTAL NUMBER OF USERS
    public long totalUsers() {
        return userRepo.findAll().stream().count();
    }

    public void resetPasswordAndSendEmail(String email) throws UserNotFoundException {
        Optional<User> userOpt = userRepo.findByEmailIgnoreCase(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String newPassword = generateRandomPassword(8);
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);
            String subject = "Your new password";
            String message = "Your new password is: " + newPassword + "\nPlease change it after logging in.";
            System.out.println("Sending email to: " + user.getEmail());
            emailService.sendSimpleMessage(user.getEmail(), subject, message);
        } else {
            throw new UserNotFoundException("User doesn't exist");
        }
    }

    private String generateRandomPassword(int length) {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String all = upper + lower + digits;
        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        // Ensure at least one of each
        sb.append(upper.charAt(random.nextInt(upper.length())));
        sb.append(lower.charAt(random.nextInt(lower.length())));
        sb.append(digits.charAt(random.nextInt(digits.length())));

        for (int i = 3; i < length; i++) {
            sb.append(all.charAt(random.nextInt(all.length())));
        }
        // Shuffle
        List<Character> pwdChars = sb.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        Collections.shuffle(pwdChars, random);
        StringBuilder password = new StringBuilder();
        pwdChars.forEach(password::append);
        return password.toString();
    }
}
