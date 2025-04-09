package com.example.springboot.Challenge;

import com.example.springboot.Category.Category;
import com.example.springboot.Category.CategoryDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import com.example.springboot.Category.CategoryRepo;

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
        Category category = challenge.getCategory();
        CategoryDTO categoryDto = new CategoryDTO(category.getType());

        return new ChallengeDTO(challenge.getName(),challenge.getDescription(),challenge.getDifficulty(),challenge.getFlag(),challenge.getChallengeImage(),challenge.getCategory());
    }
    public Challenge convertToEntity(ChallengeDTO challengeDto){
        return new Challenge(challengeDto.getName(),challengeDto.getDescription(),challengeDto.getDifficulty(),challengeDto.getFlag(),challengeDto.getChallengeImage(),challengeDto.getCategory());
    }


//    CRUD

    //    Get ALl
    public List<ChallengeDTO> getChallengeAll() {
        List<ChallengeDTO> challenges = challengeRepo.findAll().stream().map(this::convertToDto).toList();
        return challenges;
    }


    //    Post
    public void postChallenge(ChallengeDTO challengeDto, long catid) {

        if (challengeRepo.findByName(challengeDto.getName()) != null){
            throw new IllegalStateException("Challenge with the same name already Exists!");
        }

        Category category = categoryRepo.findById(catid).orElseThrow(() -> new EntityNotFoundException("Category Not Found!"));

        challengeDto.setCategory(category);

        Challenge challenge = convertToEntity(challengeDto);

        challengeRepo.save(challenge);
    }
}
