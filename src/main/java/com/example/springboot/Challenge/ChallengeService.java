package com.example.springboot.Challenge;

import com.example.springboot.Category.Category;
import com.example.springboot.Progress.Progress;
import com.example.springboot.Progress.ProgressDTO;
import com.example.springboot.Progress.ProgressRepo;
import com.example.springboot.User.User;
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
import java.util.Optional;

@Service
public class ChallengeService {

    //    Variables
    private final ChallengeRepo challengeRepo;
    private final CategoryRepo categoryRepo;
    private final ProgressRepo progressRepo;

    //    Constructor
    public ChallengeService(ChallengeRepo challengeRepo, CategoryRepo categoryRepo, ProgressRepo progressRepo){
        this.challengeRepo = challengeRepo;
        this.categoryRepo = categoryRepo;
        this.progressRepo = progressRepo;
    }

    //    DTOs
    public ChallengeDTO convertToDto(Challenge challenge){
        return new ChallengeDTO(
            challenge.getId(),
            challenge.getName(),
            challenge.getDescription(),
            challenge.getSolution(),
            challenge.getChallengeImage(),
            challenge.getDifficulty(),
            challenge.getFlag(),
            challenge.getFeedback(),
            challenge.getCategory(),
            challenge.getHint1(),
            challenge.getHint2()// <-- add this
        );
    }

    public ChallengePublicDTO convertToPublicDto(Challenge challenge){
        return new ChallengePublicDTO( challenge.getId(),challenge.getName(),challenge.getDescription(),challenge.getSolution(),challenge.getDifficulty(), challenge.getChallengeImage(),challenge.getCategory(),challenge.getHint1(),challenge.getHint2());
    }

    public Challenge convertToEntity(ChallengeDTO dto) {
        Challenge challenge = new Challenge(
                dto.getName(), dto.getDescription(), dto.getDifficulty(), dto.getFlag(),dto.getChallengeImage(), dto.getCategory(), dto.getHint1(), dto.getHint2(), dto.getSolution()
        );
        // challenge.setSolution(dto.getSolution()); // <-- Ensure this line exists
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

    //    GET All User
    public ResponseEntity<ApiResponseDto<List<ChallengePublicDTO>>> getChallengePublicAll(int page, int size) {
        try {
            Page<Challenge> challengesPage = challengeRepo.findAll(PageRequest.of(page, size, Sort.by("id").descending()));

            List<ChallengePublicDTO> challengePublicDto = challengesPage.getContent().stream().map(this::convertToPublicDto).toList();

            return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), challengePublicDto, challengesPage.getTotalPages()));

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

    public  ResponseEntity<ApiResponseDto<List<ChallengePublicDTO>>> getChallengePublicCategory(String categoryType) throws ChallengeNotFoundException {
        try {
            Category category = categoryRepo.findByType(categoryType);
            if (category == null) {
                throw new ChallengeNotFoundException("Category not found.");
            }

            List<Challenge> challenges = challengeRepo.findAllByCategory(category);
            List<ChallengePublicDTO> challengesPublicDto = challenges.stream().map(this::convertToPublicDto).toList();

            return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), challengesPublicDto));
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

            if(challengeDto.getCategory() != null) {
                challenge.setCategory(challengeDto.getCategory());
            }

            if (challengeDto.getDifficulty() != null) {
                challenge.setDifficulty(challengeDto.getDifficulty());
            }
            

            if(challengeDto.getHint1() != null){
                challenge.setHint1(challengeDto.getHint1());
            }

            if(challengeDto.getHint2() != null){
                challenge.setHint2(challengeDto.getHint2());
            }

            if(challengeDto.getSolution() != null) {
                challenge.setSolution(challengeDto.getSolution());
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
    public ResponseEntity<ApiResponseDto<?>> solveChallenge(Long challengeId, ChallengeSolveRequestDTO dto, User user) {
        try {
            Challenge challenge = challengeRepo.findById(challengeId)
                    .orElseThrow(() -> new ChallengeNotFoundException("Challenge not found"));

            if (!challenge.getFlag().equals(dto.getSubmittedFlag())) {
                return ResponseEntity.badRequest().body(
                        new ApiResponseDto<>(ApiResponseStatus.FAIL.name(), "Incorrect flag")
                );
            }

            int stars = switch (dto.getUsedHints() == null ? 0 : dto.getUsedHints().size()) {
                case 0 -> 3;
                case 1 -> 2;
                default -> 1;
            };

            Optional<Progress> existingProgressOpt = progressRepo.findByUserAndChallenge(user, challenge);

            Progress progress;
            if (existingProgressOpt.isPresent()) {
                progress = existingProgressOpt.get();
                progress.setStars(stars); // Optionally keep max stars only
            } else {
                progress = new Progress();
                progress.setUser(user);
                progress.setChallenge(challenge);
                progress.setStars(stars);
            }

            progressRepo.save(progress);

            ProgressDTO response = new ProgressDTO(challenge.getId(), challenge.getName(), stars);
            return ResponseEntity.ok(new ApiResponseDto<>("SUCCESS", response));

        } catch (ChallengeNotFoundException e) {
            return ResponseEntity.status(404).body(
                    new ApiResponseDto<>("FAILED", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponseDto<>("FAILED", "An unexpected error occurred")
            );
        }
    }

}
