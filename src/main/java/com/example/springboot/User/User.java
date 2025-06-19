package com.example.springboot.User;


import com.example.springboot.Progress.Progress;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


import com.example.springboot.Role.Role;
import java.time.LocalDateTime;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Progress> progressList;

    private LocalDateTime regDateAndTime;


    public User(String username, String email, String password, Role role, List<Progress> progressList) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.progressList = progressList;
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
        this.role = Role.USER;
    }


    // Ensure regDateAndTime is automatically set when the entity is created
    @PrePersist
    protected void onCreate() {
        this.regDateAndTime = LocalDateTime.now();
    }
}
