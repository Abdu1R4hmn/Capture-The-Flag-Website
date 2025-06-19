package com.example.springboot.Progress;

import com.example.springboot.Challenge.Challenge;
import com.example.springboot.Challenge.ChallengeRepo;
import com.example.springboot.Challenge.Difficulty;
import com.example.springboot.User.User;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class ProgressService {

    private final ChallengeRepo challengeRepo;
    private final ProgressRepo progressRepo;

    public ProgressService(ChallengeRepo challengeRepo, ProgressRepo progressRepo) {
        this.challengeRepo = challengeRepo;
        this.progressRepo = progressRepo;
    }

    public List<ProgressDTO> getSolvedProgressByUser(User user) {
        List<Progress> progressList = progressRepo.findByUser(user);

        return progressList.stream()
                .map(p -> new ProgressDTO(
                        p.getChallenge().getId(),
                        p.getChallenge().getName(),
                        p.getStars()
                )).collect(Collectors.toList());
    }


    public List<ProgressDTO> getSolvedChallengesByDifficulty(User user, Difficulty diff) {
        return progressRepo.findByUser(user).stream()
                .filter(p -> p.getChallenge().getDifficulty() == diff)
                .map(p -> new ProgressDTO(
                        p.getChallenge().getId(),
                        p.getChallenge().getName(),
                        p.getStars()
                ))
                .collect(Collectors.toList());
    }
    public Map<Difficulty, DifficultyStarSummary> getStarSummaryPerDifficulty(User user) {
        // Get all challenges grouped by difficulty
        Map<Difficulty, List<Challenge>> challengesByDiff = challengeRepo.findAll().stream()
                .collect(Collectors.groupingBy(Challenge::getDifficulty));

        // Get user progress
        List<Progress> userProgress = progressRepo.findByUser(user);

        // Earned stars grouped by difficulty
        Map<Difficulty, Integer> earnedMap = userProgress.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getChallenge().getDifficulty(),
                        Collectors.summingInt(Progress::getStars)
                ));

        // Build summary per difficulty
        Map<Difficulty, DifficultyStarSummary> summary = new HashMap<>();
        for (Map.Entry<Difficulty, List<Challenge>> entry : challengesByDiff.entrySet()) {
            Difficulty difficulty = entry.getKey();
            int maxStars = entry.getValue().size() * 3; // Each challenge max 3 stars
            int earnedStars = earnedMap.getOrDefault(difficulty, 0);

            summary.put(difficulty, new DifficultyStarSummary(earnedStars, maxStars));
        }

        return summary;
    }


}
