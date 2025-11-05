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
        Map<Difficulty, List<Challenge>> challengesByDiff = challengeRepo.findAll().stream()
                .collect(Collectors.groupingBy(Challenge::getDifficulty));

        List<Progress> userProgress = progressRepo.findByUser(user);

        Map<Difficulty, Integer> earnedMap = userProgress.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getChallenge().getDifficulty(),
                        Collectors.summingInt(Progress::getStars)
                ));

        Map<Difficulty, DifficultyStarSummary> summary = new HashMap<>();

        // Ensure all difficulties are included, even if empty
        for (Difficulty difficulty : Difficulty.values()) {
            int maxStars = challengesByDiff.getOrDefault(difficulty, List.of()).size() * 3;
            int earnedStars = earnedMap.getOrDefault(difficulty, 0);
            summary.put(difficulty, new DifficultyStarSummary(earnedStars, maxStars));
        }

        return summary;
    }



}
