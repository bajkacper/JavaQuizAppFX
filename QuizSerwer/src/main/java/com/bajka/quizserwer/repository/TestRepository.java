package com.bajka.quizserwer.repository;

import com.bajka.quizserwer.entity.Student;
import com.bajka.quizserwer.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    List<Test> findAll();

    List<Test> findByStudent(Student student);
}
