package com.example.springboot.Challenge;

import com.example.springboot.Category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepo extends JpaRepository<Challenge, Long> {
    Challenge findByCategory(Category category);
    Challenge findByDifficulty(Difficulty difficulty);
    Challenge findByName(String name);
}
