package com.example.springboot.Challenge;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/challenge")
public class ChallengeController {

    //    Variables
    private final ChallengeService challengeService;

    //    Constructor
    public ChallengeController(ChallengeService challengeService) {
        this.challengeService =  challengeService;
    }

//    CRUD

    //    GET challenge ALL.
    @GetMapping("/get/all")
    public List<ChallengeDTO> getChallengeAll(){
        return challengeService.getChallengeAll();
    }

//    //    GET all challenge by Difficulty.
//    @GetMapping("/get/{id}")
//    public List<ChallengeDTO> getChallengeDifficulty(){
//        return ChallengeService.getChallenge();
//    }
//
//    //    GET all challenge in Category.
//    @GetMapping("/get/type/{type}")
//    public List<ChallengeDTO> getChallengeCategory(){
//        return challengeService.getChallengeCategory();
//    }

    //    Post challenge.
    @PostMapping("/post/{catid}")
    public void postChallenge(@RequestBody ChallengeDTO challengeDto,@PathVariable("catid") long catid){
        challengeService.postChallenge(challengeDto,catid);
    }

//    //    Edit challenge
//    @PutMapping("/put/{id}")
//    public void putChallenge(@RequestBody ChallengeDTO challengeDto,@PathVariable("id") long id){
//        challengeService.putChallenge(challengeDto,id);
//    }
//
//    //    Delete challenge
//    @DeleteMapping("/delete/{id}")
//    public void deleteChallenge (@PathVariable("id") long id){
//        challengeService.deleteChallenge(id);
//    }
}


