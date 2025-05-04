package com.example.desktoppet;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

public class Timer {
    private Timeline timeline;
    private int workDuration = 25 * 60;
    private int breakDuration = 5 * 60;
    private int sessions = 4;
    private int remainingTime;
    private boolean isWork = true;
    private int sessionsCompleted = 0;

    private Label timerLabel = new Label();
    private Label statusLabel = new Label("Work Time");
    private Button startPauseButton = new Button("Start");
    private Button resetButton = new Button("Reset");
    private Button nextPhaseButton = new Button();
    private TextField workField = new TextField();
    private TextField breakField = new TextField();
    private TextField sessionsField = new TextField();
    private HBox buttonBox;

    private boolean waitingForNextPhase = false;

    // Track total actual study and break time (including overtime)
    private int totalStudyTime = 0;
    private int totalBreakTime = 0;
    private int phaseStartTime = 0; // time in seconds when a phase starts

    public void changeScene(Stage stage, Scene windowScene) {
        VBox root = new VBox(5);

        buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button backButton = new Button("Back");
        Scene scene = new Scene(root, 300, 400);

        String css = this.getClass().getResource("/application.css").toExternalForm();
        if (css != null) {
            scene.getStylesheets().add(css);
        }

        stage.setTitle("Pomodoro Timer");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

        backButton.setOnAction(e -> {
//            if (timeline != null) timeline.stop();
            stage.setScene(windowScene);
        });

        VBox inputBox = new VBox(10);
        inputBox.setAlignment(Pos.CENTER);

        workField.setPromptText("Work (min)");
        breakField.setPromptText("Break (min)");
        sessionsField.setPromptText("Sessions");

        workField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        breakField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        sessionsField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));

        Label workLabel = new Label("Work:");
        Label breakLabel = new Label("Break:");
        Label sessionsLabel = new Label("Sessions:");

        HBox workHBox = new HBox(5, workLabel, workField);
        HBox breakHBox = new HBox(5, breakLabel, breakField);
        HBox sessionsHBox = new HBox(5, sessionsLabel, sessionsField);

        inputBox.getChildren().addAll(
                workHBox,
                breakHBox,
                sessionsHBox
        );

        timerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");
        statusLabel.setStyle("-fx-font-size: 20px;");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        startPauseButton.setStyle("-fx-font-size: 16px;");
        resetButton.setStyle("-fx-font-size: 16px;");
        nextPhaseButton.setStyle("-fx-font-size: 16px;");

        buttonBox.getChildren().addAll(startPauseButton, resetButton);

        startPauseButton.setOnAction(e -> toggleTimer());
        resetButton.setOnAction(e -> resetTimer(buttonBox));

        root.getChildren().clear();
        root.getChildren().addAll(backButton, statusLabel, timerLabel, buttonBox, inputBox);
        updateTimerDisplay();
    }

    private void toggleTimer() {
        if (timeline == null || timeline.getStatus() == Timeline.Status.STOPPED) {
            startTimer();
        } else {
            if (timeline.getStatus() == Timeline.Status.RUNNING) {
                timeline.pause();
                startPauseButton.setText("Resume");
            } else {
                timeline.play();
                startPauseButton.setText("Pause");
            }
        }
    }

    private void startTimer() {
        try {
            workDuration = Integer.parseInt(workField.getText()) * 60;
            breakDuration = Integer.parseInt(breakField.getText()) * 60;
            sessions = Integer.parseInt(sessionsField.getText());
        } catch (NumberFormatException e) {
            // Use default values
        }

        remainingTime = workDuration;
        isWork = true;
        sessionsCompleted = 0;
        waitingForNextPhase = false;

        totalStudyTime = 0;
        totalBreakTime = 0;
        phaseStartTime = workDuration;

        if (timeline != null) timeline.stop();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        startPauseButton.setText("Pause");
        statusLabel.setText("Work Time");
        timerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: black;");
    }

    private void updateTimer() {
        remainingTime--;
        updateTimerDisplay();

        if (remainingTime == 0 && !waitingForNextPhase) {
            waitingForNextPhase = true;
            showNextPhaseButton();
        } else if (waitingForNextPhase) {
            // Timer is negative, keep counting down and show in red
            timerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: red;");
        }
    }

    private void showNextPhaseButton() {
        HBox buttonBox = (HBox) startPauseButton.getParent();
        buttonBox.getChildren().clear();

        // Calculate overtime for this phase
        int overtime = -remainingTime; // negative remainingTime is overtime in seconds

        if (isWork) {
            totalStudyTime += phaseStartTime + overtime;
        } else {
            totalBreakTime += phaseStartTime + overtime;
        }

        // If this was last session, show "Done"
        if (isWork && sessionsCompleted + 1 >= sessions) {
            nextPhaseButton.setText("Done");
            nextPhaseButton.setOnAction(e -> showStats(buttonBox));
        } else {
            if (isWork) {
                nextPhaseButton.setText("Start Break");
                nextPhaseButton.setOnAction(e -> proceedToNextPhase(buttonBox));
            } else {
                nextPhaseButton.setText("Continue Studying");
                nextPhaseButton.setOnAction(e -> proceedToNextPhase(buttonBox));
            }
        }

        buttonBox.getChildren().add(nextPhaseButton);
    }

    private void proceedToNextPhase(HBox buttonBox) {
        waitingForNextPhase = false;
        timerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: black;");

        if (isWork) {
            sessionsCompleted++;
            remainingTime = breakDuration;
            phaseStartTime = breakDuration;
            isWork = false;
            statusLabel.setText("Break Time");
        } else {
            remainingTime = workDuration;
            phaseStartTime = workDuration;
            isWork = true;
            statusLabel.setText("Work Time");
        }
        updateTimerDisplay();

        buttonBox.getChildren().clear();
        buttonBox.getChildren().addAll(startPauseButton, resetButton);
    }

    private void showStats(HBox buttonBox) {
        timeline.stop();
        waitingForNextPhase = false;
        timerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: green;");

        int overtime = -remainingTime;
        if (isWork) {
            totalStudyTime += phaseStartTime + overtime;
        } else {
            totalBreakTime += phaseStartTime + overtime;
        }

        String studyTimeStr = formatSeconds(totalStudyTime);
        String breakTimeStr = formatSeconds(totalBreakTime);

        statusLabel.setText("All sessions completed!");
        timerLabel.setText("Study: " + studyTimeStr + "\nBreak: " + breakTimeStr);

        buttonBox.getChildren().clear();
        nextPhaseButton.setText("Close");
        buttonBox.getChildren().add(nextPhaseButton);
        nextPhaseButton.setOnAction(e -> resetTimer(buttonBox));
    }


    private String formatSeconds(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    private void updateTimerDisplay() {
        int absTime = Math.abs(remainingTime);
        int minutes = absTime / 60;
        int seconds = absTime % 60;
        String timeString = String.format("%s%02d:%02d", (remainingTime < 0 ? "-" : ""), minutes, seconds);
        timerLabel.setText(timeString);
    }

    private void resetTimer(HBox buttonBox) {
        if (timeline != null) {
            timeline.stop();
        }
        startPauseButton.setText("Start");
        isWork = true;
        sessionsCompleted = 0;
        waitingForNextPhase = false;
        remainingTime = workDuration;
        updateTimerDisplay();
        statusLabel.setText("Work Time");

        buttonBox.getChildren().clear();
        buttonBox.getChildren().addAll(startPauseButton, resetButton);

        timerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: black;");
    }

}
