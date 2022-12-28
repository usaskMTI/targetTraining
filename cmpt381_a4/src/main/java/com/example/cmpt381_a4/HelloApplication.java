package com.example.cmpt381_a4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MainUI mainui = new MainUI();
        Scene scene = new Scene(mainui, 800, 800);
        mainui.passScene(scene);
        stage.setTitle("Target Trainer Demo");
        stage.setScene(scene);
        stage.show();
        mainui.requestFocus();
    }

    public static void main(String[] args) {
        launch();
    }
}