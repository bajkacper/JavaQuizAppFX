package com.example.fx.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Test {

	private Long id;
	private int maxScore;
	private int score;
	private int shot;
	private Student student;
	private String studentIndex;
	private String grade;

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	private LocalDate data;

	public Test() {
	}

	public Long getId() {
		return id;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public void setStudentId(Long studentId) {
		if (this.student == null) {
			this.student = new Student();
		}
		this.student.setId(studentId);
	}
	public void setId(Long id) {
		this.id = id;
	}

	public int getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getShot() {
		return shot;
	}

	public void setShot(int shot) {
		this.shot = shot;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public String getStudentIndex() {
		return studentIndex;
	}

	public void setStudentIndex(String studentIndex) {
		this.studentIndex = studentIndex;
	}

	@Override
	public String toString() {
		return "Test{" +
				"id=" + id +
				", maxScore=" + maxScore +
				", score=" + score +
				", shot=" + shot +
				", student=" + student +
				", studentIndex='" + studentIndex + '\'' +
				'}';
	}
}
