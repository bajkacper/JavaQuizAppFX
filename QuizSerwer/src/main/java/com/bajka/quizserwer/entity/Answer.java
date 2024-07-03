package com.bajka.quizserwer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String answer;
    private boolean correct;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonIgnore
    private Question question;

    public Answer(){}

    public Answer(Long id, String answer, boolean correct, Question question){
        this.id = id;
        this.answer = answer;
        this.correct = correct;
        this.question = question;
    }
}