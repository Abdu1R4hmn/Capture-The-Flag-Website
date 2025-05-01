package com.example.springboot.Progress;

import com.example.springboot.Challenge.Challenge;
import com.example.springboot.Challenge.ChallengeDTO;
import com.example.springboot.Challenge.Difficulty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/progress")
public class ProgressController {

    private final ProgressService progressService;

//    Constructor
    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

//    CRUD

//    GET
    @GetMapping("/get")
    public List<Challenge> getSolvedChallenges(){
        return progressService.getSolvedChallenges();
    }

//    GET Stars
    @GetMapping("/get/{diff}")
    public List<Challenge> getSolvedDifficulty(@PathVariable("diff") Difficulty diff){
        return progressService.getSolvedDifficulty(diff);
    }


}
