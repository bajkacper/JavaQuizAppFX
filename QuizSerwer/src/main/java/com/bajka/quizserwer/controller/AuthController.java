package com.bajka.quizserwer.controller;

import com.bajka.quizserwer.entity.Student;
import com.bajka.quizserwer.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Student loginStudent) {
        Student student = studentRepository.findByIndex(loginStudent.getIndex())
                .orElse(null);

        if (student == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!student.getPassword().equals(loginStudent.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(student);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Student newStudent) {
        if (studentRepository.existsByIndex(newStudent.getIndex())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        newStudent.setAdmin(false);

        studentRepository.save(newStudent);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
