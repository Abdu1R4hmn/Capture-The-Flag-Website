package com.example.springboot.User;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.management.relation.Role;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Size(min=3, max=20)
    private String username;

    @Column(unique = true)
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$")
    @Size(min = 8)
    private String password;

//    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime regDateAndTime;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Ensure regDateAndTime is automatically set when the entity is created
    @PrePersist
    protected void onCreate() {
        this.regDateAndTime = LocalDateTime.now();
    }

    public User(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

}
