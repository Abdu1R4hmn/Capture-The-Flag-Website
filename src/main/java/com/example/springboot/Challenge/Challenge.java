package com.example.springboot.Challenge;

import com.example.springboot.Category.Category;
import com.example.springboot.Hints.Hints;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    private String flag;

    @Lob
    @Column(name = "ChallengeImage")
    private byte[] challengeImage;

    @ManyToOne
    private Category category;

    @OneToOne
    private Hints hints;

//    Constructor
    public Challenge(String name, String description, Difficulty difficulty, String flag, byte[] challengeImage, Category category) {
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.flag = flag;
        this.challengeImage = challengeImage;
        this.category = category;
    }
}
