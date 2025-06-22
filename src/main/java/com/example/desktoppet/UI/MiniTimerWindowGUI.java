package com.example.desktoppet.UI;

import com.example.desktoppet.Controller.Timer;
import com.example.desktoppet.Interfaces.MiniTimerWindowInterface;
import com.example.desktoppet.Model.PetData;
import com.example.desktoppet.StyleManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Objects;

public class MiniTimerWindowGUI implements MiniTimerWindowInterface {
    private static Stage miniStage = null; // Only one instance allowed
    private Timer timer;
    private PetData petController;

    // UI Components
    private Label miniStatusLabel;
    private Label miniSessionLabel;
    private Label miniTimerLabel;
    private Button miniStartPause;
    private Button miniReset;
    private Button miniNextPhase;
    private HBox miniButtonBox;
    private ImageView playImageView;
    private ImageView pauseImageView;

    @Override
    public void createMiniTimerWindow(Timer timer, PetData petController) {
        this.timer = timer;
        this.petController = petController;
        petController.setMinitimeropened(true);

        miniStage = new Stage();
        miniStage.setTitle("Mini Timer");
        miniStage.setAlwaysOnTop(true);
        miniStage.setResizable(false);

        Label miniTimerTitle = new Label(" Mini Timer");
        miniTimerTitle.setId("miniTimerTitle");

        VBox miniRootVBox = new VBox(6);
        miniRootVBox.setAlignment(Pos.CENTER);

        Scene miniScene = new Scene(miniRootVBox, 300, 200);
        miniScene.setFill(Color.web("#B8CCCB"));

        String css = petController.getCss();
        if (css != null) {
            String cssUrl = getClass().getResource(css).toExternalForm();
            if (cssUrl != null) {
                miniScene.getStylesheets().add(cssUrl);
            }
        }

        StyleManager.getInstance().registerScene(miniScene);

        setupComponents();

        VBox miniWorkTimeVBox = new VBox(2);
        miniWorkTimeVBox.setAlignment(Pos.CENTER);
        miniWorkTimeVBox.getChildren().addAll(miniStatusLabel, miniSessionLabel);

        miniButtonBox = new HBox(15);
        miniButtonBox.setAlignment(Pos.CENTER);
        miniButtonBox.setId("miniButtonBox");

        miniRootVBox.getChildren().addAll(miniTimerTitle, miniWorkTimeVBox, miniTimerLabel, miniButtonBox);
        miniRootVBox.setId("miniRootVBox");

        miniStage.setScene(miniScene);

        timer.setOnUpdate(() -> {
            updateButtonsUI(timer.getButtonState());
            updateTimerLabelColor(miniTimerLabel.getText());
            updateStartPauseButtonImage(timer.isRunning());
        });

        miniTimerLabel.textProperty().addListener((obs, oldVal, newVal) ->
                updateTimerLabelColor(newVal));

        updateButtonsUI(timer.getButtonState());
        updateTimerLabelColor(miniTimerLabel.getText());

        miniStage.setOnCloseRequest(e -> {
            miniStage = null;
            petController.setMinitimeropened(false);
        });

        miniStage.show();
    }

    private void setupComponents() {
        miniStatusLabel = new Label();
        miniStatusLabel.textProperty().bind(timer.statusTextProperty());
        miniStatusLabel.setId("statusLabel");

        miniSessionLabel = new Label();
        miniSessionLabel.textProperty().bind(timer.getTimerUI().getSessionLabel().textProperty());
        miniSessionLabel.setId("sessionLabel");

        miniTimerLabel = new Label();
        miniTimerLabel.textProperty().bind(timer.timerTextProperty());
        miniTimerLabel.setId("timerLabel");

        // Play-Button Image
        Image playButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/play.png")));
        playImageView = new ImageView(playButtonImg);
        playImageView.setFitWidth(17);
        playImageView.setFitHeight(17);
        playImageView.setPreserveRatio(true);

        // Pause-Button Image
        Image pauseButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pause.png")));
        pauseImageView = new ImageView(pauseButtonImg);
        pauseImageView.setFitWidth(17);
        pauseImageView.setFitHeight(17);
        pauseImageView.setPreserveRatio(true);

        miniStartPause = new Button("");
        miniStartPause.setGraphic(playImageView);
        miniStartPause.setOnAction(e -> timer.getTimerUI().getStartPauseButton().fire());
        miniStartPause.setStyle("-fx-font-size: 16px;");
        miniStartPause.setId("startPauseButton");

        Image resetButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/replay.png")));
        ImageView resetButtonImgView = new ImageView(resetButtonImg);
        resetButtonImgView.setFitWidth(17);
        resetButtonImgView.setFitHeight(17);
        resetButtonImgView.setPreserveRatio(true);
        miniReset = new Button("");
        miniReset.setGraphic(resetButtonImgView);
        miniReset.setOnAction(e -> timer.getTimerUI().getResetButton().fire());
        miniReset.setStyle("-fx-font-size: 16px;");
        miniReset.setId("resetButton");

        miniNextPhase = new Button();
        miniNextPhase.textProperty().bind(timer.nextPhaseTextProperty());
        miniNextPhase.setOnAction(e -> timer.getTimerUI().getNextPhaseButton().fire());
        miniNextPhase.setStyle("-fx-font-size: 16px;");
        miniNextPhase.setId("nextPhaseButton");
    }

    private void updateStartPauseButtonImage(boolean isPlaying) {
        miniStartPause.setGraphic(isPlaying ? pauseImageView : playImageView);
    }

    @Override
    public void updateButtonsUI(Timer.TimerButtonState state) {
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
    }

    @Override
    public void updateTimerLabelColor(String timerText) {
        boolean isStats = timerText.contains("Study:") && timerText.contains("Break:");
        boolean isNegative = timerText.startsWith("-");
        if (isStats) {
            miniTimerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: green;");
        } else if (isNegative) {
            miniTimerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: red;");
        } else {
            miniTimerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: #152F38;");
        }
    }

    @Override
    public void close() {
        if (miniStage != null) {
            miniStage.close();
            miniStage = null;
        }
    }

    @Override
    public void toFront() {
        if (miniStage != null) {
            miniStage.toFront();
        }
    }

    @Override
    public boolean isShowing() {
        return miniStage != null && miniStage.isShowing();
    }
}