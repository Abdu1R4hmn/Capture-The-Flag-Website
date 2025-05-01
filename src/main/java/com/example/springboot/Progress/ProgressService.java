package com.example.springboot.Progress;

import com.example.springboot.Challenge.Challenge;
import com.example.springboot.Challenge.ChallengeRepo;
import com.example.springboot.Challenge.Difficulty;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgressService {

    private final ProgressRepo progressRepo;
    private final ChallengeRepo challengeRepo;

    public ProgressService(ProgressRepo progressRepo,ChallengeRepo challengeRepo) {
        this.progressRepo = progressRepo;
        this.challengeRepo = challengeRepo;
    }

//    DTO


//    CRUD

//    GET All Solved
    public List<Challenge> getSolvedChallenges() {

        List<Challenge> challengeList =  challengeRepo.findAllByCompleted(true).stream().toList();
//        new Progress(challengeList);
        return challengeList;

    }

//  Get by Difficulty
    public List<Challenge> getSolvedDifficulty(Difficulty diff) {

        return getSolvedChallenges().stream()
                .filter(challenge -> challenge.getDifficulty() == diff)
                .collect(Collectors.toList());
    }
}
