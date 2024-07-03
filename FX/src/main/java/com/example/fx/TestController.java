package com.example.fx;

import com.example.fx.entity.Question;
import com.example.fx.entity.Answer;
import com.example.fx.entity.TestSubmissionDTO;
import com.example.fx.entity.StudentDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class TestController {
    @FXML
    private Label questionLabel;
    @FXML
    private VBox answersBox;
    @FXML
    private Label timerLabel;
    @FXML
    private Button nextButton;
    @FXML
    private ImageView questionImageView;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private final Map<Long, List<Long>> userAnswers = new HashMap<>();
    private int score = 0;
    private int maxScore = 0;
    private Timer timer;
    private int timeRemaining = 3600;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl = "http://localhost:8080/user";
    public void initialize() {
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
        calculateMaxScore(questions);
        displayQuestion();
        startTimer();
    }
//    private void calculateMaxScore() {
//        for (Question question : questions) {
//            for (Answer answer : question.getAnswers()) {
//                if (answer.isCorrect()) {
//                    maxScore++;
//                }
//            }
//        }
//    }
    private void calculateMaxScore(List<Question> questions){
        maxScore=questions.size();
    }

    private void displayQuestion() {
        Question question = questions.get(currentQuestionIndex);
        int counter = currentQuestionIndex+1;
        questionLabel.setText(counter+". "+question.getQuestion());
        answersBox.getChildren().clear();
        for (Answer answer : question.getAnswers()) {
            CheckBox checkBox = new CheckBox(answer.getAnswer());
            checkBox.setUserData(answer.getId());
            answersBox.getChildren().add(checkBox);
        }
        if (question.getImage() != null) {
            byte[] imageBytes = Base64.getDecoder().decode(question.getImage());
            Image image = new Image(new ByteArrayInputStream(imageBytes));
            questionImageView.setImage(image);
        } else {
            questionImageView.setImage(null);
        }
        if (currentQuestionIndex == questions.size() - 1) {
            nextButton.setText("Submit");
        } else {
            nextButton.setText("Next");
        }
    }
    public void startTimer() {
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    int minutes = timeRemaining / 60;
                    int seconds = timeRemaining % 60;
                    timerLabel.setText(String.format("Czas: %02d:%02d", minutes, seconds));
                    if (timeRemaining <= 0) {
                        timer.cancel();
                        showResultsTime();
                        switchToUserPanel();
                    }
                    timeRemaining--;
                });
            }
        }, 0, 1000);
    }
    @FXML
    private void nextQuestion() {
        if (!saveCurrentAnswer()) {
            showError("Wybierz jakąkolwiek odpowiedź.");
            return;
        }
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            displayQuestion();
        } else {
            showResults();
            submitTest();
        }
    }
    private boolean saveCurrentAnswer() {
        List<Long> selectedAnswers = new ArrayList<>();
        for (Node node : answersBox.getChildren()) {
            if (node instanceof CheckBox checkBox) {
                if (checkBox.isSelected()) {
                    Long answerId = (Long) checkBox.getUserData();
                    selectedAnswers.add(answerId);
                }
            }
        }
        if (selectedAnswers.isEmpty()) {
            return false;
        } else {
            checkAnswers(selectedAnswers);
            userAnswers.put(questions.get(currentQuestionIndex).getId(), selectedAnswers);
            return true;
        }
    }
//    private void checkAnswers(List<Long> selectedAnswers) {
//        Question question = questions.get(currentQuestionIndex);
//        for (Long answerId : selectedAnswers) {
//            for (Answer answer : question.getAnswers()) {
//                if (answer.getId().equals(answerId) && answer.isCorrect()) {
//                    score++;
//                }
//            }
//        }
//    }
    //wszystkie poprawne
private void checkAnswers(List<Long> selectedAnswers) {
    Question question = questions.get(currentQuestionIndex);
    List<Long> correctAnswers = new ArrayList<>();

    for (Answer answer : question.getAnswers()) {
        if (answer.isCorrect()) {
            correctAnswers.add(answer.getId());
        }
    }
    if (selectedAnswers.containsAll(correctAnswers) && correctAnswers.containsAll(selectedAnswers)) {
        score++;
    }
}
    private void showResults() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Wyniki testu");
        alert.setHeaderText("Twoje wyniki");
        alert.setContentText("Uzyskałeś/-aś: " + score + " pkt na " + maxScore+ " ocena:" + calculateGrade(score,maxScore));
        alert.showAndWait();
    }
    private void showResultsTime() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Wyniki testu");
        alert.setHeaderText("Czas upłynął. Twoje wyniki");
        alert.setContentText("Uzyskałeś/-aś: " + score + " pkt na " + maxScore+ " ocena: " + calculateGrade(score,maxScore));
        alert.showAndWait();
    }
    private String calculateGrade(int score, int maxScore){
        String grade = new String();
        float percentReceived = ((float)score/maxScore)*100;
        if(percentReceived<60.0) grade = "2.0";
        else if (percentReceived>=60.0&&percentReceived<65.0) {
            grade = "3.0";
        }
        else if (percentReceived>=65.0&&percentReceived<70.0) {
            grade = "3.5";
        }
        else if (percentReceived>=70.0&&percentReceived<80.0) {
            grade = "4.0";
        }
        else if (percentReceived>=80.0&&percentReceived<90.0) {
            grade = "4.5";
        }
        else if (percentReceived>=90.0&&percentReceived<=100.0) {
            grade = "5.0";
        }
        else{
            grade = "Invalid";
        }
        return grade;
    }
    private void submitTest() {
        try {
            Long studentId = getCurrentStudentId();
            StudentDTO student = new StudentDTO();
            student.setId(studentId);

            TestSubmissionDTO testSubmission = new TestSubmissionDTO();
            testSubmission.setStudent(student);
            testSubmission.setMaxScore(maxScore);
            testSubmission.setScore(score);
            testSubmission.setGrade(calculateGrade(score,maxScore));

            String testJson = objectMapper.writeValueAsString(testSubmission);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/submitTest"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(testJson))
                    .build();
            System.out.println(request);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 201) {
                System.out.println("Test submitted successfully.");
                switchToUserPanel();
            } else {
                System.err.println("Failed to submit test: " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Error submitting test: " + e.getMessage());
        }
    }
    private void switchToUserPanel() {
        Platform.runLater(() -> {
            try {
                Parent userPanel = FXMLLoader.load(getClass().getResource("UserPanel.fxml"));
                Scene userPanelScene = new Scene(userPanel);
                Stage stage = (Stage) nextButton.getScene().getWindow();
                stage.setScene(userPanelScene);
                stage.show();
            } catch (IOException e) {
                System.err.println("Error switching to user panel: " + e.getMessage());
            }
        });
    }
    private Long getCurrentStudentId() {
        return Main.loggedInUserId;
    }
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
