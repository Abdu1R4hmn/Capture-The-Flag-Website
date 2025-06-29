package com.example.springboot.Challenge;

import com.example.springboot.Category.Category;
import com.example.springboot.Feedback.Feedback;
import com.example.springboot.Progress.Progress;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Challenge {

//    Variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Challenge name is required.")
    @Size(max = 100, message = "Challenge name must not exceed 100 characters.")
    private String name;

    @NotBlank(message = "Description is required.")
    @Lob
    private String description;

    @NotNull(message = "Difficulty must be specified.")
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @NotBlank(message = "Flag is required.")
    private String flag;

    @NotBlank(message = "Hints must be provided.")
    private String hint1;

    @NotBlank(message = "Hints must be provided.")
    private String hint2;

    @NotBlank(message = "Solution must be provided.")
    @Lob
    @Column(columnDefinition = "TEXT")
    private String solution;

    @Lob
    @Column(name = "ChallengeImage")
    private byte[] challengeImage;

//    Relationships
    @JsonIgnore
    @OneToMany(mappedBy = "challenge", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Progress> progresses = new ArrayList<>();

    @ManyToOne
    private Category category;


    @OneToMany(mappedBy = "challenge", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedback;


    public Challenge(String name, String description, Difficulty difficulty, String flag, byte[] challengeImage, Category category, String hint1, String hint2, String solution) {
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.flag = flag;
        this.challengeImage = challengeImage;
        this.category = category;
        this.hint1 = hint1;
        this.hint2 = hint2;
        this.solution = solution;
    }

}
