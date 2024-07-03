package com.bajka.quizserwer.controller;

import com.bajka.quizserwer.entity.Student;
import com.bajka.quizserwer.entity.Test;
import com.bajka.quizserwer.service.StudentService;
import com.bajka.quizserwer.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/panel")
public class UserPanelController {

    private final StudentService studentService;
    private final TestService testService;

    @Autowired
    public UserPanelController(StudentService studentService, TestService testService) {
        this.studentService = studentService;
        this.testService = testService;
    }
// wyniki z wszystkich testow, test z losowymi 10 pytaniami
@GetMapping("/results/{index}")
public List<Test> getResultsForEachTest(@PathVariable String index) {
    Student student = studentService.findByIndex(index);
    if (student != null) {
        return testService.findTestsByStudent(student);
    }
    return null; // or handle differently as per your application's logic
}
    @GetMapping("/results/{id}")
    public List<Test> getAllResultsForStudent(@PathVariable Long id) {
        Student student = studentService.findById(id);
        if (student != null) {
            return testService.findTestsByStudent(student);
        }
        return null; // or handle differently as per your application's logic
    }
}
