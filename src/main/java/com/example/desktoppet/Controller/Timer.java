// Only changed parts are commented

package com.example.desktoppet.Controller;

import com.example.desktoppet.Model.PetData;
import com.example.desktoppet.Interfaces.TimerInterface;
import com.example.desktoppet.UI.TimerGUI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

import java.util.function.UnaryOperator;

/**
 * Handles timer logic and data, delegating UI operations to TimerInterface.
 */
public class Timer {
    private final PetData petController;
    private final TimerInterface timerUI;
    private Timeline timeline;
    
    // Timer settings
    private int workDuration = 25 * 60;
    private int breakDuration = 5 * 60;
    private int sessions = 4;
    private int remainingTime;
    private boolean isWork = true;
    private int sessionsCompleted = 0;
    
    // Stats tracking
    private int breakTime = 0;
    private int studyTime = 0;
    private String studyInput;
    private String breakInput;
    private String sessionsInput;
    private boolean showingStats = false;
    
    // UI interaction
    private Runnable onUpdate = null;
    private final BooleanProperty waitingForNextPhaseProperty = new SimpleBooleanProperty(false);
    private boolean waitingForNextPhase = false;
    
    private static final int MAX_MINUTES = 24 * 60;
    
    Notification notification;
    
    /**
     * Constructor that uses the default TimerGUI implementation
     * @param petController the pet controller instance
     */
    public Timer(PetData petController) {
        this.petController = petController;
        this.notification = petController.getNotification();
        this.timerUI = new TimerGUI(this, petController);
        
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        remainingTime = workDuration;
    }
    
    /**
     * Constructor that accepts a custom UI implementation
     * @param petController the pet controller instance
     * @param timerUI custom UI implementation
     */
    public Timer(PetData petController, TimerInterface timerUI) {
        this.petController = petController;
        this.notification = petController.getNotification();
        this.timerUI = timerUI;
        
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        remainingTime = workDuration;
    }
    
    /**
     * Changes to the timer scene using the UI implementation
     */
    public void changeScene() {
        timerUI.changeScene();
    }
    
    /**
     * Toggles the timer between start, pause, and resume states
     */
    public void toggleTimer() {
        if (timeline == null || timeline.getStatus() == Timeline.Status.STOPPED) {
            startTimer();
        } else if (timeline.getStatus() == Timeline.Status.RUNNING) {
            timeline.pause();
            timerUI.startPauseTextProperty().set("Resume");
        } else {
            timeline.play();
            timerUI.startPauseTextProperty().set("Pause");
        }
        if (onUpdate != null) onUpdate.run();
    }
    
    /**
     * Starts the timer with current settings
     */
    private void startTimer() {
        if (timeline != null) timeline.stop();
        
        // Get values from UI fields via the controller
        TextField workField = ((TimerGUI)timerUI).getWorkField();
        TextField breakField = ((TimerGUI)timerUI).getBreakField();
        TextField sessionsField = ((TimerGUI)timerUI).getSessionsField();
        
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
        
        timerUI.startPauseTextProperty().set("Pause");
        timerUI.statusTextProperty().set("Work Time");
        timerUI.updateButtons();
        timerUI.updateSessionLabel();
        timerUI.updateTimerLabelColor();
        
        if (onUpdate != null) onUpdate.run();
    }
    
    /**
     * Updates the timer state each second
     */
    private void updateTimer() {
        remainingTime--;
        if (isWork) {
            studyTime++;
        }
        else {
            breakTime++;
        }
        timerUI.updateTimerDisplay();
        
        if (remainingTime == 0 && !waitingForNextPhase) {
            waitingForNextPhase = true;
            waitingForNextPhaseProperty.set(true);
            timerUI.showNextPhaseButton();
        } else if (waitingForNextPhase) {
            timerUI.updateTimerLabelColor();
            if (!petController.isTimeropened() && !petController.isMinitimeropened()) {
                notification.setTimerIcon();
            }
        }
        
        if (onUpdate != null) onUpdate.run();
    }
    
    /**
     * Proceeds to the next phase of the timer (work or break)
     */
    public void proceedToNextPhase() {
        waitingForNextPhase = false;
        waitingForNextPhaseProperty.set(false);
        notification.setNoIcon();
        
        if (isWork) {
            sessionsCompleted++;
            remainingTime = breakDuration;
            isWork = false;
            timerUI.statusTextProperty().set("Break Time");
        } else {
            remainingTime = workDuration;
            isWork = true;
            timerUI.statusTextProperty().set("Work Time");
        }
        
        timerUI.updateTimerDisplay();
        timerUI.updateSessionLabel();
        timerUI.updateButtons();
        timerUI.updateTimerLabelColor();
        
        if (onUpdate != null) onUpdate.run();
    }
    
    /**
     * Shows the stats after completing all sessions
     */
    public void showStats() {
        showingStats = true;
        if (timeline != null) timeline.stop();
        waitingForNextPhase = false;
        waitingForNextPhaseProperty.set(false);
        notification.setNoIcon();
        
        timerUI.showStats();
        
        if (onUpdate != null) onUpdate.run();
    }
    
    /**
     * Resets the timer to initial state
     */
    public void resetTimer() {
        if (timeline != null) timeline.stop();
        
        // Get values from UI fields via the controller
        TextField sessionsField = ((TimerGUI)timerUI).getSessionsField();
        TextField workField = ((TimerGUI)timerUI).getWorkField();
        
        isWork = true;
        sessionsCompleted = 0;
        waitingForNextPhase = false;
        waitingForNextPhaseProperty.set(false);
        workDuration = clampToMaxMinutes(workField, 25) * 60;
        remainingTime = workDuration;
        studyTime = 0;
        breakTime = 0;
        
        timerUI.updateTimerDisplay();
        timerUI.statusTextProperty().set("Work Time");
        timerUI.nextPhaseTextProperty().set("");
        timerUI.startPauseTextProperty().set("Start");
        timerUI.updateButtons();
        timerUI.updateTimerLabelColor();
        
        notification.setNoIcon();
        timerUI.getSessionLabel().setText("Session 1 of " + sessionsField.getText());
        showingStats = false;
        
        if (onUpdate != null) onUpdate.run();
    }
    
    /**
     * Save input field values
     */
    public void saveInputFields(String work, String breakTime, String sessionCount) {
        studyInput = work;
        breakInput = breakTime;
        sessionsInput = sessionCount;
    }
    
    /**
     * Format seconds into a human-readable time string
     * @param totalSeconds the total seconds to format
     * @return formatted time string
     */
    public String formatSeconds(int totalSeconds) {
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
    
    /**
     * Clamp a value from a text field to a valid minutes range
     * @param field the text field containing the value
     * @param defaultValue the default value if parsing fails
     * @return the clamped value
     */
    public int clampToMaxMinutes(TextField field, int defaultValue) {
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
    
    /**
     * Filter that only allows positive integers
     */
    public UnaryOperator<TextFormatter.Change> positiveIntegerFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("\\d*")) return change;
        return null;
    };
    
    /**
     * Converter for ensuring positive integers in text fields
     */
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
    
    /**
     * Enumeration of possible button states
     */
    public enum TimerButtonState {
        START_PAUSE_AND_RESET,
        NEXT_PHASE_ONLY,
        CLOSE_ONLY
    }
    
    /**
     * Get the current button state
     * @return the current button state
     */
    public TimerButtonState getButtonState() {
        String txt = timerUI.nextPhaseTextProperty().get();
        if ("Done".equals(txt) || "Close".equals(txt)) {
            return TimerButtonState.CLOSE_ONLY;
        } else if (waitingForNextPhase) {
            return TimerButtonState.NEXT_PHASE_ONLY;
        } else {
            return TimerButtonState.START_PAUSE_AND_RESET;
        }
    }
    
    // Getters and setters
    public void setOnUpdate(Runnable r) { 
        this.onUpdate = r; 
    }
    
    public Runnable getOnUpdate() {
        return onUpdate;
    }
    
    public StringProperty timerTextProperty() { 
        return timerUI.timerTextProperty(); 
    }
    
    public StringProperty statusTextProperty() { 
        return timerUI.statusTextProperty(); 
    }
    
    public StringProperty startPauseTextProperty() { 
        return timerUI.startPauseTextProperty(); 
    }
    
    public StringProperty nextPhaseTextProperty() { 
        return timerUI.nextPhaseTextProperty(); 
    }
    
    public boolean isWaitingForNextPhase() {
        return waitingForNextPhase;
    }
    
    public boolean isShowingStats() {
        return showingStats;
    }
    
    public boolean isWork() {
        return isWork;
    }
    
    public int getSessionsCompleted() {
        return sessionsCompleted;
    }
    
    public int getSessions() {
        return sessions;
    }
    
    public int getRemainingTime() {
        return remainingTime;
    }
    
    public int getStudyTime() {
        return studyTime;
    }
    
    public int getBreakTime() {
        return breakTime;
    }
    
    public String getStudyInput() {
        return studyInput;
    }
    
    public String getBreakInput() {
        return breakInput;
    }
    
    public String getSessionsInput() {
        return sessionsInput;
    }
    
    public TimerInterface getTimerUI() {
        return timerUI;
    }
}
