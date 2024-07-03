package com.example.fx;

import com.example.fx.entity.Question;
import com.example.fx.entity.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class UserController {
    @FXML
    private Button logoutButton;
    @FXML
    private TableView<Test> testResultsTable;

    @FXML
    private TableColumn<Test, Long> idColumn;

    @FXML
    private TableColumn<Test, Integer> scoreColumn;

    @FXML
    private TableColumn<Test, Integer> maxScoreColumn;

    @FXML
    private TableColumn<Test, Integer> shotColumn;
    @FXML
    private TableColumn<Test, String> gradeColumn;
    @FXML
    private TableColumn<Test, LocalDate> dataColumn;

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final String baseUrl = "http://localhost:8080/user";

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private Map<Long, Long> userAnswers = new HashMap<>();
    private Timer timer;
    private int timeRemaining = 15 * 60*4;

    @FXML
    private Label timerLabel;
    @FXML
    private Label questionLabel;
    @FXML
    private VBox answersBox;
    @FXML
    private Button nextButton;
    @FXML
    private Label currentDateLabel;

    @FXML
    private Hyperlink javaCourseLink;
    @FXML
    public void initialize() {
        setupTestResultsTable();
        refreshResults();
        setCurrentDateLabel();
        javaCourseLink.setText("PROGRAMOWANIE W JĘZYKU JAVA (dr hab. inż. Jan PROKOP, prof. PRz)");

    }
    private void setCurrentDateLabel() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = currentDate.format(formatter);
        currentDateLabel.setText("Zaliczenie w dniu " + formattedDate);
    }
    private void setupTestResultsTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        maxScoreColumn.setCellValueFactory(new PropertyValueFactory<>("maxScore"));
        shotColumn.setCellValueFactory(new PropertyValueFactory<>("shot"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        dataColumn.setCellFactory(column -> new TableCell<Test, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });
    }
    @FXML
    private void logout() {
        Main.loggedInUserId = null;
        try {
            Main.switchToLoginPanel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void startTest() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/randomQuestions/10"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                questions = objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, Question.class));
                openTestScreen(questions);
            } else {
                showError("Nie udało się pobrać pytań: " + response.body());
            }
        } catch (Exception e) {
            showError("Błąd podczas rozpoczynania testu: " + e.getMessage());
        }
    }

    private void openTestScreen(List<Question> questions) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TestScreen.fxml"));
            Parent testScreen = loader.load();

            TestController testController = loader.getController();
            testController.setQuestions(questions);

            Scene currentScene = timerLabel.getScene();
            currentScene.setRoot(testScreen);
        } catch (IOException e) {
            showError("Błąd podczas otwierania ekranu testu: " + e.getMessage());
        }
    }
    @FXML
    private void openJavaCourseLink() {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI("http://web.prz.edu.pl/"));
            } catch (IOException | URISyntaxException e) {
                showError("Błąd podczas otwierania linku: " + e.getMessage());
            }
        }
    }
    @FXML
    public void refreshResults() {
        try {
            Long studentId = getCurrentStudentId();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/tests/" + studentId))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                List<Test> tests = objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, Test.class));
                ObservableList<Test> testResults = FXCollections.observableArrayList(tests);
                testResultsTable.setItems(testResults);
            } else {
                showError("Nie udało się pobrać wyników: " + response.body());
            }
        } catch (Exception e) {
            showError("Błąd podczas odświeżania wyników: " + e.getMessage());
        }
    }

    private void showError(String message) {
        System.err.println(message);
    }
    private Long getCurrentStudentId() {
        return Main.loggedInUserId;
    }
}
