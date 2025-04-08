package com.example.springboot.Category;

import com.example.springboot.Challenge.Challenge;
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
    private Long id;
    private String type;

    @OneToMany(mappedBy = "category" ,fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private List<Challenge> challenge;

}


