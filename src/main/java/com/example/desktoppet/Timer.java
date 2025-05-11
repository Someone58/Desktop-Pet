// com/example/desktoppet/Timer.java
package com.example.desktoppet;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

    Label timerLabel = new Label();
    Label statusLabel = new Label("Work Time");
    Button startPauseButton = new Button("Start");
    Button resetButton = new Button("Reset");
    Button nextPhaseButton = new Button();
    private TextField workField = new TextField();
    private TextField breakField = new TextField();
    private TextField sessionsField = new TextField();
    private HBox buttonBox;
    Button miniTimerButton = new Button("Mini Timer");

    private Runnable onUpdate = null;

    private final StringProperty timerText = new SimpleStringProperty("25:00");
    private final StringProperty statusText = new SimpleStringProperty("Work Time");
    private final BooleanProperty waitingForNextPhaseProperty = new SimpleBooleanProperty(false);

    private boolean waitingForNextPhase = false;

    // Track total actual study and break time (including overtime)
    private int totalStudyTime = 0;
    private int totalBreakTime = 0;
    private int phaseStartTime = 0; // time in seconds when a phase starts

    public Timer() {
        timeline = new Timeline(); // Initialize empty timeline
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

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
            stage.setScene(windowScene);
            stage.setTitle("Apps");
        });

        VBox inputBox = new VBox(10);
        inputBox.setAlignment(Pos.CENTER);

        workField.setPromptText("25 (min)");
        breakField.setPromptText("5 (min)");
        sessionsField.setPromptText("4 Sessions");

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

        buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        startPauseButton.setStyle("-fx-font-size: 16px;");
        resetButton.setStyle("-fx-font-size: 16px;");
        nextPhaseButton.setStyle("-fx-font-size: 16px;");

        buttonBox.getChildren().addAll(startPauseButton, resetButton);

        startPauseButton.setOnAction(e -> toggleTimer());
        resetButton.setOnAction(e -> resetTimer(buttonBox));
        miniTimerButton.setOnAction(e -> new MiniTimerWindow(this, stage));

        root.getChildren().clear();
        root.getChildren().addAll(backButton, miniTimerButton, statusLabel, timerLabel, buttonBox, inputBox);
        updateTimerDisplay();
        if (onUpdate != null) onUpdate.run();
    }

    void toggleTimer() {
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
        if (onUpdate != null) onUpdate.run();
    }

    private void startTimer() {
        if (timeline != null) timeline.stop();

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
        waitingForNextPhaseProperty.set(false);

        totalStudyTime = 0;
        totalBreakTime = 0;
        phaseStartTime = workDuration;

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        startPauseButton.setText("Pause");
        statusLabel.setText("Work Time");
        timerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: black;");

        buttonBox.getChildren().setAll(startPauseButton, resetButton);
        if (onUpdate != null) onUpdate.run();
    }

    private void updateTimer() {
        remainingTime--;
        updateTimerDisplay();

        if (remainingTime == 0 && !waitingForNextPhase) {
            waitingForNextPhase = true;
            waitingForNextPhaseProperty.set(true);
            showNextPhaseButton();
        } else if (waitingForNextPhase) {
            timerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: red;");
        }
        if (onUpdate != null) onUpdate.run();
    }

    private void showNextPhaseButton() {
        HBox box = (HBox) startPauseButton.getParent();
        box.getChildren().clear();

        int overtime = -remainingTime;
        if (isWork) {
            totalStudyTime += phaseStartTime + overtime;
        } else {
            totalBreakTime += phaseStartTime + overtime;
        }

        if (isWork && sessionsCompleted + 1 >= sessions) {
            nextPhaseButton.setText("Done");
            nextPhaseButton.setOnAction(e -> showStats(box));
        } else {
            if (isWork) {
                nextPhaseButton.setText("Start Break");
                nextPhaseButton.setOnAction(e -> proceedToNextPhase(box));
            } else {
                nextPhaseButton.setText("Continue Studying");
                nextPhaseButton.setOnAction(e -> proceedToNextPhase(box));
            }
        }

        box.getChildren().add(nextPhaseButton);
        if (onUpdate != null) onUpdate.run();
    }

    private void proceedToNextPhase(HBox box) {
        waitingForNextPhase = false;
        waitingForNextPhaseProperty.set(false);
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

        box.getChildren().setAll(startPauseButton, resetButton);
        if (onUpdate != null) onUpdate.run();
    }

    private void showStats(HBox box) {
        timeline.stop();
        waitingForNextPhase = false;
        waitingForNextPhaseProperty.set(false);
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

        box.getChildren().clear();
        nextPhaseButton.setText("Close");
        nextPhaseButton.setOnAction(e -> resetTimer(box));
        box.getChildren().add(nextPhaseButton);
        if (onUpdate != null) onUpdate.run();
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
        if (onUpdate != null) onUpdate.run();
    }

    private void resetTimer(HBox box) {
        if (timeline != null) {
            timeline.stop();
        }
        startPauseButton.setText("Start");
        isWork = true;
        sessionsCompleted = 0;
        waitingForNextPhase = false;
        waitingForNextPhaseProperty.set(false);
        remainingTime = workDuration;
        updateTimerDisplay();
        statusLabel.setText("Work Time");

        // ← clear the old “Done/Close” text so mini‐window knows to go back to START/RESET
        nextPhaseButton.setText("");

        box.getChildren().setAll(startPauseButton, resetButton);
        timerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: black;");
        if (onUpdate != null) onUpdate.run();
    }

    public void setOnUpdate(Runnable r) { this.onUpdate = r; }
    public javafx.beans.property.StringProperty timerTextProperty() { return timerLabel.textProperty(); }
    public javafx.beans.property.StringProperty statusTextProperty() { return statusLabel.textProperty(); }
    public javafx.beans.property.StringProperty startPauseTextProperty() { return startPauseButton.textProperty(); }
    public javafx.beans.property.StringProperty nextPhaseTextProperty() { return nextPhaseButton.textProperty(); }

    public enum TimerButtonState {
        START_PAUSE_AND_RESET,
        NEXT_PHASE_ONLY,
        CLOSE_ONLY
    }

    public TimerButtonState getButtonState() {
        String txt = nextPhaseButton.getText();
        // if the button is “Done” or “Close” at all, treat it as CLOSE_ONLY:
        if ("Done".equals(txt) || "Close".equals(txt)) {
            return TimerButtonState.CLOSE_ONLY;
        } else if (waitingForNextPhase) {
            return TimerButtonState.NEXT_PHASE_ONLY;
        } else {
            return TimerButtonState.START_PAUSE_AND_RESET;
        }
    }
}
