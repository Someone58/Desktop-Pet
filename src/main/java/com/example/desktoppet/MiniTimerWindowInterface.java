package com.example.desktoppet;

/**
 * Interface for Mini Timer Window UI operations.
 * This allows for different UI implementations while maintaining consistent functionality.
 */
public interface MiniTimerWindowInterface {
    /**
     * Create and display the mini timer window
     * @param timer the timer logic instance
     * @param petController the pet controller instance
     */
    void createMiniTimerWindow(Timer timer, PetController petController);
    
    /**
     * Update the UI based on button state
     * @param buttonState the current state of timer buttons
     */
    void updateButtonsUI(Timer.TimerButtonState buttonState);
    
    /**
     * Update the timer label color based on its content
     * @param timerText the current text in the timer label
     */
    void updateTimerLabelColor(String timerText);
    
    /**
     * Close the mini timer window
     */
    void close();
    
    /**
     * Bring the mini timer window to the front
     */
    void toFront();
    
    /**
     * Check if the mini timer window is currently showing
     * @return true if the window is showing, false otherwise
     */
    boolean isShowing();
}
