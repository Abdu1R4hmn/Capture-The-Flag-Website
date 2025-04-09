package com.example.springboot.Challenge;

import com.example.springboot.Category.Category;
import com.example.springboot.Category.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChallengeDTO {
    private String name;

    private String description;

    private Difficulty difficulty;

    private String flag;

    private byte[] challengeImage;

    private Category category;
}
