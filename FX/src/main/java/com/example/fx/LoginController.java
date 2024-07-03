package com.example.fx;

import com.example.fx.entity.Student;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoginController {
    private static final String API_BASE_URL = "http://localhost:8080/api/auth";

    @FXML
    private TextField indexField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField registerIndexField;
    @FXML
    private TextField registerNameField;
    @FXML
    private TextField registerLastNameField;
    @FXML
    private PasswordField registerPasswordField;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private Label registerMessageLabel;
    @FXML
    private Button loginButton;

    @FXML
    public void initialize() {
        indexField.setOnKeyPressed(this::handleEnterKey);
        passwordField.setOnKeyPressed(this::handleEnterKey);
    }

    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleLogin();
        }
    }

    public void handleLogin() {
        String index = indexField.getText();
        String password = passwordField.getText();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"index\":\"" + index + "\",\"password\":\"" + password + "\"}"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    int statusCode = response.statusCode();
                    if (statusCode == 200) {
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            Student student = mapper.readValue(response.body(), Student.class);
                            Main.loggedInUserId = student.getId();
                            System.err.println(Main.loggedInUserId);
                            return student;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    } else {
                        return null;
                    }
                })
                .thenAccept(student -> {
                    if (student != null) {
                        System.out.println("Zalogowano pomyślnie!");
                        Platform.runLater(() -> loginMessageLabel.setText("Zalogowano pomyślnie!"));
                        if (student.getIsAdmin()) {
                            Platform.runLater(() -> {
                                try {
                                    Main.switchToAdminPanel();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        } else {
                            Platform.runLater(() -> {
                                try {
                                    Main.switchToUserPanel();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    } else {
                        System.out.println("Błąd logowania. Spróbuj ponownie.");
                        Platform.runLater(() -> loginMessageLabel.setText("Błąd logowania. Spróbuj ponownie."));
                    }
                })
                .exceptionally(e -> {
                    System.err.println("Wystąpił błąd podczas logowania: " + e.getMessage());
                    Platform.runLater(() -> loginMessageLabel.setText("Wystąpił błąd podczas logowania"));
                    return null;
                });
    }

    public void handleRegister() {
        String index = registerIndexField.getText();
        String name = registerNameField.getText();
        String lastName = registerLastNameField.getText();
        String password = registerPasswordField.getText();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"index\":\"" + index + "\",\"name\":\"" + name + "\",\"lastName\":\"" + lastName + "\",\"password\":\"" + password + "\"}"))
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::statusCode)
                .thenAccept(statusCode -> {
                    if (statusCode == 201) {
                        System.out.println("Zarejestrowano pomyślnie!");
                        Platform.runLater(() -> registerMessageLabel.setText("Zarejestrowano pomyślnie!"));
                    } else if (statusCode == 409) {
                        System.out.println("Użytkownik o podanym indeksie już istnieje.");
                        Platform.runLater(() -> registerMessageLabel.setText("Użytkownik o podanym indeksie już istnieje."));
                    } else {
                        System.out.println("Błąd rejestracji. Spróbuj ponownie.");
                        Platform.runLater(() -> registerMessageLabel.setText("Błąd rejestracji. Spróbuj ponownie."));
                    }
                })
                .exceptionally(e -> {
                    System.err.println("Wystąpił błąd podczas rejestracji: " + e.getMessage());
                    Platform.runLater(() -> registerMessageLabel.setText("Wystąpił błąd podczas rejestracji"));
                    return null;
                });
    }
}
