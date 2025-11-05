    
package com.example.springboot.Challenge;

import lombok.Data;

import java.util.List;

@Data
public class ChallengeSolveRequestDTO {
    private String submittedFlag;
    private List<Integer> usedHints; // e.g., [1,2], [1], or []
}
