package com.example.fx;

import com.example.fx.entity.Student;
import com.example.fx.entity.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.fx.entity.Answer;
import com.example.fx.entity.Question;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.ByteArrayInputStream;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class AdminController {

    @FXML
    private ListView<Question> questionListView;
    @FXML
    private ImageView questionImageView;
    @FXML
    private Label selectedImageLabel;
    @FXML
    private ListView<Answer> answerListView;

    @FXML
    private TextArea questionTextArea;

    @FXML
    private TextField answerTextField;

    @FXML
    private CheckBox isCorrectCheckBox;

    @FXML
    private Label questionMessageLabel;

    @FXML
    private VBox answersBox;

    @FXML
    private Button logoutButton;
    @FXML
    private TableView<Student> userTableView;
    @FXML
    private TableColumn<Student, String> indexColumn;
    @FXML
    private TableColumn<Student, String> nameColumn;
    @FXML
    private TableColumn<Student, String> lastNameColumn;
    @FXML
    private TableColumn<Student, Boolean> adminColumn;
    @FXML
    private TableColumn<Student, Long> idColumn;
    @FXML
    private Label userMessageLabel;
    @FXML
    private TableView<Test> testTableView;
    @FXML
    private TableColumn<Test, Long> testIdColumn;
    @FXML
    private TableColumn<Test, Integer> testMaxScoreColumn;
    @FXML
    private TableColumn<Test, Integer> testScoreColumn;
    @FXML
    private TableColumn<Test, Integer> testShotColumn;
    @FXML
    private TableColumn<Test, String> testStudentIndexColumn;
    @FXML
    private TableColumn<Test, String> gradeColumn;
    @FXML
    private TableColumn<Test, LocalDate> dataColumn;

    @FXML
    private Label testMessageLabel;

    private List<Question> questions = new ArrayList<>();
    private List<Answer> answers = new ArrayList<>();
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String baseUrl = "http://localhost:8080/admin";
    private FileChooser imageFileChooser = new FileChooser();
    private File selectedImageFile;
    @FXML
    private TableColumn<Test, Integer> maxScoreColumn;

    @FXML
    private TableColumn<Test, Integer> scoreColumn;

    @FXML
    private TableColumn<Test, Integer> shotColumn;

    @FXML
    private TableColumn<Test, String> studentIndexColumn;

    @FXML
    public void initialize() {
        objectMapper.registerModule(new JavaTimeModule());
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("index"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        adminColumn.setCellValueFactory(new PropertyValueFactory<>("admin"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        testIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        testMaxScoreColumn.setCellValueFactory(new PropertyValueFactory<>("maxScore"));
        testScoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        testShotColumn.setCellValueFactory(new PropertyValueFactory<>("shot"));
        testStudentIndexColumn.setCellValueFactory(new PropertyValueFactory<>("studentIndex"));
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
        showUsers();
        showTests();
        imageFileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        showQuestions();
        questionListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayQuestion(newValue);
                displayAnswers(newValue);
            }
        });
        answerListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayAnswer(newValue);
            }
        });
        questionListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayQuestion(newValue);
                displayAnswers(newValue);
                displayQuestionImage(newValue);
            }
        });

    }
    @FXML
    public void showTests() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/tests"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Test[] testsArray = objectMapper.readValue(response.body(), Test[].class);
                ObservableList<Test> testsList = FXCollections.observableArrayList(testsArray);
                testTableView.setItems(testsList);
                testMessageLabel.setText("Pobrano listę testów.");
            } else {
                testMessageLabel.setText("Błąd podczas pobierania testów: " + response.body());
            }
        } catch (Exception e) {
            testMessageLabel.setText("Błąd podczas pobierania testów: " + e.getMessage());
        }
    }


    private void displayQuestionImage(Question question) {
        if (question.getImage() != null) {
            byte[] imageBytes = Base64.getDecoder().decode(question.getImage());
            Image image = new Image(new ByteArrayInputStream(imageBytes));
            questionImageView.setImage(image);
        } else {
            questionImageView.setImage(null);
        }
    }

    @FXML
    public void showUsers() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/users"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Student[] usersArray = objectMapper.readValue(response.body(), Student[].class);
                ObservableList<Student> usersList = FXCollections.observableArrayList(usersArray);
                userTableView.setItems(usersList);
                userMessageLabel.setText("Pobrano listę użytkowników.");
            } else {
                userMessageLabel.setText("Błąd podczas pobierania użytkowników: " + response.body());
            }
        } catch (Exception e) {
            userMessageLabel.setText("Błąd podczas pobierania użytkowników: " + e.getMessage());
        }
    }
    @FXML
    public void showQuestions() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/questions"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Question[] questionsArray = objectMapper.readValue(response.body(), Question[].class);
                questions = List.of(questionsArray);
                questionListView.getItems().setAll(questions);
                questionTextArea.clear();
                questionImageView.setImage(null);
                answerTextField.clear();
                //tu
                questionMessageLabel.setText(null);
                isCorrectCheckBox.setSelected(false);
                answers.clear();
                answerListView.getItems().clear();
            }
        } catch (Exception e) {
            questionMessageLabel.setText("Błąd podczas pobierania pytań: " + e.getMessage());
        }
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
    public void addQuestion() {
        try {
            String questionText = questionTextArea.getText();
            if (!questionText.isEmpty()) {
                Question question = new Question();
                question.setQuestion(questionText);
                question.setAnswers(new ArrayList<>(answers));

                if (selectedImageFile != null) {
                    byte[] imageBytes = new byte[(int) selectedImageFile.length()];
                    FileInputStream fis = new FileInputStream(selectedImageFile);
                    fis.read(imageBytes);
                    fis.close();
                    String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
                    question.setImage(encodedImage);
                }

                for (Answer answer : answers) {
                    answer.setQuestion(question);
                }

                String requestBody = objectMapper.writeValueAsString(question);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(baseUrl + "/addQuestion"))
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .header("Content-Type", "application/json")
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 201) {
                    showQuestions();

                    questionTextArea.clear();
                    answers.clear();
                    answerListView.getItems().clear();
                    questionImageView.setImage(null);
                    questionMessageLabel.setText("Pytanie dodane pomyślnie.");
                } else {
                    questionMessageLabel.setText("Błąd podczas dodawania pytania: " + response.body());
                }
            } else {
                questionMessageLabel.setText("Treść pytania nie może być pusta.");
            }

        } catch (Exception e) {
            questionMessageLabel.setText("Błąd podczas dodawania pytania: " + e.getMessage());
        }
    }
    @FXML
    public void updateQuestion() {
        try {
            Question selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
            if (selectedQuestion != null) {
                selectedQuestion.setQuestion(questionTextArea.getText());

                List<Answer> updatedAnswers = new ArrayList<>(selectedQuestion.getAnswers());

                String newAnswerText = answerTextField.getText();
                boolean newIsCorrect = isCorrectCheckBox.isSelected();

                Answer selectedAnswer = answerListView.getSelectionModel().getSelectedItem();
                if (selectedAnswer != null) {
                    selectedAnswer.setAnswer(newAnswerText);
                    selectedAnswer.setCorrect(newIsCorrect);
                } else {
                    Answer newAnswer = new Answer(null, newAnswerText, newIsCorrect, selectedQuestion);
                    addAnswerToDB(newAnswer, selectedQuestion);
                    updatedAnswers.add(newAnswer);
                }

                selectedQuestion.setAnswers(updatedAnswers);
                if (selectedImageFile != null) {
                    byte[] imageBytes = new byte[(int) selectedImageFile.length()];
                    FileInputStream fis = new FileInputStream(selectedImageFile);
                    fis.read(imageBytes);
                    fis.close();
                    String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
                    selectedQuestion.setImage(encodedImage);
                }else if (questionImageView.getImage() == null) {
                    selectedQuestion.setImage(null);
                }
                String requestBody = objectMapper.writeValueAsString(selectedQuestion);
                System.err.println(requestBody);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(baseUrl + "/updateQuestion/" + selectedQuestion.getId()))
                        .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                        .header("Content-Type", "application/json")
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    questionMessageLabel.setText("Pytanie zaktualizowane pomyślnie.");

                    showQuestions();
                    questionImageView.setImage(null);
                } else {
                    questionMessageLabel.setText("Błąd podczas aktualizacji pytania: " + response.body());
                }
            } else {
                questionMessageLabel.setText("Proszę wybrać pytanie do aktualizacji.");
            }
        } catch (Exception e) {
            questionMessageLabel.setText("Błąd podczas aktualizacji pytania: " + e.getMessage());
        }
    }

    @FXML
    public void deleteQuestion() {
        try {
            Question selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
            if (selectedQuestion != null) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(baseUrl + "/deleteQuestion/" + selectedQuestion.getId()))
                        .DELETE()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    showQuestions();
                    questionImageView.setImage(null);
                    questionMessageLabel.setText("Pytanie usunięte pomyślnie.");
                } else {
                    questionMessageLabel.setText("Błąd podczas usuwania pytania: " + response.body());
                }
            } else {
                questionMessageLabel.setText("Proszę wybrać pytanie do usunięcia.");
            }
        } catch (Exception e) {
            questionMessageLabel.setText("Błąd podczas usuwania pytania: " + e.getMessage());
        }
    }
@FXML
public void addAnswer() {
    String answerText = answerTextField.getText();
    Question selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
    boolean isCorrect = isCorrectCheckBox.isSelected();
    if (!answerText.isEmpty() && selectedQuestion != null) {
        Answer answer = new Answer(null, answerText, isCorrect, selectedQuestion);
        addAnswerToDB(answer, selectedQuestion);

    }else{
        Answer answer = new Answer(null, answerText, isCorrect, null);
        answers.add(answer);
        answerListView.getItems().add(answer);
        answerTextField.clear();
        isCorrectCheckBox.setSelected(false);
    }
}

    public void addAnswerToDB(Answer answer, Question selectedQuestion) {
        try {
            String answerText = answer.getAnswer();
            boolean isCorrect = answer.isCorrect();

            if (selectedQuestion != null && !answerText.isEmpty()) {
                String requestBody = objectMapper.writeValueAsString(answer);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(baseUrl + "/postAnswer/" + selectedQuestion.getId()))
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .header("Content-Type", "application/json")
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    answers.add(answer);
                    answerListView.getItems().add(answer);
                    answerTextField.clear();
                    //dodane
                    answerListView.refresh();
                    isCorrectCheckBox.setSelected(false);
                    System.out.println("Odpowiedź dodana pomyślnie.");
                } else {
                    System.out.println("Błąd podczas dodawania odpowiedzi: " + response.body());
                }
            } else {
                System.out.println("Proszę wybrać pytanie i wprowadzić odpowiedź.");
            }
        } catch (Exception e) {
            System.out.println("Błąd podczas dodawania odpowiedzi: " + e.getMessage());
        }
    }
    @FXML
    public void deleteAnswer() {
        Answer selectedAnswer = answerListView.getSelectionModel().getSelectedItem();
        if (selectedAnswer != null) {
            try {
//                if(selectedAnswer.getId()==null){
//                    answers.remove(selectedAnswer);
//                    answerListView.getItems().remove(selectedAnswer);
//                    answerListView.refresh();
//                    return;
//                }
                answers.remove(selectedAnswer);
                answerListView.getItems().remove(selectedAnswer);
                answerListView.refresh();
                Long answerId = selectedAnswer.getId();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(baseUrl + "/deleteAnswer/" + answerId))
                        .DELETE()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    questionMessageLabel.setText("Odpowiedź usunięta pomyślnie.");

                    updateQuestionAfterAnswerDeletion(selectedAnswer);
                } else {
                    System.out.println("Błąd podczas usuwania odpowiedzi: " + response.body());
                }
            } catch (Exception e) {
                System.out.println("Błąd podczas usuwania odpowiedzi: " + e.getMessage());
            }
        } else {
            questionMessageLabel.setText("Proszę wybrać odpowiedź do usunięcia.");
        }
    }
@FXML
private void selectImage() {
    selectedImageFile = imageFileChooser.showOpenDialog(null);
    if (selectedImageFile != null) {
        Image image = new Image(selectedImageFile.toURI().toString());
        questionImageView.setImage(image);
    }
}
    @FXML
    private void deleteImage() {
        questionImageView.setImage(null);
        selectedImageFile = null;
    }

    private void updateQuestionAfterAnswerDeletion(Answer deletedAnswer) {
        Question selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            selectedQuestion.getAnswers().remove(deletedAnswer);
            try {
                String requestBody = objectMapper.writeValueAsString(selectedQuestion);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(baseUrl + "/updateQuestion/" + selectedQuestion.getId()))
                        .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                        .header("Content-Type", "application/json")
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    questionMessageLabel.setText("Odpowiedź usunięta z pytania i zaktualizowana pomyślnie.");
                } else {
                    questionMessageLabel.setText("Błąd podczas aktualizacji pytania po usunięciu odpowiedzi: " + response.body());
                }
            } catch (Exception e) {
                questionMessageLabel.setText("Błąd podczas aktualizacji pytania po usunięciu odpowiedzi: " + e.getMessage());
            }
        }
    }

    private void displayQuestion(Question question) {
        if (question != null) {
            questionTextArea.setText(question.getQuestion());
            displayQuestionImage(question);
        }
    }

    private void displayAnswers(Question question) {
        if (question != null) {
            answerListView.getItems().setAll(question.getAnswers());
        }
    }

    private void displayAnswer(Answer answer) {
        if (answer != null) {
            answerTextField.setText(answer.getAnswer());
            isCorrectCheckBox.setSelected(answer.isCorrect());
        }
    }
}
