package com.example.desktoppet.UI;

import com.example.desktoppet.Controller.MiniTimerWindow;
import com.example.desktoppet.Model.PetData;
import com.example.desktoppet.Controller.Timer;
import com.example.desktoppet.Interfaces.TimerInterface;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * GUI implementation for the Timer.
 * Handles all UI operations for the timer functionality.
 */
public class TimerGUI implements TimerInterface {
    private final Timer timerLogic;
    private final PetData petController;
    
    // UI Components
    private final Label statusLabel = new Label("Work Time");
    private final Label sessionLabel = new Label();
    private final Label timerLabel = new Label();
    private final Button startPauseButton = new Button("");
    private final Button resetButton = new Button("");
    private final Button nextPhaseButton = new Button();
    private final Button miniTimerButton = new Button("Mini Timer");
    private final TextField workField = new TextField();
    private final TextField breakField = new TextField();
    private final TextField sessionsField = new TextField();
    private final Label workLabel = new Label("Work:      ");
    private final Label breakLabel = new Label("Break:     ");
    private final Label sessionsLabel = new Label("Sessions: ");
    private HBox buttonBox;
    
    // String properties for binding
    private final StringProperty timerTextProperty = new SimpleStringProperty();
    private final StringProperty statusTextProperty = new SimpleStringProperty("Work Time");
    private final StringProperty startPauseTextProperty = new SimpleStringProperty("");
    private final StringProperty nextPhaseTextProperty = new SimpleStringProperty();

    private ImageView playImageView;
    private ImageView pauseImageView;

    public TimerGUI(Timer timerLogic, PetData petController) {
        this.timerLogic = timerLogic;
        this.petController = petController;

        // Initialize components
        setupComponents();

        // Bind nur die anderen Properties
        timerLabel.textProperty().bindBidirectional(timerTextProperty);
        statusLabel.textProperty().bindBidirectional(statusTextProperty);
        nextPhaseButton.textProperty().bindBidirectional(nextPhaseTextProperty);

        // Set up event handlers
        setupEventHandlers();
    }

    private void setupComponents() {
        nextPhaseButton.setStyle("-fx-font-size: 16px;");

        Image startPauseButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/play.png")));
        ImageView startPauseButtonImgView = new ImageView(startPauseButtonImg);
        startPauseButtonImgView.setFitWidth(17);
        startPauseButtonImgView.setFitHeight(17);
        startPauseButtonImgView.setPreserveRatio(true);
        startPauseButton.setGraphic(startPauseButtonImgView);

        Image resetButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/replay.png")));
        ImageView resetButtonImgView = new ImageView(resetButtonImg);
        resetButtonImgView.setFitWidth(17);
        resetButtonImgView.setFitHeight(17);
        resetButtonImgView.setPreserveRatio(true);
        resetButton.setGraphic(resetButtonImgView);

        // Play-Button Image
        Image playButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/play.png")));
        playImageView = new ImageView(playButtonImg);
        playImageView.setFitWidth(17);
        playImageView.setFitHeight(17);
        playImageView.setPreserveRatio(true);

        // Pause-Button Image
        Image pauseButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pause.png"))); // Sie müssen eine pause.png hinzufügen
        pauseImageView = new ImageView(pauseButtonImg);
        pauseImageView.setFitWidth(17);
        pauseImageView.setFitHeight(17);
        pauseImageView.setPreserveRatio(true);

        startPauseButton.setGraphic(playImageView);


        timerLabel.setId("timerLabel");
        statusLabel.setId("statusLabel");
        sessionLabel.setId("sessionLabel");
        startPauseButton.setId("startPauseButton");
        resetButton.setId("resetButton");
        nextPhaseButton.setId("nextPhaseButton");
        
        // Set up text fields
        workField.setPromptText("25 (min)");
        breakField.setPromptText("5 (min)");
        sessionsField.setPromptText("4 Sessions");
        
        // Set up text formatters
        workField.setTextFormatter(new TextFormatter<>(timerLogic.new PositiveIntegerStringConverter(), null, timerLogic.positiveIntegerFilter));
        breakField.setTextFormatter(new TextFormatter<>(timerLogic.new PositiveIntegerStringConverter(), null, timerLogic.positiveIntegerFilter));
        sessionsField.setTextFormatter(new TextFormatter<>(timerLogic.new PositiveIntegerStringConverter(), null, timerLogic.positiveIntegerFilter));
        
        // Initialize buttonBox
        buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(startPauseButton, resetButton);

        miniTimerButton.setId("miniTimerButton");
    }

    public void updateStartPauseButtonImage(boolean isPlaying) {
        startPauseButton.setGraphic(isPlaying ? pauseImageView : playImageView);
    }


    private void setupEventHandlers() {
        startPauseButton.setOnAction(e -> timerLogic.toggleTimer());
        resetButton.setOnAction(e -> timerLogic.resetTimer());
        miniTimerButton.setOnAction(e -> new MiniTimerWindow(timerLogic, petController));
    }
    
    @Override
    public void changeScene() {
        Stage stage = petController.getStage();
        Scene windowScene = petController.getWindowScene();

        petController.setTimeropened(true);
        petController.getNotification().setNoIcon();

        VBox rootTimerVBox = new VBox(10);
        rootTimerVBox.setAlignment(Pos.CENTER);

        Label timerTitle = new Label(" Timer");
        timerTitle.setId("timerTitle");

        Button backButton = new Button("");
        Image backButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/arrow.png")));
        ImageView backButtonImgView = new ImageView(backButtonImg);
        backButtonImgView.setFitWidth(15);
        backButtonImgView.setFitHeight(15);
        backButtonImgView.setPreserveRatio(true);
        backButton.setGraphic(backButtonImgView);
        backButton.setId("backButton");


        Scene scene = new Scene(rootTimerVBox, 300, 400);

        scene.setFill(Color.web("#B8CCCB"));

        String css = petController.getCss();
        if (css != null) {
            scene.getStylesheets().add(css);
        }

        stage.setTitle("Pomodoro Timer");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        backButton.setOnAction(e -> {
            timerLogic.saveInputFields(
                workField.getText(),
                breakField.getText(),
                sessionsField.getText()
            );
            stage.setScene(windowScene);
            stage.setTitle("Apps");
            petController.setTimeropened(false);
        });

        stage.setOnCloseRequest(event -> {
            petController.setTimeropened(false);
            timerLogic.saveInputFields(
                workField.getText(),
                breakField.getText(),
                sessionsField.getText()
            );
        });

        HBox navigationHBox = new HBox(25);
        navigationHBox.getChildren().addAll(backButton, miniTimerButton);

        VBox inputBox = new VBox(10);
        inputBox.setAlignment(Pos.CENTER);

        // Apply saved input values if available
        if (timerLogic.getStudyInput() != null) workField.setText(timerLogic.getStudyInput());
        if (timerLogic.getBreakInput() != null) breakField.setText(timerLogic.getBreakInput());
        if (timerLogic.getSessionsInput() != null) sessionsField.setText(timerLogic.getSessionsInput());

        HBox workHBox = new HBox(13);
        workHBox.getChildren().addAll(workLabel, workField);
        workHBox.setAlignment(Pos.CENTER);

        HBox breakHBox = new HBox(13);
        breakHBox.getChildren().addAll(breakLabel, breakField);
        breakHBox.setAlignment(Pos.CENTER);

        HBox sessionsHBox = new HBox(13);
        sessionsHBox.getChildren().addAll(sessionsLabel, sessionsField);
        sessionsHBox.setAlignment(Pos.CENTER);

        inputBox.getChildren().addAll(
                workHBox,
                breakHBox,
                sessionsHBox);


        inputBox.setId("inputBox");

        buttonBox.getChildren().clear();
        if (timerLogic.isWaitingForNextPhase()) {
            showNextPhaseButton();
        } else {
            buttonBox.getChildren().addAll(
                    startPauseButton,
                    resetButton);
        }

        buttonBox.setId("buttonBox");

        VBox workTimeVBox = new VBox(2);
        workTimeVBox.setAlignment(Pos.CENTER);
        workTimeVBox.getChildren().addAll(statusLabel, sessionLabel);

        VBox timeVBox = new VBox();
        timeVBox.setSpacing(4);
        timeVBox.setAlignment(Pos.CENTER);
        timeVBox.getChildren().addAll(workTimeVBox, timerLabel, buttonBox);

        rootTimerVBox.getChildren().clear();
        rootTimerVBox.getChildren().addAll(
                timerTitle,
                navigationHBox,
                timeVBox,
                inputBox
        );

        rootTimerVBox.setId("rootTimerVBox");

        updateTimerDisplay();
        updateSessionLabel();
        updateTimerLabelColor();
        
        if (timerLogic.isShowingStats()) {
            showStats();
        }

        if (timerLogic.getOnUpdate() != null) timerLogic.getOnUpdate().run();
    }
    
    @Override
    public void updateTimerDisplay() {
        timerTextProperty.set(timerLogic.formatSeconds(timerLogic.getRemainingTime()));
    }
    
    @Override
    public void updateSessionLabel() {
        int currentSession = timerLogic.isWork() ? timerLogic.getSessionsCompleted() + 1 : timerLogic.getSessionsCompleted();
        int totalSessions = timerLogic.getSessions();
        if (totalSessions > 0) {
            sessionLabel.setText("Session " + currentSession + " of " + totalSessions);
        } else {
            sessionLabel.setText("");
        }
    }
    
    @Override
    public void showNextPhaseButton() {
        buttonBox.getChildren().clear();

        if (timerLogic.isWork() && timerLogic.getSessionsCompleted() + 1 >= timerLogic.getSessions()) {
            nextPhaseButton.setText("Done");
            nextPhaseButton.setOnAction(e -> timerLogic.showStats());
        } else {
            nextPhaseButton.setText(timerLogic.isWork() ? "Start Break" : "Continue Studying");
            nextPhaseButton.setOnAction(e -> timerLogic.proceedToNextPhase());
        }

        buttonBox.getChildren().add(nextPhaseButton);
    }
    
    @Override
    public void updateButtons() {
        buttonBox.getChildren().clear();
        Timer.TimerButtonState state = timerLogic.getButtonState();
        
        switch (state) {
            case START_PAUSE_AND_RESET:
                buttonBox.getChildren().addAll(startPauseButton, resetButton);
                break;
            case NEXT_PHASE_ONLY:
            case CLOSE_ONLY:
                buttonBox.getChildren().add(nextPhaseButton);
                break;
        }
    }
    
    @Override
    public void updateTimerLabelColor() {
        String timerText = timerTextProperty.get();
        boolean isStats = timerText != null && timerText.contains("Study:") && timerText.contains("Break:");
        boolean isNegative = timerText != null && timerText.startsWith("-");
        
        if (isStats) {
            timerLabel.setStyle("-fx-font-size: 47px; -fx-font-weight: bold; -fx-text-fill: green;");
        } else if (isNegative) {
            timerLabel.setStyle("-fx-font-size: 47px; -fx-font-weight: bold; -fx-text-fill: red;");
        } else {
            timerLabel.setStyle("-fx-font-size: 47px; -fx-font-weight: bold; -fx-text-fill: #152F38;");
        }
    }
    
    @Override
    public void showStats() {
        buttonBox.getChildren().clear();
        nextPhaseButton.setText("Close");
        nextPhaseButton.setOnAction(e -> {
            timerLogic.resetTimer();
        });
        buttonBox.getChildren().add(nextPhaseButton);
        
        // Update display
        String studyTimeStr = timerLogic.formatSeconds(timerLogic.getStudyTime());
        String breakTimeStr = timerLogic.formatSeconds(timerLogic.getBreakTime());
        
        statusTextProperty.set("All sessions completed!");
        timerTextProperty.set("Study: " + studyTimeStr + "\nBreak: " + breakTimeStr);
        timerLabel.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-text-fill: green;");
    }
    
    @Override
    public StringProperty timerTextProperty() {
        return timerTextProperty;
    }
    
    @Override
    public StringProperty statusTextProperty() {
        return statusTextProperty;
    }
    
    @Override
    public StringProperty startPauseTextProperty() {
        return startPauseTextProperty;
    }
    
    @Override
    public StringProperty nextPhaseTextProperty() {
        return nextPhaseTextProperty;
    }
    
    @Override
    public Label getSessionLabel() {
        return sessionLabel;
    }
    
    @Override
    public Button getStartPauseButton() {
        return startPauseButton;
    }
    
    @Override
    public Button getResetButton() {
        return resetButton;
    }
    
    @Override
    public Button getNextPhaseButton() {
        return nextPhaseButton;
    }
    
    /**
     * Get the work duration field
     * @return the work field component
     */
    public TextField getWorkField() {
        return workField;
    }
    
    /**
     * Get the break duration field
     * @return the break field component
     */
    public TextField getBreakField() {
        return breakField;
    }
    
    /**
     * Get the sessions field
     * @return the sessions field component
     */
    public TextField getSessionsField() {
        return sessionsField;
    }
}
