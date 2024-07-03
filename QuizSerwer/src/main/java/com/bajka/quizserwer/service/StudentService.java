package com.bajka.quizserwer.service;

import com.bajka.quizserwer.entity.Student;
import com.bajka.quizserwer.repository.StudentRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final CurrentStudentService currentStudentService;

    @Getter
    private Student currentStudent;

    @Autowired
    public StudentService(StudentRepository studentRepository, CurrentStudentService currentStudentService) {
        this.studentRepository = studentRepository;
        this.currentStudentService = currentStudentService;
    }
    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    public void registerStudent(Student student) {
        studentRepository.save(student);
        currentStudentService.setCurrentStudent(student);
    }

    public boolean validateStudent(String index, String password) {
        Optional<Student> studentOptional = studentRepository.findByIndexAndPassword(index, password);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            currentStudentService.setCurrentStudent(student);
            return true;
        }
        return false;
    }

    public boolean existsByIndex(String index) {
        return studentRepository.existsByIndex(index);
    }

    public Student findByIndex(String index) {
        Optional<Student> studentOptional = studentRepository.findByIndex(index);
        return studentOptional.orElse(null);
    }

    public Student findById(Long indexNumber) {
        Optional<Student> studentOptional = studentRepository.findById(indexNumber);
        return studentOptional.orElse(null);
    }
}
