package com.example.desktoppet;

/**
 * Interface for Settings UI operations.
 * This allows for different UI implementations while maintaining consistent functionality.
 */
public interface SettingsInterface {
    /**
     * Display the settings window
     */
    void changeScene();
    
    /**
     * Set the values of sliders based on current settings
     * @param speed current speed value
     * @param activity current activity value
     * @param size current size value
     * @param height current height value
     */
    void setSliderValues(double speed, double activity, double size, double height);
    
    /**
     * Get the current speed slider value
     * @return current speed value
     */
    double getSpeedValue();
    
    /**
     * Get the current activity slider value
     * @return current activity value
     */
    double getActivityValue();
    
    /**
     * Get the current size slider value
     * @return current size value
     */
    double getSizeValue();
    
    /**
     * Get the current height slider value
     * @return current height value
     */
    double getHeightValue();
}
