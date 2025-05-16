package com.example.desktoppet;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MiniTimerWindow {
    private static Stage miniStage = null; // Only one instance allowed
    private Notification notification;

    public MiniTimerWindow(Timer timer, PetController petController) {
        if (miniStage != null && miniStage.isShowing()) {
            miniStage.toFront();
            return;
        }
        this.notification = petController.getNotification();
        petController.setMinitimeropened(true);

        miniStage = new Stage();
        miniStage.setTitle("Mini Timer");
        miniStage.setAlwaysOnTop(true);
        miniStage.setResizable(false);

        VBox miniRoot = new VBox(10);
        miniRoot.setAlignment(Pos.CENTER);

        Label miniStatusLabel = new Label();
        miniStatusLabel.textProperty().bind(timer.statusTextProperty());
        miniStatusLabel.setStyle("-fx-font-size: 20px;");

        Label miniSessionLabel = new Label();
        miniSessionLabel.textProperty().bind(timer.sessionLabel.textProperty());
        miniSessionLabel.setStyle("-fx-font-size: 16px;");

        Label miniTimerLabel = new Label();
        miniTimerLabel.textProperty().bind(timer.timerTextProperty());
        miniTimerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");

        Button miniStartPause = new Button();
        miniStartPause.textProperty().bind(timer.startPauseTextProperty());
        miniStartPause.setOnAction(e -> timer.startPauseButton.fire());
        miniStartPause.setStyle("-fx-font-size: 16px;");

        Button miniReset = new Button("Reset");
        miniReset.setOnAction(e -> timer.resetButton.fire());
        miniReset.setStyle("-fx-font-size: 16px;");

        Button miniNextPhase = new Button();
        miniNextPhase.textProperty().bind(timer.nextPhaseTextProperty());
        miniNextPhase.setOnAction(e -> timer.nextPhaseButton.fire());
        miniNextPhase.setStyle("-fx-font-size: 16px;");

        HBox miniButtonBox = new HBox(10);
        miniButtonBox.setAlignment(Pos.CENTER);

        miniRoot.getChildren().addAll(miniStatusLabel, miniSessionLabel, miniTimerLabel, miniButtonBox);

        Scene miniScene = new Scene(miniRoot, 320, 220);
        miniStage.setScene(miniScene);

        Runnable updateButtons = () -> {
            Timer.TimerButtonState state = timer.getButtonState();
            miniButtonBox.getChildren().clear();
            switch (state) {
                case START_PAUSE_AND_RESET:
                    miniButtonBox.getChildren().addAll(miniStartPause, miniReset);
                    break;
                case NEXT_PHASE_ONLY:
                case CLOSE_ONLY:
                    miniButtonBox.getChildren().add(miniNextPhase);
                    break;
            }
        };
        
        Runnable updateTimerLabelColor = () -> {
            String timerText = miniTimerLabel.getText();
            boolean isStats = timerText.contains("Study:") && timerText.contains("Break:");
            boolean isNegative = timerText.startsWith("-");
            if (isStats) {
                miniTimerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: green;");
            } else if (isNegative) {
                miniTimerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: red;");
            } else {
                miniTimerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: black;");
            }
        };

        timer.setOnUpdate(() -> {
            updateButtons.run();
            updateTimerLabelColor.run();
        });

        miniTimerLabel.textProperty().addListener((obs, oldVal, newVal) -> updateTimerLabelColor.run());

        updateButtons.run();
        updateTimerLabelColor.run();

        miniStage.setOnCloseRequest(e -> {
                    miniStage = null;
                    petController.setMinitimeropened(false);
                });

        miniStage.show();
    }
}
