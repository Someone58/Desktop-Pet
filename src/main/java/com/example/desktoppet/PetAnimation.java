package com.example.desktoppet;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.stage.StageStyle;

import java.util.Random;

public class PetAnimation {
    private final Random random = new Random();
    private double xSpeed = 1.5;
    private AnimationTimer animation;
    Button pet = new Button();
    Rectangle2D screeNBounds;
    Pane root = new Pane(pet);
    ImageView petImage = new ImageView();

    public PetAnimation(Rectangle2D screenBounds, double xSpeed) {
        this.xSpeed = xSpeed;
        this.screeNBounds = screenBounds;
        petImage.setFitHeight(100);
        petImage.setFitWidth(100);
        pet.setBackground(null);
    }

    public void setupStage(Stage primaryStage, Image idle) {
        System.out.println("button triggered");
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        pet.setPrefSize(60, 40);
        double bottomY = screenBounds.getHeight() - pet.getPrefHeight() - 40;
        pet.setLayoutY(bottomY);

        petImage.setImage(idle);
        pet.setGraphic(petImage);

        root.setStyle("-fx-background-color: transparent;");
        setupAnimation(screenBounds.getWidth());

        root.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            if (!pet.getBoundsInParent().contains(e.getX(), e.getY())) {
                e.consume();
            }
        });

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setAlwaysOnTop(true);

        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        scene.setFill(null);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void setupPet(Image petIdle) {
        petImage.setImage(petIdle);
    }

    private void setupAnimation(double screenWidth) {
        animation = new AnimationTimer() {
            private long lastChange = 0;

            @Override
            public void handle(long now) {
                // Move horizontally
                double newX = pet.getLayoutX() + xSpeed;
                if (xSpeed < 0){
                    petImage.setScaleX(1.0);
                } else if (xSpeed > 0){
                    petImage.setScaleX(-1.0);
                }

                // Boundary check
                if (newX > screenWidth - pet.getWidth() || newX < 0) {
                    xSpeed *= -1;
                    if (newX > screenWidth - pet.getWidth()){
                        petImage.setScaleX(1.0);
                    } else{
                        petImage.setScaleX(-1.0);
                    }

                    newX = Math.max(0, Math.min(newX, screenWidth - pet.getWidth()));
                }

                pet.setLayoutX(newX);

                // Random direction changes
                if (now - lastChange > 1_000_000_000) { // Every 1 second
                    if (random.nextDouble() < 0.3) { // 30% chance to change
                        if (random.nextDouble() < 0.5){
                            xSpeed = (random.nextBoolean() ? 0.5 : -0.5) * (1 + random.nextDouble() * 2);
                            if (xSpeed < 0){
                                petImage.setScaleX(1.0);
                            } else if (xSpeed > 0){
                                petImage.setScaleX(-1.0);
                            }
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

}
