package com.example.springboot.Challenge;

import com.example.springboot.Category.Category;
import com.example.springboot.Feedback.Feedback;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChallengeDTO {

    private Long id;

    @NotBlank(message = "Challenge name is required.")
    @Size(max = 100, message = "Challenge name must not exceed 100 characters.")
    private String name;

    @NotBlank(message = "Description is required.")
    @Lob
    private String description;

    // @NotBlank(message = "Solution must be provided.")
    private String solution;

    @Lob
    @Column(name = "ChallengeImage")
    private byte[] challengeImage;

    @NotNull(message = "Difficulty must be specified.")
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @NotBlank(message = "Flag is required.")
    private String flag;

    private List<Feedback> feedback;

    @ManyToOne
    private Category category;

    @NotBlank(message = "Hints must be provided.")
    private String hint1;

    @NotBlank(message = "Hints must be provided.")
    private String hint2;


}
