package com.example.desktoppet;

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
import javafx.stage.Stage;

import java.util.Objects;

/**
 * GUI implementation for the Timer.
 * Handles all UI operations for the timer functionality.
 */
public class TimerGUI implements TimerInterface {
    private final Timer timerLogic;
    private final PetController petController;
    
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
    private HBox buttonBox;
    
    // String properties for binding
    private final StringProperty timerTextProperty = new SimpleStringProperty();
    private final StringProperty statusTextProperty = new SimpleStringProperty("Work Time");
    private final StringProperty startPauseTextProperty = new SimpleStringProperty("");
    private final StringProperty nextPhaseTextProperty = new SimpleStringProperty();
    
    public TimerGUI(Timer timerLogic, PetController petController) {
        this.timerLogic = timerLogic;
        this.petController = petController;
        
        // Initialize components
        setupComponents();
        
        // Bind text properties
        timerLabel.textProperty().bindBidirectional(timerTextProperty);
        statusLabel.textProperty().bindBidirectional(statusTextProperty);
        startPauseButton.textProperty().bindBidirectional(startPauseTextProperty);
        nextPhaseButton.textProperty().bindBidirectional(nextPhaseTextProperty);
        
        // Set up event handlers
        setupEventHandlers();
    }
    
    private void setupComponents() {
        // Style components
        timerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");
        statusLabel.setStyle("-fx-font-size: 20px;");
        startPauseButton.setStyle("-fx-font-size: 16px;");
        resetButton.setStyle("-fx-font-size: 16px;");
        nextPhaseButton.setStyle("-fx-font-size: 16px;");

        Image startPauseButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/play.png")));
        ImageView startPauseButtonImgView = new ImageView(startPauseButtonImg);
        startPauseButton.setGraphic(startPauseButtonImgView);

        Image resetButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/replay.png")));
        ImageView resetButtonImgView = new ImageView(resetButtonImg);
        resetButton.setGraphic(resetButtonImgView);

        timerLabel.setId("timerLabel");
        statusLabel.setId("statusLabel");
        sessionLabel.setId("sessionLabel");
        
        // Set up text fields
        workField.setPromptText("25 (min)");
        breakField.setPromptText("5 (min)");
        sessionsField.setPromptText("4 Sessions");
        
        // Set up text formatters
        workField.setTextFormatter(new TextFormatter<>(timerLogic.new PositiveIntegerStringConverter(), null, timerLogic.positiveIntegerFilter));
        breakField.setTextFormatter(new TextFormatter<>(timerLogic.new PositiveIntegerStringConverter(), null, timerLogic.positiveIntegerFilter));
        sessionsField.setTextFormatter(new TextFormatter<>(timerLogic.new PositiveIntegerStringConverter(), null, timerLogic.positiveIntegerFilter));
        
        // Initialize buttonBox
        buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(startPauseButton, resetButton);
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

        VBox rootVBox = new VBox(10);
        rootVBox.setAlignment(Pos.CENTER);

        Label timerTitle = new Label("Timer");
        timerTitle.setId("timerTitle");

        Button backButton = new Button("");
        Image backButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/arrow.png")));
        ImageView backButtonImgView = new ImageView(backButtonImg);
        backButton.setGraphic(backButtonImgView);


        Scene scene = new Scene(rootVBox, 300, 400);

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

        HBox navigationHBox = new HBox(10);
        navigationHBox.getChildren().addAll(backButton, miniTimerButton);

        VBox inputBox = new VBox(10);
        inputBox.setAlignment(Pos.CENTER);

        // Apply saved input values if available
        if (timerLogic.getStudyInput() != null) workField.setText(timerLogic.getStudyInput());
        if (timerLogic.getBreakInput() != null) breakField.setText(timerLogic.getBreakInput());
        if (timerLogic.getSessionsInput() != null) sessionsField.setText(timerLogic.getSessionsInput());

        HBox workHBox = new HBox(5, new Label("Work:"), workField);
        HBox breakHBox = new HBox(5, new Label("Break:"), breakField);
        HBox sessionsHBox = new HBox(5, new Label("Sessions:"), sessionsField);

        inputBox.getChildren().addAll(
                workHBox,
                breakHBox,
                sessionsHBox);

        buttonBox.getChildren().clear();
        if (timerLogic.isWaitingForNextPhase()) {
            showNextPhaseButton();
        } else {
            buttonBox.getChildren().addAll(
                    startPauseButton,
                    resetButton);
        }

        rootVBox.getChildren().clear();
        rootVBox.getChildren().addAll(
                timerTitle,
                navigationHBox,
                statusLabel,
                sessionLabel,
                timerLabel,
                buttonBox,
                inputBox
        );

        rootVBox.setId("rootVBox");

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
            timerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: green;");
        } else if (isNegative) {
            timerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: red;");
        } else {
            timerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: black;");
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
        timerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: green;");
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
