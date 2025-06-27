package com.example.springboot.Progress;

import com.example.springboot.Challenge.Challenge;
import com.example.springboot.Challenge.ChallengeDTO;
import com.example.springboot.Challenge.Difficulty;
import com.example.springboot.User.User;
import com.example.springboot.User.UserService;
import com.example.springboot.exceptions.userException.UserNotFoundException;
import com.example.springboot.responses.ApiResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/progress")
public class ProgressController {

    private final ProgressService progressService;
    private final UserService userService;

    public ProgressController(ProgressService progressService, UserService userService) {
        this.progressService = progressService;
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('USER','LECTURER','ADMIN')")
    @GetMapping("/get")
    public List<ProgressDTO> getSolvedProgressByUser(Authentication authentication) throws UserNotFoundException {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        return progressService.getSolvedProgressByUser(user);
    }

    @PreAuthorize("hasAnyRole('USER','LECTURER','ADMIN')")
    @GetMapping("/get/{diff}")
    public ResponseEntity<ApiResponseDto<List<ProgressDTO>>> getSolvedChallengesByDifficulty(
            @PathVariable("diff") Difficulty diff,
            Authentication authentication
    ) throws UserNotFoundException {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);

        List<ProgressDTO> list = progressService.getSolvedChallengesByDifficulty(user, diff);
        return ResponseEntity.ok(new ApiResponseDto<>("SUCCESS", list));
    }

    @PreAuthorize("hasAnyRole('USER','LECTURER','ADMIN')")
    @GetMapping("/stars/summary/full")
    public ResponseEntity<ApiResponseDto<Map<Difficulty, DifficultyStarSummary>>> getFullStarSummary(
            Authentication authentication
    ) throws UserNotFoundException {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        Map<Difficulty, DifficultyStarSummary> summary = progressService.getStarSummaryPerDifficulty(user);
        return ResponseEntity.ok(new ApiResponseDto<>("SUCCESS", summary));
    }
}
