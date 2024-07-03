package com.bajka.quizserwer.service;

import com.bajka.quizserwer.entity.Student;
import com.bajka.quizserwer.entity.Test;
import com.bajka.quizserwer.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TestService {
    private final TestRepository testRepository;
    public List<Test> findAllTest() {
        return testRepository.findAll();
    }
    @Autowired
    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }
    public List<Test> findTestsByStudent(Student student) {
        return testRepository.findByStudent(student);
    }
    public Test saveTest(Test test) {
        return testRepository.save(test);
    }

    public List<Test> getAllTests() { return testRepository.findAll();
    }
}
