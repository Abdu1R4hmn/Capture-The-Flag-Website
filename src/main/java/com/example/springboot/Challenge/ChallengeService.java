package com.example.springboot.Challenge;

import com.example.springboot.Category.Category;
import com.example.springboot.exceptions.challengeException.ChallengeServiceLogicException;
import com.example.springboot.responses.ApiResponseDto;
import com.example.springboot.responses.ApiResponseStatus;
import com.example.springboot.exceptions.challengeException.ChallengeAlreadyExistsException;
import com.example.springboot.exceptions.challengeException.ChallengeNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return new ChallengeDTO(challenge.getId(), challenge.getName(),challenge.getDescription(),challenge.getChallengeImage(), challenge.getDifficulty(),challenge.getFlag(),challenge.isCompleted(), challenge.getStars(),challenge.getFeedback(),challenge.getCategory(),challenge.getHint1(),challenge.getHint2());
    }
    public ChallengePublicDTO convertToPublicDto(Challenge challenge){
        return new ChallengePublicDTO( challenge.getId(),challenge.getName(),challenge.getDescription(),challenge.getDifficulty(),challenge.isCompleted(), challenge.getStars(), challenge.getChallengeImage(),challenge.getCategory(),challenge.getHint1(),challenge.getHint2());
    }

    public Challenge convertToEntity(ChallengeDTO dto) {
        Challenge challenge = new Challenge(
                dto.getName(), dto.getDescription(), dto.getDifficulty(), dto.getFlag(),
                dto.isCompleted(), dto.getStars(), dto.getChallengeImage(), dto.getCategory(), dto.getHint1(), dto.getHint2()
        );
        return challenge;
    }


//    CRUD

    //    GET All ADMIN
    public ResponseEntity<ApiResponseDto<List<ChallengeDTO>>> getChallengeAll(int page, int size) {
        try {
            Page<Challenge> challengesPage = challengeRepo.findAll(PageRequest.of(page, size, Sort.by("id").descending()));

            List<ChallengeDTO> challengeDto = challengesPage.getContent().stream().map(this::convertToDto).toList();

            return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), challengeDto, challengesPage.getTotalPages()));

        } catch (Exception e) {
            throw new ChallengeServiceLogicException("Failed to retrieve challenges");
        }
    }

    //    GET Difficulty
    public ResponseEntity<ApiResponseDto<List<ChallengePublicDTO>>> getChallengeDifficulty(Difficulty diff) {
        try {
            List<Challenge> challenges = challengeRepo.findAllByDifficulty(diff);
            List<ChallengePublicDTO> challengesDto = challenges.stream().map(this::convertToPublicDto).toList();
            return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), challengesDto));
        } catch (Exception e) {
            throw new ChallengeServiceLogicException("Failed to retrieve challenges by difficulty");
        }
    }

    //    GET Category
    public  ResponseEntity<ApiResponseDto<List<ChallengeDTO>>> getChallengeCategory(String categoryType) throws ChallengeNotFoundException {
        try {
            Category category = categoryRepo.findByType(categoryType);
            if (category == null) {
                throw new ChallengeNotFoundException("Category not found.");
            }

            List<Challenge> challenges = challengeRepo.findAllByCategory(category);
            List<ChallengeDTO> challengesDto = challenges.stream().map(this::convertToDto).toList();

            return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), challengesDto));
        } catch (ChallengeNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ChallengeServiceLogicException("Failed to retrieve challenges by category");
        }
    }

    //    Get Id
    public  ResponseEntity<ApiResponseDto<ChallengePublicDTO>> getChallenge(long id) throws ChallengeNotFoundException {
        try {
            Challenge challenge = challengeRepo.findById(id).orElseThrow(() -> new ChallengeNotFoundException("Challenge not found with ID: " + id));
            ChallengePublicDTO challengePublicDTO = convertToPublicDto(challenge);
            return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), challengePublicDTO));
        } catch (ChallengeNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ChallengeServiceLogicException("Failed to retrieve challenge by ID");
        }
    }

    //    Get name
    public ResponseEntity<ApiResponseDto<ChallengeDTO>> getChallengeName(String name) throws ChallengeNotFoundException {
        try {
            if (challengeRepo.findByName(name) == null) {
                throw new ChallengeNotFoundException("Challenge Not Found With The Name " + name);
            }
            Challenge challenge = challengeRepo.findByName(name);
            ChallengeDTO challengeDTO = convertToDto(challenge);
            return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), challengeDTO));
        } catch (ChallengeNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ChallengeServiceLogicException("Failed to retrieve challenge by ID");
        }
    }

    //    Post
    public ResponseEntity<ApiResponseDto<?>> postChallenge(ChallengeDTO challengeDto, long catid) throws ChallengeAlreadyExistsException, ChallengeNotFoundException {
        try {
            if (challengeRepo.findByName(challengeDto.getName()) != null){
                throw new ChallengeAlreadyExistsException("Challenge with the same name already exists!");
            }

//        Get Category
            Category category = categoryRepo.findById(catid).orElseThrow(() -> new ChallengeNotFoundException("Category not found with ID: " + catid));
//         Attach category
            challengeDto.setCategory(category);

            Challenge challenge = convertToEntity(challengeDto);

            challengeRepo.save(challenge);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), "Challenge created successfully!"));
        } catch (ChallengeAlreadyExistsException | ChallengeNotFoundException e) {
            throw e;
        } catch (Exception e) {

            throw new ChallengeServiceLogicException("Failed to create challenge");
        }
    }

    //    Edit
    public ResponseEntity<ApiResponseDto<?>> putChallenge(ChallengeDTO challengeDto, long id, long catid) throws ChallengeNotFoundException, ChallengeAlreadyExistsException {
        try {

            Challenge existingByName = challengeRepo.findByName(challengeDto.getName());
            if (existingByName != null && existingByName.getId() != id) {
                throw new ChallengeAlreadyExistsException("Challenge with the same name already exists!");
            }


            Challenge challenge = challengeRepo.findById(id).orElseThrow(() -> new ChallengeNotFoundException("Challenge not found with ID: " + id));

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

            if(challengeDto.getHint1() != null){
                challenge.setHint1(challengeDto.getHint1());
            }

            if(challengeDto.getHint2() != null){
                challenge.setHint2(challengeDto.getHint2());
            }

            //  Update category
            Category category = categoryRepo.findById(catid).orElseThrow(() ->
                    new ChallengeNotFoundException("Category not found with ID: " + catid));

            challenge.setCategory(category);

            challengeRepo.save(challenge);
            return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), "Challenge updated successfully!"));
        } catch (ChallengeNotFoundException e) {
            throw new ChallengeNotFoundException(e.getMessage());
        } catch (ChallengeAlreadyExistsException e) {
            throw new ChallengeAlreadyExistsException(e.getMessage());
        } catch (Exception e) {
            throw new ChallengeServiceLogicException("Failed to update challenge");
        }
    }

    //    Delete
    public ResponseEntity<ApiResponseDto<?>> deleteChallenge(long id) throws ChallengeNotFoundException {
        try {
            Challenge challenge = challengeRepo.findById(id).orElseThrow(()-> new ChallengeNotFoundException("Challenge not found with ID: " + id));
            challengeRepo.delete(challenge);
            return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), "Challenge deleted successfully!"));
        } catch (ChallengeNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ChallengeServiceLogicException("Failed to delete challenge");
        }
    }

    //      GET TOTAL NUMBER OF USERS
    public long totalChallenges() {
        return challengeRepo.findAll().stream().count();
    }


//Functions

    //    Challenge completed
    public void isSolved(Challenge challenge, String flag){
    };

}
