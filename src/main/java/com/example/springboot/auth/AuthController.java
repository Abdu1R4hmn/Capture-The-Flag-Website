package com.example.springboot.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    // Handles user login requests
    @PostMapping("/api/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        
        // Authenticate the user using email and password
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        // Set the authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Ensure a session is created
        request.getSession(true);

        // Store the security context in the session for future requests
        HttpSession session = request.getSession();
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        // Return a success response
        return ResponseEntity.ok().body(Map.of("status", "SUCCESS"));
    }
}

/**
 * DTO for login requests.
 */
class LoginRequest {
    private String email;
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}