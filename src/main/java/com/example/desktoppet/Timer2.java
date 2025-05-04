package com.example.desktoppet;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Timer2 {

    int completeTime = 0;
    int completeBreakTime = 0;
    private int timeInSeconds = 3600;
    private int studyTime = 3600;
    private int pauseTime = 600;
    private Timeline timeline;
    boolean startEnabled = true;
    Label timer = new Label(formatTime(timeInSeconds));
    Label timerForView = new Label(timer.getText());
    private boolean studymode = true;
    Label totalStudyTime = new Label(Math.round((float)completeTime / 3600) + " h " + (int)(((float)completeTime % 3600)/60) + "min");
    Label totalBreakTime = new Label(Math.round((float)completeBreakTime / 3600) + " h " + (int)(((float)completeBreakTime % 3600)/60) + "min");
    String currentTimerState = "timer";
    private int repeatAmount = 2;
    private int currentRepeat = 0;

    VBox rootVBox = new VBox(5);

    Button pauseStart = new Button("Pause starten :)");
    Button studyStart = new Button("Lernen starten :)");
    Label finished = new Label("Fertig!");
    Button start = new Button("Starten");
    Button pause = new Button("Pausieren");
    Button restart = new Button("Nochmals");
    HBox timerbox = new HBox();
    VBox timerVbox = new VBox();
    VBox finishVbox = new VBox();
    HBox controlButtons = new HBox(20);
    Label currentMode = new Label("Lernen: " + (currentRepeat + 1) + "/" + repeatAmount);
    Label currentMode2 = new Label(currentMode.getText());

    public void changeScene(Stage stage, Scene windowScene) {

        Button backButton = new Button("Back");



//        VBox rootVBox = new VBox(5);
        rootVBox.getChildren().addAll(
                backButton,
                timerbox,
                start,
                pause
        );

        Group root = new Group(rootVBox);
        Scene scene = new Scene(root, 300, 400);

        String css = this.getClass().getResource("/application.css").toExternalForm();
        if (css != null) {
            scene.getStylesheets().add(css);
        }

        stage.setTitle("Chat");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);


        backButton.setOnAction(e -> {
            stage.setScene(windowScene);
        });


        pauseStart.setOnAction(e -> {
            currentTimerState = "timer";
            timerbox.getChildren().clear();
            studymode = false;
            timerbox.getChildren().addAll(timerVbox);
            timerbox.setPadding(new Insets(0, 0, 40, 0));
            startTimer(timer);
            currentMode.setText("Pause: " + (currentRepeat + 1) + "/" + (repeatAmount - 1));

//            timerbox2.getChildren().clear();
//            timerbox2.getChildren().addAll(timerVbox2);
//            timerbox2.setPadding(new Insets(0, 0, 10, 0));
            currentMode2.setText("Pause: " + (currentRepeat + 1) + "/" + (repeatAmount - 1));
        });

        studyStart.setOnAction(e -> {
            currentTimerState = "timer";
            timerbox.getChildren().clear();
            currentRepeat += 1;
            studymode = true;
            timerbox.getChildren().addAll(timerVbox);
            timerbox.setPadding(new Insets(0, 0, 40, 0));
            startTimer(timer);
            currentMode.setText("Lernen: " + (currentRepeat + 1) + "/" + repeatAmount);

//            timerbox2.getChildren().clear();
//            timerbox2.getChildren().addAll(timerVbox2);
//            timerbox2.setPadding(new Insets(0, 0, 10, 0));
            currentMode2.setText("Lernen: " + (currentRepeat + 1) + "/" + repeatAmount);
        });

        restart.setOnAction(e -> {
            currentTimerState = "timer";
            timerbox.getChildren().clear();
            currentRepeat = 0;
            studymode = true;
            timerbox.getChildren().addAll(timerVbox);
            timerbox.setPadding(new Insets(0, 0, 40, 0));
            controlButtons.getChildren().clear();
            controlButtons.getChildren().addAll(start, pause);
            timeInSeconds = studyTime;
            timer.setText(formatTime(studyTime));
            currentMode.setText("Lernen: " + (currentRepeat + 1) + "/" + repeatAmount);

//            timerbox2.getChildren().clear();
//            timerbox2.getChildren().addAll(timerVbox2);
//            timerbox2.setPadding(new Insets(0, 0, 10, 0));
//            controlButtons2.getChildren().clear();
//            controlButtons2.getChildren().addAll(start2, pause2);
            timerForView.setText(formatTime(studyTime));
            currentMode2.setText("Lernen: " + (currentRepeat + 1) + "/" + repeatAmount);
            pause.setDisable(false);
//            pause2.setDisable(false);
            start.setDisable(false);
//            start2.setDisable(false);
        });

//        pauseStart.setMinWidth(screenWidth/3 - 20);
//        studyStart.setMinWidth(screenWidth/3 - 20);
//        restart.setMinWidth(screenWidth/3 - 20);

        timer.setId("timer");
        pauseStart.setId("pauseButton");
        studyStart.setId("pauseButton");
//        studyStart2.setPrefWidth(220);
        finished.setId("finishButton");
        timerVbox.getChildren().addAll(timer, currentMode);
        finishVbox.getChildren().addAll(finished);
        timerbox.setPadding(new Insets(0, 0, 40, 0));
        timerbox.setAlignment(Pos.TOP_CENTER);
        timerVbox.setAlignment(Pos.TOP_CENTER);
        finishVbox.setAlignment(Pos.TOP_CENTER);

        if (!(currentTimerState != null)) {
            currentTimerState = "timer";
        }
        if (currentTimerState.equals("timer")) {
            timerbox.getChildren().addAll(timerVbox);
            timerbox.setPadding(new Insets(0, 0, 40, 0));
            controlButtons.getChildren().addAll(start, pause);
        } else if (currentTimerState.equals("pause")) {
            timerbox.getChildren().addAll(pauseStart);
            timerbox.setPadding(new Insets(20, 0, 45, 0));
            controlButtons.getChildren().addAll(start, pause);
            System.out.println(currentTimerState);
            pause.setDisable(true);
            start.setDisable(true);
        } else if (currentTimerState.equals("study")) {
            System.out.println(currentTimerState);
            timerbox.getChildren().addAll(studyStart);
            timerbox.setPadding(new Insets(20, 0, 45, 0));
            controlButtons.getChildren().addAll(start, pause);
            pause.setDisable(true);
            start.setDisable(true);
        } else if (currentTimerState.equals("finish")) {
            System.out.println(currentTimerState);
            timerbox.getChildren().addAll(finishVbox);
            timerbox.setPadding(new Insets(20, 0, 45, 0));
            finishVbox.setPadding(new Insets(5, 0, 4, 0));
            controlButtons.getChildren().clear();
            controlButtons.getChildren().addAll(restart);
        }

//        start.setMinWidth(screenWidth/6 - 20);
//        pause.setMinWidth(screenWidth/6 - 20);

        start.setOnAction(e -> {
            if (startEnabled == true) {
                startTimer(timer);
            }
        });
        pause.setOnAction(e -> pauseTimer());


//        icons.setId("iconButton");

        Button timerWindow = new Button("pin");
//        timerWindow.setGraphic(icons);
//        icons.setFitHeight(20);
//        icons.setFitWidth(20);
        timerWindow.setId("iconButton");
        timerWindow.setOnAction(e -> {
//            openTimerView();
        });
    }

    private void startTimer(Label timerLabel) {
        if (timeline != null) {
            timeline.stop();
        }

        startEnabled = false;

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeInSeconds--;
            startEnabled = true;

            if (studymode == true) {
                completeTime++;
            } else if (studymode == false) {
                completeBreakTime++;
                System.out.println(completeBreakTime);
            }

            timerLabel.setText(formatTime(timeInSeconds));
            if (timeInSeconds <= 0) {
                timeline.stop();
//                finish.play();

                timerbox.getChildren().clear();
//                timerbox2.getChildren().clear();
                if (studymode == true && currentRepeat < repeatAmount - 1) {
                    currentTimerState = "pause";
                    timerbox.getChildren().addAll(pauseStart);
//                    timerbox2.getChildren().addAll(pauseStart2);
//                    timerbox2.setPadding(new Insets(23, 0, 23, 0));
                    timeInSeconds = pauseTime;
                    currentMode.setText("Pause: " + (currentRepeat + 1) + "/" + (repeatAmount - 1));
                    currentMode2.setText(currentMode.getText());
                    pause.setDisable(true);
//                    pause2.setDisable(true);
                    start.setDisable(true);
//                    start2.setDisable(true);
                } else if (studymode == false && currentRepeat < repeatAmount - 1){
                    currentTimerState = "study";
                    timerbox.getChildren().addAll(studyStart);
//                    timerbox2.getChildren().addAll(studyStart2);
//                    timerbox2.setPadding(new Insets(23, 0, 23, 0));
                    timeInSeconds = studyTime;
                    currentMode.setText("Lernen: " + (currentRepeat + 1) + "/" + repeatAmount);
                    currentMode2.setText(currentMode.getText());
                    pause.setDisable(true);
//                    pause2.setDisable(true);
                    start.setDisable(true);
//                    start2.setDisable(true);
                } else if (currentRepeat >= repeatAmount - 1) {
                    currentTimerState = "finish";
                    timerbox.getChildren().addAll(finishVbox);
//                    timerbox2.getChildren().addAll(finishVbox2);
                    finishVbox.setPadding(new Insets(5, 0, 4, 0));
//                    finishVbox2.setPadding(new Insets(18, 0, 8, 0));
                    controlButtons.getChildren().clear();
                    controlButtons.getChildren().addAll(restart);
//                    controlButtons2.getChildren().clear();
//                    controlButtons2.getChildren().addAll(restart2);
//                    restart2.setPrefWidth(218);

                }

                timerbox.setPadding(new Insets(11, 0, 51, 0));

            }
            timerForView.setText(formatTime(timeInSeconds));
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private String formatTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void pauseTimer() {
        if (timeline != null) {
            timeline.pause();
        }
    }
}
