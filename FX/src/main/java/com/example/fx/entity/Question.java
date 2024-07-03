package com.example.fx.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Question {
	private Long id;
	private String question;
	private String imageData;

	@JsonManagedReference
	private List<Answer> answers;

	public Question() {
	}

	@JsonCreator
	public Question(
			@JsonProperty("id") Long id,
			@JsonProperty("question") String question,
			@JsonProperty("imageData") String imageData,
			@JsonProperty("answers") List<Answer> answers) {
		this.id = id;
		this.question = question;
		this.imageData = imageData;
		this.answers = answers;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getImageData() {
		return imageData;
	}

	public void setImageData(String imageData) {
		this.imageData = imageData;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	@Override
	public String toString() {
		return question;
	}

	public void setImage(String encodedImage) {
		this.imageData = encodedImage;
	}
	public String getImage(){
		return imageData;
	}
}
