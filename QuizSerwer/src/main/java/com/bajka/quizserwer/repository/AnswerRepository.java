package com.bajka.quizserwer.repository;

import com.bajka.quizserwer.entity.Answer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer,Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Answer a WHERE a.id = :answerId")
    void deleteAnswerById(Long answerId);
}

