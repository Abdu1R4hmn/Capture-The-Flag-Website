package com.example.springboot.Progress;

import com.example.springboot.Challenge.Challenge;
import com.example.springboot.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JsonIgnore
    private User user;

//    Add the Solved Challenges

    @OneToMany(mappedBy = "progress",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Challenge> challengeList;

    public Progress(List<Challenge> challengeList){

        this.challengeList = challengeList;
    }
}
