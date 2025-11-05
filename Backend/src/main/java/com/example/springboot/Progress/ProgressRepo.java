package com.example.springboot.Progress;

import com.example.springboot.Challenge.Challenge;
import com.example.springboot.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgressRepo extends JpaRepository<Progress, Long> {
    boolean existsByUserAndChallenge(User user, Challenge challenge);
    List<Progress> findByUser(User user);
    Optional<Progress> findByUserAndChallenge(User user, Challenge challenge);
}
