package com.example.desktoppet;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Interface for Timer UI operations.
 * This allows for different UI implementations while maintaining consistent functionality.
 */
public interface TimerInterface {
    /**
     * Display the timer window
     */
    void changeScene();
    
    /**
     * Update the timer display
     */
    void updateTimerDisplay();
    
    /**
     * Update the session label
     */
    void updateSessionLabel();
    
    /**
     * Show the next phase button
     */
    void showNextPhaseButton();
    
    /**
     * Update the button state based on the timer state
     */
    void updateButtons();
    
    /**
     * Update the timer label color based on the timer state
     */
    void updateTimerLabelColor();
    
    /**
     * Show the statistics after completing all sessions
     */
    void showStats();
    
    /**
     * Get timer label text property
     * @return the text property for timer label
     */
    StringProperty timerTextProperty();
    
    /**
     * Get status label text property
     * @return the text property for status label
     */
    StringProperty statusTextProperty();
    
    /**
     * Get start/pause button text property
     * @return the text property for start/pause button
     */
    StringProperty startPauseTextProperty();
    
    /**
     * Get next phase button text property
     * @return the text property for next phase button
     */
    StringProperty nextPhaseTextProperty();
    
    /**
     * Get session label
     * @return the session label component
     */
    Label getSessionLabel();
    
    /**
     * Get start/pause button
     * @return the start/pause button component
     */
    Button getStartPauseButton();
    
    /**
     * Get reset button
     * @return the reset button component
     */
    Button getResetButton();
    
    /**
     * Get next phase button
     * @return the next phase button component
     */
    Button getNextPhaseButton();
}
