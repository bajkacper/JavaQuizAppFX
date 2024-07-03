package com.bajka.quizserwer.service;

import com.bajka.quizserwer.entity.Answer;
import com.bajka.quizserwer.repository.AnswerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }
    public void saveAnswer(Answer answer) {
        answerRepository.save(answer);
    }

    public List<Answer> getAllAnswer(){
        return answerRepository.findAll();
    }
    public Optional<Answer> getAnswerById(Long id) {
        return answerRepository.findById(id);
    }

    @Transactional
    public void deleteAnswerById(Long answerId) {
        answerRepository.deleteAnswerById(answerId);
    }
}
