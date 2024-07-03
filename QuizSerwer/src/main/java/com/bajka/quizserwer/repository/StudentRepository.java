package com.bajka.quizserwer.repository;

import com.bajka.quizserwer.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByIndex(String index);

    Optional<Student> findByIndexAndPassword(String index, String password);

    boolean existsByIndex(String index);
}
