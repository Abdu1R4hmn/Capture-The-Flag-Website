package com.example.springboot.Category;

import com.example.springboot.Challenge.Challenge;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;

    @JsonIgnore
    @OneToMany(mappedBy = "category" ,fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private List<Challenge> challenge;

    public Category(String type){
        this.type = type;
    }
}


