// Only changed parts are commented

package com.example.desktoppet;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

import java.util.function.UnaryOperator;

public class Timer {
    private final PetController petController;
    private Timeline timeline;
    private int workDuration = 25 * 60;
    private int breakDuration = 5 * 60;
    private int sessions = 4;
    private int remainingTime;
    private boolean isWork = true;
    private int sessionsCompleted = 0;

    private int breakTime = 0;
    private int studyTime = 0;
    private String studyInput;
    private String breakInput;
    private String sessionsInput;
    private boolean showingStats = false;

    final Label sessionLabel = new Label();
    private final Label timerLabel = new Label();
    private final Label statusLabel = new Label("Work Time");
    final Button startPauseButton = new Button("Start");
    final Button resetButton = new Button("Reset");
    final Button nextPhaseButton = new Button();
    private final TextField workField = new TextField();
    private final TextField breakField = new TextField();
    private final TextField sessionsField = new TextField();
    private HBox buttonBox;
    private final Button miniTimerButton = new Button("Mini Timer");

    private Runnable onUpdate = null;

    private final BooleanProperty waitingForNextPhaseProperty = new SimpleBooleanProperty(false);

    private boolean waitingForNextPhase = false;

    private static final int MAX_MINUTES = 24 * 60;

    Notification notification;

    public Timer(PetController petController) {
        this.petController = petController;
        this.notification = petController.getNotification();

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        remainingTime = workDuration;
    }

    public void changeScene() {
        Stage stage = petController.getStage();
        Scene windowScene = petController.getWindowScene();

        petController.setTimeropened(true);
        notification.setNoIcon();

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button backButton = new Button("Back");
        Scene scene = new Scene(root, 300, 400);

        String css = petController.getCss();
        if (css != null) {
            scene.getStylesheets().add(css);
        }

        stage.setTitle("Pomodoro Timer");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        backButton.setOnAction(e -> {
            studyInput = workField.getText();
            breakInput = breakField.getText();
            sessionsInput = sessionsField.getText();

            stage.setScene(windowScene);
            stage.setTitle("Apps");
            petController.setTimeropened(false);

        });

        stage.setOnCloseRequest(event -> {
            System.out.println("Stage is closing");
            petController.setTimeropened(false);

            studyInput = workField.getText();
            breakInput = breakField.getText();
            sessionsInput = sessionsField.getText();
        });

        VBox inputBox = new VBox(10);
        inputBox.setAlignment(Pos.CENTER);

        workField.setPromptText("25 (min)");
        breakField.setPromptText("5 (min)");
        sessionsField.setPromptText("4 Sessions");

        workField.setTextFormatter(new TextFormatter<>(new PositiveIntegerStringConverter(), null, positiveIntegerFilter));
        breakField.setTextFormatter(new TextFormatter<>(new PositiveIntegerStringConverter(), null, positiveIntegerFilter));
        sessionsField.setTextFormatter(new TextFormatter<>(new PositiveIntegerStringConverter(), null, positiveIntegerFilter));

        workField.setText(studyInput);
        breakField.setText(breakInput);
        sessionsField.setText(sessionsInput);

        HBox workHBox = new HBox(5, new Label("Work:"), workField);
        HBox breakHBox = new HBox(5, new Label("Break:"), breakField);
        HBox sessionsHBox = new HBox(5, new Label("Sessions:"), sessionsField);

        inputBox.getChildren().addAll(workHBox, breakHBox, sessionsHBox);

        timerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");
        statusLabel.setStyle("-fx-font-size: 20px;");
        startPauseButton.setStyle("-fx-font-size: 16px;");
        resetButton.setStyle("-fx-font-size: 16px;");
        nextPhaseButton.setStyle("-fx-font-size: 16px;");
        buttonBox.getChildren().clear();
        if (waitingForNextPhase) {
            showNextPhaseButton(buttonBox);
        } else {
            buttonBox.getChildren().addAll(startPauseButton, resetButton);
        }
        startPauseButton.setOnAction(e -> toggleTimer());
        resetButton.setOnAction(e -> resetTimer(buttonBox));
        miniTimerButton.setOnAction(e -> new MiniTimerWindow(this, petController));

        root.getChildren().clear();
        root.getChildren().addAll(backButton, miniTimerButton, statusLabel, sessionLabel, timerLabel, buttonBox, inputBox);

//        remainingTime = workDuration;
        updateTimerDisplay();
        updateSessionLabel();
        if (waitingForNextPhase && remainingTime < 0) {
            timerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: red;");
        } else {
            timerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: black;");
        }
        if (showingStats) {
            showStats(buttonBox);
        }

        if (onUpdate != null) onUpdate.run();
    }

    UnaryOperator<TextFormatter.Change> positiveIntegerFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("\\d*")) return change;
        return null;
    };

    public class PositiveIntegerStringConverter extends IntegerStringConverter {
        @Override
        public Integer fromString(String value) {
            if (value == null || value.isEmpty()) return null;
            int intValue = super.fromString(value);
            if (intValue < 0) throw new RuntimeException("Negative number not allowed");
            return intValue;
        }

        @Override
        public String toString(Integer value) {
            if (value == null) return "";
            if (value < 0) return "0";
            return super.toString(value);
        }
    }

    private int clampToMaxMinutes(TextField field, int defaultValue) {
        try {
            int value = Integer.parseInt(field.getText());
            if (value < 1) return defaultValue;
            if (value > MAX_MINUTES) {
                field.setText(String.valueOf(MAX_MINUTES));
                return MAX_MINUTES;
            }
            return value;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    void toggleTimer() {
        if (timeline == null || timeline.getStatus() == Timeline.Status.STOPPED) {
            startTimer();
        } else if (timeline.getStatus() == Timeline.Status.RUNNING) {
            timeline.pause();
            startPauseButton.setText("Resume");
        } else {
            timeline.play();
            startPauseButton.setText("Pause");
        }
        if (onUpdate != null) onUpdate.run();
    }

    private void startTimer() {
        if (timeline != null) timeline.stop();

        workDuration = clampToMaxMinutes(workField, 25) * 60;
        breakDuration = clampToMaxMinutes(breakField, 5) * 60;
        sessions = clampToMaxMinutes(sessionsField, 4);

        remainingTime = workDuration;
        isWork = true;
        sessionsCompleted = 0;
        waitingForNextPhase = false;
        waitingForNextPhaseProperty.set(false);

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        startPauseButton.setText("Pause");
        statusLabel.setText("Work Time");
        timerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: black;");
        buttonBox.getChildren().setAll(startPauseButton, resetButton);
        updateSessionLabel();
        if (onUpdate != null) onUpdate.run();
    }

    private void updateTimer() {
        remainingTime--;
        if (isWork) {
            studyTime++;
//            System.out.println("Study: " + studyTime);
        }
        else {
            breakTime++;
//            System.out.println("Break: " + breakTime);
        }
        updateTimerDisplay();

        if (remainingTime == 0 && !waitingForNextPhase) {
            waitingForNextPhase = true;
            waitingForNextPhaseProperty.set(true);
            showNextPhaseButton(buttonBox);
        } else if (waitingForNextPhase) {
            timerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: red;");
            if (petController.isTimeropened() == false && petController.isMinitimeropened() == false){
                notification.setTimerIcon();
            }
        }

        if (onUpdate != null) onUpdate.run();
    }

    private void showNextPhaseButton(HBox box) {
        box.getChildren().clear();

        if (isWork && sessionsCompleted + 1 >= sessions) {
            nextPhaseButton.setText("Done");
            nextPhaseButton.setOnAction(e -> showStats(box));
        } else {
            nextPhaseButton.setText(isWork ? "Start Break" : "Continue Studying");
            nextPhaseButton.setOnAction(e -> proceedToNextPhase(box));
        }

        box.getChildren().add(nextPhaseButton);
        if (onUpdate != null) onUpdate.run();
    }


    private void proceedToNextPhase(HBox box) {
        waitingForNextPhase = false;
        waitingForNextPhaseProperty.set(false);
        timerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: black;");
        notification.setNoIcon();

        if (isWork) {
            sessionsCompleted++;
            remainingTime = breakDuration;
            isWork = false;
            statusLabel.setText("Break Time");
        } else {
            remainingTime = workDuration;
            isWork = true;
            statusLabel.setText("Work Time");
        }

        updateTimerDisplay();
        updateSessionLabel();
        box.getChildren().setAll(startPauseButton, resetButton);
        if (onUpdate != null) onUpdate.run();
    }

    private void showStats(HBox box) {
        showingStats = true;
        timeline.stop();
        waitingForNextPhase = false;
        waitingForNextPhaseProperty.set(false);
        timerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: green;");
        notification.setNoIcon();

        String studyTimeStr = formatSeconds(studyTime);
        String breakTimeStr = formatSeconds(breakTime);
        statusLabel.setText("All sessions completed!");
        timerLabel.setText("Study: " + studyTimeStr + "\nBreak: " + breakTimeStr);

        box.getChildren().clear();
        nextPhaseButton.setText("Close");
        nextPhaseButton.setOnAction(e -> {
            resetTimer(box);
            showingStats = false;
        });
        box.getChildren().add(nextPhaseButton);
        if (onUpdate != null) onUpdate.run();
    }

    private String formatSeconds(int totalSeconds) {
        int absSeconds = Math.abs(totalSeconds);
        int hours = absSeconds / 3600;
        int minutes = (absSeconds % 3600) / 60;
        int seconds = absSeconds % 60;

        String sign = totalSeconds < 0 ? "-" : "";

        if (hours > 0) {
            return String.format("%s%d:%02d:%02d", sign, hours, minutes, seconds);
        } else {
            return String.format("%s%02d:%02d", sign, minutes, seconds);
        }
    }

    private void updateTimerDisplay() {
        timerLabel.setText(formatSeconds(remainingTime));
        if (onUpdate != null) onUpdate.run();
    }

    private void updateSessionLabel() {
        int currentSession = isWork ? sessionsCompleted + 1 : sessionsCompleted;
        if (sessions > 0) {
            sessionLabel.setText("Session " + currentSession + " of " + sessions);
        } else {
            sessionLabel.setText("");
        }
    }

    private void resetTimer(HBox box) {
        if (timeline != null) timeline.stop();
        startPauseButton.setText("Start");
        isWork = true;
        sessionsCompleted = 0;
        waitingForNextPhase = false;
        waitingForNextPhaseProperty.set(false);
        workDuration = clampToMaxMinutes(workField, 25) * 60;
        remainingTime = workDuration;
        studyTime = 0;
        breakTime = 0;
        updateTimerDisplay();
        statusLabel.setText("Work Time");
        nextPhaseButton.setText("");
        box.getChildren().setAll(startPauseButton, resetButton);
        timerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: black;");
        notification.setNoIcon();
//        updateSessionLabel();
        sessionLabel.setText("Session 1 of " + sessionsField.getText());
        if (onUpdate != null) onUpdate.run();
    }

    public void setOnUpdate(Runnable r) { this.onUpdate = r; }
    public StringProperty timerTextProperty() { return timerLabel.textProperty(); }
    public StringProperty statusTextProperty() { return statusLabel.textProperty(); }
    public StringProperty startPauseTextProperty() { return startPauseButton.textProperty(); }
    public StringProperty nextPhaseTextProperty() { return nextPhaseButton.textProperty(); }

    public enum TimerButtonState {
        START_PAUSE_AND_RESET,
        NEXT_PHASE_ONLY,
        CLOSE_ONLY
    }

    public TimerButtonState getButtonState() {
        String txt = nextPhaseButton.getText();
        if ("Done".equals(txt) || "Close".equals(txt)) {
            return TimerButtonState.CLOSE_ONLY;
        } else if (waitingForNextPhase) {
            return TimerButtonState.NEXT_PHASE_ONLY;
        } else {
            return TimerButtonState.START_PAUSE_AND_RESET;
        }
    }
}
