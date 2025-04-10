package com.example.springboot.Hints;

import com.example.springboot.Challenge.Challenge;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Hints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String hint_1;

    private String hint_2;

    @JsonIgnore
    @OneToOne(mappedBy = "hints" ,fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private Challenge challenge;


//    Constructor
    public Hints(String hint_1, String hint_2) {
        this.hint_1 = hint_1;
        this.hint_2 = hint_2;
    }
}
