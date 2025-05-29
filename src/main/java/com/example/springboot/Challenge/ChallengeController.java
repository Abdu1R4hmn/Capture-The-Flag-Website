package com.example.springboot.Challenge;


import com.example.springboot.Category.Category;
import com.example.springboot.Category.CategoryDTO;
import com.example.springboot.exceptions.challengeException.ChallengeAlreadyExistsException;
import com.example.springboot.exceptions.challengeException.ChallengeNotFoundException;
import com.example.springboot.responses.ApiResponseDto;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
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

    //    GET challenge ALL. ADMIN include flag
    @GetMapping("/get/all")
    public ResponseEntity<ApiResponseDto<List<ChallengeDTO>>> getChallengeAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return challengeService.getChallengeAll(page,size);
    }

    //    GET all challenge by Difficulty.
    @GetMapping("/get/difficulty/{diff}")
    public  ResponseEntity<ApiResponseDto<List<ChallengePublicDTO>>> getChallengeDifficulty(@PathVariable("diff") Difficulty diff){
        return challengeService.getChallengeDifficulty(diff);
    }

    //    GET all challenge in Category.
    @GetMapping("/get/category/{cat}")
    public ResponseEntity<ApiResponseDto<List<ChallengeDTO>>> getChallengeCategory(@PathVariable("cat") String cat) throws ChallengeNotFoundException {
        return challengeService.getChallengeCategory(cat);
    }

    //    GET by id.
    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponseDto<ChallengePublicDTO>>  getChallenge(@PathVariable("id") long id) throws ChallengeNotFoundException {
        return challengeService.getChallenge(id);
    }

    //    GET by name.
    @GetMapping("/get/name/{name}")
    public ResponseEntity<ApiResponseDto<ChallengeDTO>>  getChallengeName(@PathVariable("name") String name) throws ChallengeNotFoundException {
        return challengeService.getChallengeName(name);
    }

    //    Post challenge.
    @PostMapping("/post/{catid}")
    public ResponseEntity<ApiResponseDto<?>> postChallenge(@RequestBody ChallengeDTO challengeDto, @PathVariable("catid") long catid) throws ChallengeAlreadyExistsException, ChallengeNotFoundException {
        return challengeService.postChallenge(challengeDto,catid);
    }

    //    Edit challenge
    @PatchMapping("/put/{id}")
    public ResponseEntity<ApiResponseDto<?>> putChallenge(@RequestBody ChallengeDTO challengeDto,@PathVariable("id") long id,@RequestParam long catid) throws ChallengeNotFoundException, ChallengeAlreadyExistsException {
        return challengeService.putChallenge(challengeDto,id, catid);
    }

    //    Delete challenge
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponseDto<?>> deleteChallenge (@PathVariable("id") long id) throws ChallengeNotFoundException {
        return challengeService.deleteChallenge(id);
    }

    //    GET TOTAL NUMBER OF challenges
    @GetMapping(path = "total")
    public long totalChallenges() {
        return challengeService.totalChallenges();
    }
//    Game Logic

    //    Submit Flag
    @PostMapping("/solve/{challengeId}")
    public void solveChallenge(@PathVariable long challengeId, @RequestBody String submittedFlag){}


}


