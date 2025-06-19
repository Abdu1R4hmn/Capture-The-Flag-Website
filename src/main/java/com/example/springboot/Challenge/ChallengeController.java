package com.example.springboot.Challenge;

import com.example.springboot.Progress.ProgressDTO;
import com.example.springboot.User.User;
import com.example.springboot.User.UserService;
import com.example.springboot.exceptions.challengeException.ChallengeAlreadyExistsException;
import com.example.springboot.exceptions.challengeException.ChallengeNotFoundException;
import com.example.springboot.exceptions.userException.UserNotFoundException;
import com.example.springboot.exceptions.userException.UserServiceLogicException;
import com.example.springboot.responses.ApiResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("api/challenge")
public class ChallengeController {

    //    Variables
    private final ChallengeService challengeService;
    private final UserService userService;

    //    Constructor
    public ChallengeController(ChallengeService challengeService, UserService userService) {
        this.challengeService =  challengeService;
        this.userService = userService;
    }


//    CRUD

    //    GET challenge ALL. ADMIN include flag
    @GetMapping("/get/all")
    public ResponseEntity<ApiResponseDto<List<ChallengeDTO>>> getChallengeAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return challengeService.getChallengeAll(page,size);
    }
    //    GET challenge ALL. ADMIN include flag
    @GetMapping("/get/public/all")
    public ResponseEntity<ApiResponseDto<List<ChallengePublicDTO>>> getChallengePublicAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return challengeService.getChallengePublicAll(page,size);
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

    //    GET all challenge in Category.
    @GetMapping("/get/category/public/{cat}")
    public ResponseEntity<ApiResponseDto<List<ChallengePublicDTO>>> getChallengePublicCategory(@PathVariable("cat") String cat) throws ChallengeNotFoundException {
        return challengeService.getChallengePublicCategory(cat);
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
    public ResponseEntity<ApiResponseDto<?>> solveChallenge(
            @PathVariable Long challengeId,
            @RequestBody ChallengeSolveRequestDTO dto,
            @RequestParam String email) throws UserNotFoundException, UserServiceLogicException, ChallengeNotFoundException {

        User user = userService.getUserByEmail(email);
        return challengeService.solveChallenge(challengeId, dto, user);
    }





}


