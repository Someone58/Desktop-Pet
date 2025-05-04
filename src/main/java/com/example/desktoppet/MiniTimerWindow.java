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

    public MiniTimerWindow(Timer timer, Stage owner) {
        if (miniStage != null && miniStage.isShowing()) {
            miniStage.toFront();
            return;
        }

        miniStage = new Stage();
        miniStage.setTitle("Mini Timer");
        miniStage.setAlwaysOnTop(true);
        miniStage.setResizable(false);

        VBox miniRoot = new VBox(10);
        miniRoot.setAlignment(Pos.CENTER);

        Label miniStatusLabel = new Label();
        miniStatusLabel.textProperty().bind(timer.statusTextProperty());
        miniStatusLabel.setStyle("-fx-font-size: 20px;");

        Label miniTimerLabel = new Label();
        miniTimerLabel.textProperty().bind(timer.timerTextProperty());
        miniTimerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");

        Button miniStartPause = new Button();
        miniStartPause.textProperty().bind(timer.startPauseTextProperty());
        miniStartPause.setOnAction(e -> timer.startPauseButton.fire());
        miniStartPause.setStyle("-fx-font-size: 16px;");

        Button miniReset = new Button();
        miniReset.textProperty().bind(timer.resetButton.textProperty());
        miniReset.setOnAction(e -> timer.resetButton.fire());
        miniReset.setStyle("-fx-font-size: 16px;");

        Button miniNextPhase = new Button();
        miniNextPhase.textProperty().bind(timer.nextPhaseTextProperty());
        miniNextPhase.setOnAction(e -> timer.nextPhaseButton.fire());
        miniNextPhase.setStyle("-fx-font-size: 16px;");

        HBox miniButtonBox = new HBox(10, miniStartPause, miniReset, miniNextPhase);
        miniButtonBox.setAlignment(Pos.CENTER);

        miniRoot.getChildren().addAll(miniStatusLabel, miniTimerLabel, miniButtonBox);

        Scene miniScene = new Scene(miniRoot, 320, 200);
        miniStage.setScene(miniScene);

        // Clear static reference when closed
        miniStage.setOnCloseRequest(e -> miniStage = null);

        // Update button visibility when timer changes
        Runnable updateButtons = () -> {
            Timer.TimerButtonState state = timer.getButtonState();
            miniStartPause.setVisible(state == Timer.TimerButtonState.START_PAUSE_AND_RESET);
            miniReset.setVisible(state == Timer.TimerButtonState.START_PAUSE_AND_RESET);
            miniNextPhase.setVisible(
                    state == Timer.TimerButtonState.NEXT_PHASE_ONLY ||
                            state == Timer.TimerButtonState.CLOSE_ONLY
            );
        };

        timer.setOnUpdate(updateButtons);
        updateButtons.run();

        miniStage.show();
    }
}
