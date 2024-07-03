package com.bajka.quizserwer.controller;

import com.bajka.quizserwer.entity.*;
import com.bajka.quizserwer.service.AnswerService;
import com.bajka.quizserwer.service.QuestionService;
import com.bajka.quizserwer.service.StudentService;
import com.bajka.quizserwer.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final TestService testService;
    private final StudentService studentService;

    @Autowired
    public UserController(QuestionService questionService, AnswerService answerService, TestService testService, StudentService studentService) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.testService = testService;
        this.studentService = studentService;
    }

    @GetMapping("/randomQuestions/{numberOfQuestions}")
    @ResponseBody
    public ResponseEntity<List<Question>> getRandomQuestions(@PathVariable int numberOfQuestions) {
        List<Question> randomQuestions = questionService.getRandomQuestions(numberOfQuestions);
        return new ResponseEntity<>(randomQuestions, HttpStatus.OK);
    }

//    @PostMapping("/submitTest")
//    public ResponseEntity<String> submitTest(@RequestBody List<Answer> answers) {
//        // Przyjmujemy listę odpowiedzi od użytkownika
//        // Możesz tutaj dodać logikę sprawdzającą poprawność odpowiedzi i obliczania wyniku testu
//        Student currentStudent = studentService.getCurrentStudent();
//        Test test = new Test();
//        test.setStudent(currentStudent);
//        // Przykład: Obliczanie wyniku
//        int score = calculateScore(answers); // Przykładowa metoda do obliczania wyniku
//        test.setScore(String.valueOf(score));
//        testService.saveTest(test);
//        return new ResponseEntity<>("Test submitted successfully", HttpStatus.CREATED);
//    }
@PostMapping("/submitTest")
public ResponseEntity<String> submitTest(@RequestBody TestSubmissionDTO testSubmission) {
    Long indexNumber = testSubmission.getStudent().getId();
    System.err.println(indexNumber);
    Student student = studentService.findById(indexNumber);
    if (student == null) {
        return new ResponseEntity<>("Student not found", HttpStatus.NOT_FOUND);
    }

    List<Test> studentTests = testService.findTestsByStudent(student);
    int shotCount = studentTests.size() + 1;
    Test test = new Test();
    test.setStudent(student);
    test.setMaxScore(testSubmission.getMaxScore());
    test.setScore(testSubmission.getScore());
    test.setGrade(testSubmission.getGrade());
    test.setShot(shotCount);
    test.setData(LocalDate.now());
    testService.saveTest(test);

    return new ResponseEntity<>("Test submitted successfully", HttpStatus.CREATED);
}

    @GetMapping("/tests/{studentId}")
    public ResponseEntity<List<Test>> getTestsForStudent(@PathVariable Long studentId) {
        Student student = studentService.findById(studentId);
        if (student == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Test> tests = testService.findTestsByStudent(student);

        return new ResponseEntity<>(tests, HttpStatus.OK);
    }
    private int calculateScore(List<Answer> answers) {
        int score = 0;
        for (Answer answer : answers) {
            if (answer.isCorrect()) {
                score++;
            }
        }
        return score;
    }
}
