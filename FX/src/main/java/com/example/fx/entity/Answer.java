package com.example.fx.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Answer {
    private Long id;
    private String answer;
    private boolean correct;

    @JsonBackReference
    private Question question;

    public Answer() {
    }

    @JsonCreator
    public Answer(
            @JsonProperty("id") Long id,
            @JsonProperty("answer") String answer,
            @JsonProperty("correct") boolean correct,
            @JsonProperty("question") Question question) {
        this.id = id;
        this.answer = answer;
        this.correct = correct;
        this.question = question;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return answer;
    }
}
