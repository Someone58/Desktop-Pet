package com.example.desktoppet;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Window {
    public Window(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root, 100, 600);

        String css = this.getClass().getResource("/application.css").toExternalForm();

        if (css != null) {
            scene.getStylesheets().add(css);
        } else {
            System.err.println("Warning: CSS resource file not found");
        }

        stage.setTitle("Connect 4");
        stage.setScene(scene);
        stage.show();
    }
}