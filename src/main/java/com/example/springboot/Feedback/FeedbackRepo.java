package com.example.springboot.Feedback;

import com.example.springboot.Challenge.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepo extends JpaRepository<Feedback ,Long> {
    List<Feedback> findByChallenge(Challenge challenge);
}
