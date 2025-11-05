package com.example.springboot.Progress;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProgressDTO {
    private Long challengeId;
    private String challengeName;
    private int starsEarned;
}
