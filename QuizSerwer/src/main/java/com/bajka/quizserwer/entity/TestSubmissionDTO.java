package com.bajka.quizserwer.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class TestSubmissionDTO {

    @JsonProperty("student")
    private StudentDTO student;

    //@JsonProperty("answers")
    //private List<AnswerSubmissionDTO> answers;

    @JsonProperty("score")
    private int score;

    @JsonProperty("maxScore")
    private int maxScore;
    @JsonProperty("grade")
    private String grade;
    @JsonProperty("data")
    private LocalDate data;

    public StudentDTO getStudent() {
        return student;
    }

    public void setStudent(StudentDTO student) {
        this.student = student;
    }



    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}