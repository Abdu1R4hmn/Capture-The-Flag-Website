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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
    @PreAuthorize("hasAnyRole('LECTURER','ADMIN')")
    @GetMapping("/get/all")
    public ResponseEntity<ApiResponseDto<List<ChallengeDTO>>> getChallengeAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return challengeService.getChallengeAll(page,size);
    }

    //    GET challenge ALL. Public version
    @PreAuthorize("hasAnyRole('USER','LECTURER','ADMIN')")
    @GetMapping("/get/public/all")
    public ResponseEntity<ApiResponseDto<List<ChallengePublicDTO>>> getChallengePublicAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return challengeService.getChallengePublicAll(page,size);
    }

    //    GET all challenge by Difficulty. (Public)
    @GetMapping("/get/difficulty/{diff}")
    public  ResponseEntity<ApiResponseDto<List<ChallengePublicDTO>>> getChallengeDifficulty(@PathVariable("diff") Difficulty diff){
        return challengeService.getChallengeDifficulty(diff);
    }

    //    GET all challenge in Category. (Lecturer, Admin)
    @PreAuthorize("hasAnyRole('LECTURER','ADMIN')")
    @GetMapping("/get/category/{cat}")
    public ResponseEntity<ApiResponseDto<List<ChallengeDTO>>> getChallengeCategory(@PathVariable("cat") String cat) throws ChallengeNotFoundException {
        return challengeService.getChallengeCategory(cat);
    }

    //    GET all challenge in Category. (Public)
    @GetMapping("/get/category/public/{cat}")
    public ResponseEntity<ApiResponseDto<List<ChallengePublicDTO>>> getChallengePublicCategory(@PathVariable("cat") String cat) throws ChallengeNotFoundException {
        return challengeService.getChallengePublicCategory(cat);
    }

    //    GET by id. (Public)
    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponseDto<ChallengePublicDTO>>  getChallenge(@PathVariable("id") long id) throws ChallengeNotFoundException {
        return challengeService.getChallenge(id);
    }

    //    GET by name. (Lecturer, Admin)
    @PreAuthorize("hasAnyRole('LECTURER','ADMIN', 'USER')")
    @GetMapping("/get/name/{name}")
    public ResponseEntity<ApiResponseDto<ChallengeDTO>>  getChallengeName(@PathVariable("name") String name) throws ChallengeNotFoundException {
        return challengeService.getChallengeName(name);
    }

    //    Post challenge. (Lecturer)
    @PreAuthorize("hasAnyRole('LECTURER','ADMIN')")
    @PostMapping("/post/{catid}")
    public ResponseEntity<ApiResponseDto<?>> postChallenge(@RequestBody ChallengeDTO challengeDto,
    @PathVariable("catid") long catid) throws ChallengeAlreadyExistsException, ChallengeNotFoundException {
        return challengeService.postChallenge(challengeDto,catid);
    }

    //    Edit challenge (Lecturer, Admin)
    @PreAuthorize("hasAnyRole('LECTURER','ADMIN')")
    @PatchMapping("/put/{id}")
    public ResponseEntity<ApiResponseDto<?>> putChallenge(@RequestBody ChallengeDTO challengeDto,@PathVariable("id") long id,
    @RequestParam long catid) throws ChallengeNotFoundException, ChallengeAlreadyExistsException {
        return challengeService.putChallenge(challengeDto,id, catid);
    }

    //    Delete challenge (Lecturer, Admin)
    @PreAuthorize("hasAnyRole('LECTURER','ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponseDto<?>> deleteChallenge (@PathVariable("id") long id) throws ChallengeNotFoundException {
        return challengeService.deleteChallenge(id);
    }

    //    GET TOTAL NUMBER OF challenges (Lecturer, Admin)
    @PreAuthorize("hasAnyRole('LECTURER','ADMIN')")
    @GetMapping(path = "total")
    public long totalChallenges() {
        return challengeService.totalChallenges();
    }

    //    Game Logic

    //    Submit Flag (User, Lecturer, Admin)
    @PreAuthorize("hasAnyRole('USER','LECTURER','ADMIN')")
    @PostMapping("/solve/{challengeId}")
    public ResponseEntity<ApiResponseDto<?>> solveChallenge(
            @PathVariable Long challengeId,
            @RequestBody ChallengeSolveRequestDTO dto,
            Authentication authentication) throws UserNotFoundException, UserServiceLogicException, ChallengeNotFoundException {

        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        return challengeService.solveChallenge(challengeId, dto, user);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<?> uploadChallengeImage(@PathVariable Long id, @RequestParam("image") MultipartFile image) {
        try {
            String msg = challengeService.uploadChallengeImage(id, image);
            return ResponseEntity.ok(msg);
        } catch (ChallengeNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Image upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getChallengeImage(@PathVariable Long id) {
        try {
            byte[] image = challengeService.getChallengeImage(id);
            if (image == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok()
                .header("Content-Type", "image/jpeg")
                .body(image);
        } catch (ChallengeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}


