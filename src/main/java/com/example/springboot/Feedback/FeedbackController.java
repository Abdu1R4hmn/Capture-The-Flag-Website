package com.example.springboot.Feedback;

import com.example.springboot.exceptions.challengeException.ChallengeNotFoundException;
import com.example.springboot.responses.ApiResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    // Admin: Get all feedbacks
    @GetMapping("/get/all")
    public ResponseEntity<ApiResponseDto<List<FeedbackDTO>>> getAllFeedbacks(int page, int size) {
        return feedbackService.getAllFeedbacks(page, size);
    }

    // Get feedbacks for a specific challenge
    @GetMapping("/get/challenge/{challengeName}")
    public ResponseEntity<ApiResponseDto<List<FeedbackDTO>>> getFeedbacksByChallenge(@PathVariable String challengeName) throws ChallengeNotFoundException {
        return feedbackService.getFeedbacksByChallengeId(challengeName);
    }

    // Add new feedback
    @PostMapping("/post/{challengeId}")
    public ResponseEntity<ApiResponseDto<?>> addFeedback(@RequestBody FeedbackDTO feedbackDTO, @PathVariable Long challengeId) throws ChallengeNotFoundException {
        return feedbackService.addFeedback(feedbackDTO, challengeId);
    }

    // Delete feedback
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponseDto<?>> deleteFeedback(@PathVariable Long id) {
        return feedbackService.deleteFeedback(id);
    }

    @GetMapping("/total")
    public Long getTotal(){
        return feedbackService.getTotal();
    }
}
