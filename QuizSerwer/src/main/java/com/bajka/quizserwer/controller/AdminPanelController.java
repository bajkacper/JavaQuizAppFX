package com.bajka.quizserwer.controller;

import com.bajka.quizserwer.entity.*;
import com.bajka.quizserwer.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminPanelController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final StudentService studentService;
    private final TestService testService;

    @Autowired
    public AdminPanelController(QuestionService questionService, AnswerService answerService, StudentService studentService, TestService testService) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.studentService = studentService;
        this.testService = testService;
    }

    @GetMapping("/questions")
    @ResponseBody
    public ResponseEntity<List<Question>> showQuestions() {
        List<Question> questions = questionService.getAllQuestionsWithAnswers();
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @GetMapping("/questions/{id}")
    @ResponseBody
    public ResponseEntity<Question> getQuestion(@PathVariable Long id) {
        Optional<Question> question = questionService.findQuestionById(id);
        return question.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/addQuestion", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addQuestion(@RequestBody Question question) {
        questionService.saveQuestion(question);
        if (question.getAnswers() != null) {
            for (Answer answer : question.getAnswers()) {
                answer.setQuestion(question);
                answerService.saveAnswer(answer);
            }
        }
        return new ResponseEntity<>("Question added successfully", HttpStatus.CREATED);
    }

    @PutMapping("/updateQuestion/{id}")
    public ResponseEntity<String> updateQuestion(@PathVariable Long id, @RequestBody Question updatedQuestion) {
        Optional<Question> existingQuestion = questionService.findQuestionById(id);
        if (existingQuestion.isPresent()) {
            Question question = existingQuestion.get();
            question.setQuestion(updatedQuestion.getQuestion());
            List<Answer> updatedAnswers = updatedQuestion.getAnswers();
//            question.setImageData(updatedQuestion.getImageData());
            if (updatedAnswers != null) {
                for (Answer updatedAnswer : updatedAnswers) {
                    Optional<Answer> existingAnswer = question.getAnswers().stream()
                            .filter(a -> a.getId().equals(updatedAnswer.getId()))
                            .findFirst();
                    if (existingAnswer.isPresent()) {
                        Answer answer = existingAnswer.get();
                        answer.setAnswer(updatedAnswer.getAnswer());
                        answer.setCorrect(updatedAnswer.isCorrect());
                        answerService.saveAnswer(answer);
                    }
                }
            }
            if(updatedQuestion.getImageData()!=null){
            question.setImageData(updatedQuestion.getImageData());}
            else{
                question.setImageData(null);
            }
            questionService.saveQuestion(question);
            return new ResponseEntity<>("Question updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Question not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteQuestion/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long id) {
        if (questionService.findQuestionById(id).isPresent()) {
            questionService.deleteQuestion(id);
            return new ResponseEntity<>("Question deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Question not found", HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/deleteAnswer/{id}")
    public ResponseEntity<String> deleteAnswer(@PathVariable Long id) {
        answerService.deleteAnswerById(id);
        return ResponseEntity.ok("Answer with ID " + id + " deleted successfully");
    }
    @PostMapping("/postAnswer/{questionId}")
    public ResponseEntity<String> postAnswer(@RequestBody Answer answer, @PathVariable Long questionId) {
        // Create a new Answer object
        Answer answer1 = new Answer();

        // Fetch the Question from the service
        Optional<Question> quest = questionService.findQuestionById(questionId);

        // Check if the Question exists in the Optional
        if (quest.isPresent()) {
            // Set properties of the Answer object
            answer1.setAnswer(answer.getAnswer());
            answer1.setCorrect(answer.isCorrect());

            // Set the Question in the Answer object
            answer1.setQuestion(quest.get()); // Extract the Question from Optional and set it

            // Save the Answer using the answerService (assuming this is correct in your context)
            answerService.saveAnswer(answer1);

            // Return a success response
            return ResponseEntity.ok("Answer added successfully");
        } else {
            // Handle case where Question with given id is not found
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/users")
    @ResponseBody
    public ResponseEntity<List<Student>> showUsers() {
        List<Student> students = studentService.findAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/tests")
    @ResponseBody
    public ResponseEntity<List<Test>> getAllTests() {
        List<Test> tests = testService.getAllTests();
        return new ResponseEntity<>(tests, HttpStatus.OK);
    }

}
