package com.example.desktoppet.UI;

import com.example.desktoppet.*;
import com.example.desktoppet.Controller.Notification;
import com.example.desktoppet.Controller.Timer;
import com.example.desktoppet.Interfaces.MiniTimerWindowInterface;
import com.example.desktoppet.Model.PetData;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;


/**
 * GUI implementation for the Mini Timer Window.
 * Handles all UI operations for the mini timer functionality.
 */
public class MiniTimerWindowGUI implements MiniTimerWindowInterface {
    private static Stage miniStage = null; // Only one instance allowed
    private Notification notification;
    private PetData petController;
    private Timer timer;
    
    // UI Components
    private Label miniStatusLabel;
    private Label miniSessionLabel;
    private Label miniTimerLabel;
    private Button miniStartPause;
    private Button miniReset;
    private Button miniNextPhase;
    private HBox miniButtonBox;
    
    @Override
    public void createMiniTimerWindow(Timer timer, PetData petController) {
        this.timer = timer;
        this.petController = petController;
        this.notification = petController.getNotification();
        petController.setMinitimeropened(true);
        
        miniStage = new Stage();
        miniStage.setTitle("Mini Timer");
        miniStage.setAlwaysOnTop(true);
        miniStage.setResizable(false);

        Label miniTimerTitle = new Label("Timer");
        miniTimerTitle.setId("miniTimerTitle");

        VBox rootVBox = new VBox(10);
        rootVBox.setAlignment(Pos.CENTER);

        Scene miniScene = new Scene(rootVBox, 320, 220);

        // Apply the current stylesheet using proper resource loading
        String css = petController.getCss();
        if (css != null) {
            String cssUrl = getClass().getResource(css).toExternalForm();
            if (cssUrl != null) {
                miniScene.getStylesheets().add(cssUrl);
            }
        }

        // Register the scene with StyleManager to handle future stylesheet changes
        StyleManager.getInstance().registerScene(miniScene);
        
        // Initialize UI components
        setupComponents(timer);
        
        // Initialize layout
        miniButtonBox = new HBox(10);
        miniButtonBox.setAlignment(Pos.CENTER);
        rootVBox.getChildren().addAll(miniTimerTitle, miniStatusLabel, miniSessionLabel, miniTimerLabel, miniButtonBox);

        rootVBox.setId("rootVBox");
        
        miniStage.setScene(miniScene);
        
        // Set up update functions
        Runnable updateButtons = () -> updateButtonsUI(timer.getButtonState());
        
        Runnable updateTimerLabelColor = () -> {
            String timerText = miniTimerLabel.getText();
            updateTimerLabelColor(timerText);
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
    
    private void setupComponents(Timer timer) {
        miniStatusLabel = new Label();
        miniStatusLabel.textProperty().bind(timer.statusTextProperty());
        miniStatusLabel.setStyle("-fx-font-size: 20px;");
        miniStatusLabel.setId("statusLabel");

        miniSessionLabel = new Label();
        miniSessionLabel.textProperty().bind(timer.getTimerUI().getSessionLabel().textProperty());
        miniSessionLabel.setStyle("-fx-font-size: 16px;");
        miniSessionLabel.setId("sessionLabel");

        miniTimerLabel = new Label();
        miniTimerLabel.textProperty().bind(timer.timerTextProperty());
        miniTimerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");
        miniTimerLabel.setId("timerLabel");

        miniStartPause = new Button();
        miniStartPause.textProperty().bind(timer.startPauseTextProperty());
        miniStartPause.setOnAction(e -> timer.getTimerUI().getStartPauseButton().fire());
        miniStartPause.setStyle("-fx-font-size: 16px;");

        Image miniStartPauseImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/play.png")));
        ImageView miniStartPauseImgView = new ImageView(miniStartPauseImg);
        miniStartPause.setGraphic(miniStartPauseImgView);
        
        miniReset = new Button("");
        miniReset.setOnAction(e -> timer.getTimerUI().getResetButton().fire());
        miniReset.setStyle("-fx-font-size: 16px;");

        Image miniResetImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/replay.png")));
        ImageView miniResetImgView = new ImageView(miniResetImg);
        miniReset.setGraphic(miniResetImgView);

        miniNextPhase = new Button();
        miniNextPhase.textProperty().bind(timer.nextPhaseTextProperty());
        miniNextPhase.setOnAction(e -> timer.getTimerUI().getNextPhaseButton().fire());
        miniNextPhase.setStyle("-fx-font-size: 16px;");

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
            miniTimerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: black;");
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
