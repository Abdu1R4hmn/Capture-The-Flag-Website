package com.example.springboot.Challenge;

import com.example.springboot.Category.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class ChallengePublicDTO {
    private long id;

    private String name;

    private String description;

    private Difficulty difficulty;

    private byte[] challengeImage;

    private Category category;

    private String hint1;

    private String hint2;
}
