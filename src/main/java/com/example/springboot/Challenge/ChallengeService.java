package com.example.springboot.Challenge;

import com.example.springboot.Category.Category;
import com.example.springboot.Category.CategoryDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import com.example.springboot.Category.CategoryRepo;
import com.example.springboot.Challenge.Challenge;
import com.example.springboot.Challenge.ChallengeDTO;


import java.util.List;

@Service
public class ChallengeService {

//    Variables
    private final ChallengeRepo challengeRepo;
    private final CategoryRepo categoryRepo;

//    Constructor
    public ChallengeService(ChallengeRepo challengeRepo, CategoryRepo categoryRepo){
        this.challengeRepo = challengeRepo;
        this.categoryRepo = categoryRepo;
    }

//    DTOs
    public ChallengeDTO convertToDto(Challenge challenge){
        return new ChallengeDTO(challenge.getName(),challenge.getDescription(),challenge.getDifficulty(),challenge.getFlag(),challenge.isCompleted(), challenge.getStars(), challenge.getChallengeImage(),challenge.getCategory(),challenge.getHints());
    }
    public ChallengePublicDTO convertToPublicDto(Challenge challenge){
        return new ChallengePublicDTO(challenge.getName(),challenge.getDescription(),challenge.getDifficulty(),challenge.isCompleted(), challenge.getStars(), challenge.getChallengeImage(),challenge.getCategory(),challenge.getHints());
    }

    public Challenge convertToEntity(ChallengeDTO challengeDto){
        return new Challenge(challengeDto.getName(),challengeDto.getDescription(),challengeDto.getDifficulty(),challengeDto.getFlag(), challengeDto.isCompleted(), challengeDto.getStars(), challengeDto.getChallengeImage(),challengeDto.getCategory());
    }

//Functions

    //    Challenge completed
    public void isSolved(Challenge challenge, String flag){

//        Compare the submitted Flag to the Challenge's flag.
//        If they match then the challenge is completed succesfully.
        if(flag == challenge.getFlag()){
            challenge.setCompleted(true);
        }

//        Check how many hints are used.
//        Assign the challenge stars based on how many hints used.
        if(challenge.getHints() != null){
            challenge.setStars(2);
        } else if (challenge.getHints() == null) {
            challenge.setStars(1);
        }else {
            challenge.setStars(3);
        }

//        Append completed challenge to list of users challenges
    };

//    CRUD

    //    GET All
    public List<ChallengePublicDTO> getChallengeAll() {
        List<ChallengePublicDTO> challenges = challengeRepo.findAll().stream().map(this::convertToPublicDto).toList();
        return challenges;
    }

    //    GET Difficulty
    public List<ChallengePublicDTO> getChallengeDifficulty(Difficulty diff) {

        List<Challenge> challenges = challengeRepo.findAllByDifficulty(diff);

        return challenges.stream().map(this::convertToPublicDto).toList();
    }

    //    GET Category
    public List<ChallengePublicDTO> getChallengeCategory(Category category) {

        List<Challenge> challenges = challengeRepo.findAllByCategory(category);

        return challenges.stream().map(this::convertToPublicDto).toList();
    }

    //    Get Id
    public ChallengePublicDTO getChallenge(long id) {
        Challenge challenge = challengeRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("challenge not found"));

        ChallengePublicDTO challengePublicDTO = convertToPublicDto(challenge);

        return challengePublicDTO;
    }

    //    Post :: How to make it submit hints with the challenges together?
    public void postChallenge(ChallengeDTO challengeDto, long catid) {

        if (challengeRepo.findByName(challengeDto.getName()) != null){
            throw new IllegalStateException("Challenge with the same name already Exists!");
        }

//        Get Category
        Category category = categoryRepo.findById(catid).orElseThrow(() -> new EntityNotFoundException("Category Not Found!"));
//         Attach category
        challengeDto.setCategory(category);

        Challenge challenge = convertToEntity(challengeDto);

        challengeRepo.save(challenge);
    }

    //    Edit
    public void putChallenge(ChallengeDTO challengeDto, long id) {

//        if (challengeRepo.findByName(challengeDto.getName()) != null){
//            throw new IllegalStateException("Challenge with the same name already Exists!");
//        }

        Challenge challenge = challengeRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Challenge Not Found!"));

        if(challengeDto.getName() != null) {
            challenge.setName(challengeDto.getName());
        }

        if(challengeDto.getDescription() != null) {
            challenge.setDescription(challengeDto.getDescription());
        }

        if(challengeDto.getFlag() != null) {
            challenge.setFlag(challengeDto.getFlag());
        }
        if(challengeDto.getChallengeImage() != null) {
            challenge.setChallengeImage(challengeDto.getChallengeImage());
        }

        if(challengeDto.getStars() != 0) {
            challenge.setStars(challengeDto.getStars());
        }

        challenge.setCompleted(challengeDto.isCompleted());

        if(challengeDto.getCategory() != null){
            challenge.setCategory(challengeDto.getCategory());
        }
        if(challengeDto.getHints() != null){
            challenge.setHints(challengeDto.getHints());
        }

        challengeRepo.save(challenge);
    }

    //    Delete
    public void deleteChallenge(long id) {

        Challenge challenge = challengeRepo.findById(id).orElseThrow(()-> new EntityNotFoundException("Challenge Not Found!"));

        challengeRepo.delete(challenge);
    }

    //      GET TOTAL NUMBER OF USERS
    public long totalChallenges() {
        return challengeRepo.findAll().stream().count();
    }
}
