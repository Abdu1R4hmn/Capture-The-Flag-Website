package com.example.springboot.Challenge;

import com.example.springboot.Category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRepo extends JpaRepository<Challenge, Long> {
    List<Challenge> findAllByCategory(Category category);
    List<Challenge> findAllByDifficulty(Difficulty difficulty);
    Challenge findByName(String name);
    List<Challenge> findAllByCompleted(Boolean completed);
}
