package com.example.desktoppet;

/**
 * Handles settings logic and data for the pet application.
 * UI is handled by the SettingsGUI class.
 */
public class Settings {
    private final PetController petController;
    private final SettingsInterface settingsUI;
    
    // Default values
    private double speed = 1.0;
    private double activity = 0.5;
    private double size = 100.0;
    private double height = 0.0;
    
    public Settings(PetController petController) {
        this.petController = petController;
        this.settingsUI = new SettingsGUI(this, petController);
    }
    
    /**
     * Display the settings window
     */
    public void changeScene() {
        // Update UI with current values
        settingsUI.setSliderValues(speed, activity, size, height);
        settingsUI.changeScene();
    }
    
    /**
     * Called when speed slider value changes
     * @param newValue the new speed value
     */
    public void onSpeedChanged(double newValue) {
        this.speed = newValue;
        if (petController != null) {
            PetAnimation petAnimation = getPetAnimation();
            if (petAnimation != null) {
                petAnimation.setxSpeed(newValue);
            }
        }
    }
    
    /**
     * Called when activity slider value changes
     * @param newValue the new activity value
     */
    public void onActivityChanged(double newValue) {
        this.activity = newValue;
        if (petController != null) {
            PetAnimation petAnimation = getPetAnimation();
            if (petAnimation != null) {
                petAnimation.setActivity(newValue);
            }
        }
    }
    
    /**
     * Called when size slider value changes
     * @param newValue the new size value
     */
    public void onSizeChanged(double newValue) {
        this.size = newValue;
        if (petController != null) {
            petController.setPetSize(newValue);
            PetAnimation petAnimation = getPetAnimation();
            if (petAnimation != null) {
                petAnimation.setSizeHeight(size, height);
            }
        }
    }
    
    /**
     * Called when height slider value changes
     * @param newValue the new height value
     */
    public void onHeightChanged(double newValue) {
        double reversedValue = 100 - newValue;
        this.height = reversedValue;
        if (petController != null) {
            PetAnimation petAnimation = getPetAnimation();
            if (petAnimation != null) {
                petAnimation.setSizeHeight(size, reversedValue);
            }
        }
    }
    
    /**
     * Get the pet animation from the PetSelect class in PetController
     * @return the PetAnimation instance or null if not available
     */
    private PetAnimation getPetAnimation() {
        try {
            return petController.getStage().getUserData() instanceof PetSelect petSelect ? 
                petSelect.getPetAnimation() : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Get the UI component for settings
     * @return the settings UI implementation
     */
    public SettingsInterface getSettingsUI() {
        return settingsUI;
    }
    
    // Getters for current values
    public double getSpeed() {
        return speed;
    }
    
    public double getActivity() {
        return activity;
    }
    
    public double getSize() {
        return size;
    }
    
    public double getHeight() {
        return height;
    }
}
