package com.example.springboot.Challenge;

import com.example.springboot.Category.Category;
import com.example.springboot.Hints.Hints;
import com.example.springboot.Progress.Progress;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Challenge {

//    Variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    private String flag;

    private boolean completed;

    private int stars;

    @Lob
    @Column(name = "ChallengeImage")
    private byte[] challengeImage;

//    Relationships

    @ManyToOne
    private Category category;

    @ManyToOne
//    @JsonIgnore
    private Progress progress;

    @OneToOne
    private Hints hints;

//    Constructor
    public Challenge(String name, String description, Difficulty difficulty, String flag, boolean completed,int stars, byte[] challengeImage, Category category) {
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.flag = flag;
        this.challengeImage = challengeImage;
        this.category = category;
    }

}
