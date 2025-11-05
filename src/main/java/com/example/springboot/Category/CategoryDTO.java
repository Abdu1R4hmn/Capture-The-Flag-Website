package com.example.springboot.Category;

import com.example.springboot.Challenge.Challenge;
import jakarta.persistence.Column;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CategoryDTO {

    private long id;

    @Column(unique = true)
    private String type;

//    private List<Challenge> challenge;
}
