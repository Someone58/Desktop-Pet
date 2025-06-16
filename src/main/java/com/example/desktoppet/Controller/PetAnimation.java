package com.example.desktoppet.Controller;

import com.example.desktoppet.Model.PetData;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Random;

public class PetAnimation {
    PetData petController;
    Notification notification;
    private final Random random = new Random();
    private double xSpeed = 1.5;
    private double speedMultiplier = 1;
    private double activity = 0.5;
    private AnimationTimer animation;
    Button pet = new Button();
    Pane root = new Pane(pet);

    String imagePath = "/Pets/Shark_Idle1.png";
    Image image = new Image(getClass().getResource(imagePath).toExternalForm());
    ImageView petImage = new ImageView(image);

    double bottomY;

    String petName = "Shark";
    ImageAnimation imageAnimation;

    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    double screenWidth = screenBounds.getWidth();


    public PetAnimation(PetData petController) {
        this.petController = petController;

        imageAnimation = new ImageAnimation(petController, petName, 4);
        notification = petController.getNotification();
    }

    public void setupStage() {
        petImage = petController.getPetImage();
        petImage.setFitHeight(petController.getPetSize());
        petImage.setFitWidth(petController.getPetSize());

        pet.setBackground(null);

        Stage primaryStage = petController.getPrimaryStage();
        Image idle = petController.getSharkIdle();

        pet.setGraphic(petImage);
        imageAnimation.play();
        imageAnimation.setFps(5);

        pet.setPrefHeight(40);
        bottomY = screenBounds.getHeight() - pet.getPrefHeight() - 40;
        pet.setLayoutY(bottomY);

        petImage.setImage(idle);

        root.setStyle("-fx-background-color: transparent;");
        setupAnimation();

        root.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            if (!pet.getBoundsInParent().contains(e.getX(), e.getY())) {
                e.consume();
            }
        });

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setAlwaysOnTop(true);

        ImageView notificationIcon = notification.getNotification();
        root.getChildren().add(notificationIcon);

        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        scene.setFill(null);
        primaryStage.setScene(scene);
        primaryStage.show();

        pet.setPadding(Insets.EMPTY);
    }

    public void setupPet(String petName) {
        imageAnimation.remove();
        imageAnimation = new ImageAnimation(petController, petName, 4);
        imageAnimation.play();
        if (xSpeed == 0){
            imageAnimation.playIdle();
        }
        imageAnimation.setFps(4);
    }

    private void setupAnimation() {
        animation = new AnimationTimer() {
            private long lastChange = 0;

            @Override
            public void handle(long now) {
                // Move horizontally
                double newX = pet.getLayoutX() + xSpeed;
                boolean facingRight = xSpeed > 0;

                notification.setSize(petController.getPetSize() / 6.0);
                double notificationWidth = notification.getSize();

                double middlePos = pet.getLayoutX() + petController.getPetSize() / 2 - notificationWidth / 2;
                if (xSpeed < 0 || xSpeed == 0 && facingRight) {
                    notification.setPos(
                            middlePos - petController.getPetSize() / 2.3,
                            pet.getLayoutY() + petController.getPetSize() / 5
                    );
                    petImage.setScaleX(1.0);
                } else if (xSpeed > 0 || xSpeed == 0 && !facingRight) {
                    notification.setPos(
                            middlePos + petController.getPetSize() / 2.3,
                            pet.getLayoutY() + petController.getPetSize() / 5
                    );
                    petImage.setScaleX(-1.0);
                }

                // Boundary check
                if (newX > screenWidth - pet.getWidth() || newX < 0) {
                    xSpeed *= -1;
                    imageAnimation.setFps(Math.abs(xSpeed * 2.5));
                    if (newX > screenWidth - pet.getWidth()) {
                        petImage.setScaleX(1.0);
                    } else {
                        petImage.setScaleX(-1.0);
                    }

                    newX = Math.max(0, Math.min(newX, screenWidth - pet.getWidth()));
                }

                pet.setLayoutX(newX);

                // Random direction changes
                if (now - lastChange > 1_000_000_000) { // Every 1 second
                    if (random.nextDouble() < 0.3) { // 30% chance to change
                        if (random.nextDouble() < activity) {
                            imageAnimation.playAnimation();
                            xSpeed = (random.nextBoolean() ? 0.5 : -0.5) * (1 + random.nextDouble() * 2);
                            xSpeed *= speedMultiplier;
                            imageAnimation.setFps(Math.abs(xSpeed * 2.5));
                            if (xSpeed < 0) {
                                petImage.setScaleX(1.0);
                            } else if (xSpeed > 0) {
                                petImage.setScaleX(-1.0);
                            }
                        } else {
                            xSpeed = 0;
                            imageAnimation.setFps(4);
                            imageAnimation.playIdle();
                        }
                    }
                    lastChange = now;
                }
            }
        };
        animation.start();
    }

    public void setxSpeed(double speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    ;

    public void setActivity(double activity) {
        this.activity = activity;
    }

    public void setSizeHeight(double size, double heightPercent) {
        petImage.setFitHeight(size);
        petImage.setFitWidth(size);

        double margin = size * 0.2; // 10% of the size as margin

        double minY = screenBounds.getMinY() - margin; // Allow going above the top
        double maxY = screenBounds.getMaxY() - size + margin; // Allow going below the bottom

        double availableHeight = (screenBounds.getHeight() - size) + 2 * margin;
        double offset = (availableHeight * heightPercent) / 100.0;
        double desiredY = minY + offset;

        double clampedY = Math.max(minY, Math.min(desiredY, maxY));
        pet.setLayoutY(clampedY);
    }


}
