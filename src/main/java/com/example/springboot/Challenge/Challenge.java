package com.example.springboot.Challenge;

import com.example.springboot.Category.Category;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Challenge {
    @Id
    private Long id;

    @ManyToOne
    private Category category;
}
