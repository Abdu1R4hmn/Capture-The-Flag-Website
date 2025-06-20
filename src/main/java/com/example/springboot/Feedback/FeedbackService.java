package com.example.springboot.Feedback;

import com.example.springboot.Challenge.Challenge;
import com.example.springboot.Challenge.ChallengeRepo;
import com.example.springboot.exceptions.challengeException.ChallengeNotFoundException;
import com.example.springboot.responses.ApiResponseDto;
import com.example.springboot.responses.ApiResponseStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FeedbackService {

    private final FeedbackRepo feedbackRepo;
    private final ChallengeRepo challengeRepo;

    public FeedbackService(FeedbackRepo feedbackRepo, ChallengeRepo challengeRepo) {
        this.feedbackRepo = feedbackRepo;
        this.challengeRepo = challengeRepo;
    }

    private FeedbackDTO convertToDto(Feedback feedback) {
        FeedbackDTO dto = new FeedbackDTO();
        dto.setId(feedback.getId());
        dto.setComment(feedback.getComment());
        dto.setChallengeName(feedback.getChallenge().getName());
        return dto;
    }

    public ResponseEntity<ApiResponseDto<List<FeedbackDTO>>> getAllFeedbacks(int page, int size) {
        Page<Feedback> list = feedbackRepo.findAll(PageRequest.of(page, size, Sort.by("id").descending()));

        List<FeedbackDTO> fbDto = list.stream().map(this::convertToDto).toList();

        return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), fbDto,list.getTotalPages()));
    }

    public ResponseEntity<ApiResponseDto<List<FeedbackDTO>>> getFeedbacksByChallengeId(String challengeName) throws ChallengeNotFoundException {
        Challenge challenge = challengeRepo.findByName(challengeName);

        List<FeedbackDTO> list = feedbackRepo.findByChallenge(challenge)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), list));
    }

    public ResponseEntity<ApiResponseDto<?>> addFeedback(FeedbackDTO dto, Long challengeId) throws ChallengeNotFoundException {

        if (dto.getComment() == null || dto.getComment().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponseDto<>(ApiResponseStatus.FAIL.name(), "Comment cannot be empty."));
        }

        Challenge challenge = challengeRepo.findById(challengeId)
                .orElseThrow(() -> new ChallengeNotFoundException("Challenge not found with ID: " + challengeId));

        Feedback feedback = new Feedback();
        feedback.setComment(dto.getComment());
        feedback.setChallenge(challenge);

        feedbackRepo.save(feedback);
        return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), "Feedback added."));
    }

    public ResponseEntity<ApiResponseDto<?>> deleteFeedback(Long id) {
        if (!feedbackRepo.existsById(id)) {
            return ResponseEntity.status(404).body(new ApiResponseDto<>(ApiResponseStatus.FAIL.name(), "Feedback not found."));
        }
        feedbackRepo.deleteById(id);
        return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), "Feedback deleted."));
    }


    public Long getTotal() {
        Long count = feedbackRepo.findAll().stream().count();
        return count;
    }
}
