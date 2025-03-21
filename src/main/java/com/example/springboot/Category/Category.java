package com.example.springboot.Category;

import com.example.springboot.Challenge.Challenge;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Category {
    @Id
    private Long id;
    private int categoryName;


    @OneToMany(mappedBy = "category" ,fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private List<Challenge> challenge;
}


