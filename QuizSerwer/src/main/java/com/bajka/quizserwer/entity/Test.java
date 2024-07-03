package com.bajka.quizserwer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Test {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "max_score")
	private int maxScore;
	private int score;
	private int shot;
	private String grade;
	private LocalDate data;
	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student student;

	public Test(Long id, int shot, int score, int maxScore, Student student, String grade){
		this.id = id;
		this.shot = shot;
		this.score = score;
		this.maxScore = maxScore;
		this.student = student;
		this.grade = grade;
	}
	public Test() {
	}
	public String getStudentIndex() {
		return student != null ? student.getIndex() : null;
	}


}