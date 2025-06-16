package com.example.desktoppet.Interfaces;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Interface for Window UI operations.
 * This allows for different UI implementations while maintaining consistent functionality.
 */
public interface WindowUI {
    /**
     * Initialize the UI components
     */
    void initializeUI();
    
    /**
     * Build and display the main window
     */
    void openWindow();
    
    /**
     * Get the stage associated with this UI
     * @return the JavaFX stage
     */
    Stage getStage();
    
    /**
     * Get the current scene
     * @return the JavaFX scene
     */
    Scene getScene();
    
    /**
     * Set the window always on top status
     * @param onTop true to set window always on top, false otherwise
     */
    void setAlwaysOnTop(boolean onTop);
}
