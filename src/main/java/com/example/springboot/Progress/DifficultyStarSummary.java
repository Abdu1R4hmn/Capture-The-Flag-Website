package com.example.springboot.Progress;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DifficultyStarSummary {
    private int earned;
    private int max;

    public DifficultyStarSummary(int earned, int max) {
        this.earned = earned;
        this.max = max;
    }

}

