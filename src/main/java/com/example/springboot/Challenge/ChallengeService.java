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
        return new ChallengeDTO(challenge.getName(),challenge.getDescription(),challenge.getDifficulty(),challenge.getFlag(),challenge.getChallengeImage(),challenge.getCategory(), challenge.getHints());
    }
    public Challenge convertToEntity(ChallengeDTO challengeDto){
        return new Challenge(challengeDto.getName(),challengeDto.getDescription(),challengeDto.getDifficulty(),challengeDto.getFlag(),challengeDto.getChallengeImage(),challengeDto.getCategory());
    }


//    CRUD

    //    GET All
    public List<ChallengeDTO> getChallengeAll() {
        List<ChallengeDTO> challenges = challengeRepo.findAll().stream().map(this::convertToDto).toList();
        return challenges;
    }

    //    GET Difficulty
    public List<ChallengeDTO> getChallengeDifficulty(Difficulty diff) {

        List<Challenge> challenges = challengeRepo.findAllByDifficulty(diff);

        return challenges.stream().map(this::convertToDto).toList();
    }

    //    GET Category
    public List<ChallengeDTO> getChallengeCategory(Category category) {

        List<Challenge> challenges = challengeRepo.findAllByCategory(category);

        return challenges.stream().map(this::convertToDto).toList();
    }

    //    Get Id
    public ChallengeDTO getChallenge(long id) {
        Challenge challenge = challengeRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("challenge not found"));

        ChallengeDTO challengeDTO = convertToDto(challenge);

        return challengeDTO;
    }

    //    Post :: How to make it submit hints with the challenges together?
    public void postChallenge(ChallengeDTO challengeDto, long catid) {

        if (challengeRepo.findByName(challengeDto.getName()) != null){
            throw new IllegalStateException("Challenge with the same name already Exists!");
        }

        Category category = categoryRepo.findById(catid).orElseThrow(() -> new EntityNotFoundException("Category Not Found!"));

        challengeDto.setCategory(category);

        Challenge challenge = convertToEntity(challengeDto);

        challengeRepo.save(challenge);
    }

    //    Edit
    public void putChallenge(ChallengeDTO challengeDto, long id) {

        Challenge challenge = challengeRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Challenge Not Found!"));

        if (challengeRepo.findByName(challengeDto.getName()) != null){
            throw new IllegalStateException("Challenge with the same name already Exists!");
        }

        challenge.setName(challengeDto.getName());
        challenge.setDescription(challengeDto.getDescription());
        challenge.setFlag(challengeDto.getFlag());
        challenge.setChallengeImage(challenge.getChallengeImage());
        challenge.setDifficulty(challengeDto.getDifficulty());

        if(challengeDto.getCategory() != null){
            challenge.setCategory(challengeDto.getCategory());
        }

        challengeRepo.save(challenge);
    }

    //    Delete
    public void deleteChallenge(long id) {

        Challenge challenge = challengeRepo.findById(id).orElseThrow(()-> new EntityNotFoundException("Challenge Not Found!"));

        challengeRepo.delete(challenge);
    }


}
