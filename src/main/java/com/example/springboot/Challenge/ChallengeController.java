package com.example.springboot.Challenge;


import com.example.springboot.Category.Category;
import jakarta.websocket.server.PathParam;
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

    //    GET all challenge by Difficulty.
    @GetMapping("/get/difficulty/{diff}")
    public List<ChallengeDTO> getChallengeDifficulty(@PathVariable("diff") Difficulty diff){
        return challengeService.getChallengeDifficulty(diff);
    }

    //    GET all challenge in Category.
    @GetMapping("/get/category/{cat}")
    public List<ChallengeDTO> getChallengeCategory(@PathVariable("cat") Category cat){
        return challengeService.getChallengeCategory(cat);
    }

    //    GET by id.
    @GetMapping("/get/{id}")
    public ChallengeDTO getChallenge(@PathVariable("id") long id){
        return challengeService.getChallenge(id);
    }

    //    Post challenge.
    @PostMapping("/post/{catid}")
    public void postChallenge(@RequestBody ChallengeDTO challengeDto,@PathVariable("catid") long catid){
        challengeService.postChallenge(challengeDto,catid);
    }

    //    Edit challenge
    @PutMapping("/put/{id}")
    public void putChallenge(@RequestBody ChallengeDTO challengeDto,@PathVariable("id") long id){
        challengeService.putChallenge(challengeDto,id);
    }

    //    Delete challenge
    @DeleteMapping("/delete/{id}")
    public void deleteChallenge (@PathVariable("id") long id){
        challengeService.deleteChallenge(id);
    }
}


