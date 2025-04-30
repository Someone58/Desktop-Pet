package com.example.desktoppet;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.Random;

public class PetWindow extends Application {
    private final Random random = new Random();
    private double xSpeed = 1.5;
    private AnimationTimer animation;
    private Stage primaryStage;
    private Button pet;
    private final String[] colors = {
            "-fx-background-color: #FFC0CB; -fx-border-color: #FF69B4;",
            "-fx-background-color: #ADD8E6; -fx-border-color: #0000FF;",
            "-fx-background-color: #90EE90; -fx-border-color: #006400;"
    };
    private int colorIndex = 0;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Create visible pet button
        pet = new Button("^_^");
        updateButtonStyle();
        pet.setPrefSize(60, 40);

        // Position at bottom with padding
        double bottomY = screenBounds.getHeight() - pet.getPrefHeight() - 10;
        pet.setLayoutY(bottomY);

        // Configure window
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setAlwaysOnTop(true);

        // Click-through pane
        Pane root = new Pane(pet);
        root.setStyle("-fx-background-color: transparent;");

        // Setup random movement
        setupAnimation(screenBounds.getWidth());

        // Click handler
        pet.setOnMouseClicked(e -> {
            colorIndex = (colorIndex + 1) % colors.length;
            updateButtonStyle();
        });

        // Click-through handling
        root.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            if (!pet.getBoundsInParent().contains(e.getX(), e.getY())) {
                e.consume();
            }
        });

        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        scene.setFill(null);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateButtonStyle() {
        pet.setStyle(colors[colorIndex] +
                "-fx-border-width: 2; " +
                "-fx-font-size: 16;");
    }

    private void setupAnimation(double screenWidth) {
        animation = new AnimationTimer() {
            private long lastChange = 0;

            @Override
            public void handle(long now) {
                // Move horizontally
                double newX = pet.getLayoutX() + xSpeed;

                // Boundary check
                if (newX > screenWidth - pet.getWidth() || newX < 0) {
                    xSpeed *= -1;
                    newX = Math.max(0, Math.min(newX, screenWidth - pet.getWidth()));
                }

                pet.setLayoutX(newX);

                // Random direction changes
                if (now - lastChange > 1_000_000_000) { // Every 1 second
                    if (random.nextDouble() < 0.3) { // 30% chance to change
                        //int timecounter = 0;
                        //if (timecounter > 5) {
                        if (random.nextDouble() < 0.5){
                            xSpeed = (random.nextBoolean() ? 0.5 : -0.5) * (1 + random.nextDouble() * 2);
                        }
                        else{
                            xSpeed = 0;
                        }
                       // }
                    }
                    lastChange = now;
                }
            }
        };
        animation.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
