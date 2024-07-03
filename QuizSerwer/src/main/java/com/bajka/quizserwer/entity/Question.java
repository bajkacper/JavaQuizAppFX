package com.bajka.quizserwer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Question {
	@Column (nullable = false)
	String question;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Lob
	private String imageData;
	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Answer> answers;
	public Question(Long id, String question){
		this.id = id;
		this.question = question;
	}
	public Question(){}

	@Override
	public String toString() {
		return question;
	}

}