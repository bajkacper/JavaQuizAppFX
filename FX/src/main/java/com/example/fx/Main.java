package com.example.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
public class Main extends Application {

    private static Stage primaryStage;
    public static Long loggedInUserId=1L;
    @Override
    public void start(Stage primaryStage) throws IOException {
        Main.primaryStage = primaryStage;
        primaryStage.setTitle("JavaTEST");

        Parent root = FXMLLoader.load(getClass().getResource("/com/example/fx/Login.fxml"));
        Scene scene = new Scene(root, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

    public static void switchToAdminPanel() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/com/example/fx/AdminPanel.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void switchToUserPanel() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/com/example/fx/UserPanel.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void switchToLoginPanel() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/com/example/fx/Login.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
