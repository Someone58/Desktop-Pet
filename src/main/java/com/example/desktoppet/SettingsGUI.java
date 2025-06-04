package com.example.desktoppet;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SettingsGUI implements SettingsInterface {
    private final Settings settingsLogic;
    private final PetController petController;
    
    // UI Components
    private final Label speedLabel = new Label("Speed (Changes the maximal speed): ");
    private final Slider speedSlider = new Slider(0, 5, 1);
    private final Label activityLabel = new Label("Activity (Changes the chance for the pet to move): ");
    private final Slider activitySlider = new Slider(0.1, 1, 0.5);
    private final Label sizeLabel = new Label("Size (Changes the size of the pet): ");
    private final Slider sizeSlider = new Slider(30, 300, 100);
    private final Label heightLabel = new Label("Height (Changes the height of the pet): ");
    private final Slider heightSlider = new Slider(0, 100, 0);

    private final Button darkmode = new Button("Dark Mode");
    private boolean isDarkMode = false;
    
    public SettingsGUI(Settings settingsLogic, PetController petController) {
        this.settingsLogic = settingsLogic;
        this.petController = petController;
        
        // Initialize UI components
        setupComponents();
    }
    
    private void setupComponents() {
        // Configure sliders
        speedSlider.setShowTickLabels(true);
        activitySlider.setShowTickLabels(true);
        sizeSlider.setShowTickLabels(true);
        sizeSlider.setMajorTickUnit(100);
        heightSlider.setShowTickLabels(true);
        
        // Add listeners to notify the logic component of changes
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> 
                settingsLogic.onSpeedChanged(newValue.doubleValue()));
        
        activitySlider.valueProperty().addListener((observable, oldValue, newValue) -> 
                settingsLogic.onActivityChanged(newValue.doubleValue()));
        
        sizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> 
                settingsLogic.onSizeChanged(newValue.doubleValue()));
        
        heightSlider.valueProperty().addListener((observable, oldValue, newValue) -> 
                settingsLogic.onHeightChanged(newValue.doubleValue()));

        // Setup darkmode button action
        darkmode.setOnAction(e -> toggleDarkMode());
    }
    
    @Override
    public void changeScene() {
        Stage stage = petController.getStage();
        Scene windowScene = petController.getWindowScene();

        Button backButton = new Button("Back");

        VBox rootVBox = new VBox(5);
        rootVBox.getChildren().addAll(
                backButton,
                speedLabel,
                speedSlider,
                activityLabel,
                activitySlider,
                sizeLabel,
                sizeSlider,
                heightLabel,
                heightSlider,
                darkmode
        );

        Group root = new Group(rootVBox);
        Scene scene = new Scene(root, 300, 400);

        // Register the scene with StyleManager to handle stylesheets
        StyleManager.getInstance().registerScene(scene);

        stage.setTitle("Settings");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

        backButton.setOnAction(e -> {
            stage.setScene(windowScene);
            stage.setTitle("Apps");
        });
    }
    
    @Override
    public void setSliderValues(double speed, double activity, double size, double height) {
        speedSlider.setValue(speed);
        activitySlider.setValue(activity);
        sizeSlider.setValue(size);
        heightSlider.setValue(height);
    }
    
    @Override
    public double getSpeedValue() {
        return speedSlider.getValue();
    }
    
    @Override
    public double getActivityValue() {
        return activitySlider.getValue();
    }
    
    @Override
    public double getSizeValue() {
        return sizeSlider.getValue();
    }
    
    @Override
    public double getHeightValue() {
        return heightSlider.getValue();
    }

    /**
     * Toggles between dark mode and light mode
     */
    private void toggleDarkMode() {
        isDarkMode = !isDarkMode;

        if (isDarkMode) {
            // Use PetController's setCss method to update all views
            petController.setCss("/darkmode.css");
            darkmode.setText("Light Mode");
        } else {
            petController.setCss("/application.css");
            darkmode.setText("Dark Mode");
        }

        // Make sure all existing scenes are updated immediately
        updateCurrentScenes();
    }

    /**
     * Updates the stylesheet of all existing scenes
     */
    private void updateCurrentScenes() {
        // Update the current scene
        Scene currentScene = petController.getStage().getScene();
        String newStylesheet = petController.getCss();

        if (currentScene != null) {
            // Clear existing stylesheets and add the new one
            currentScene.getStylesheets().clear();
            currentScene.getStylesheets().add(getClass().getResource(newStylesheet).toExternalForm());
        }

        // Update the window scene
        Scene windowScene = petController.getWindowScene();
        if (windowScene != null) {
            windowScene.getStylesheets().clear();
            windowScene.getStylesheets().add(getClass().getResource(newStylesheet).toExternalForm());
        }

        // Update the mini timer window if it's open
        MiniTimerWindowInterface miniTimer = MiniTimerWindow.getMiniTimerUI();
        if (miniTimer != null && miniTimer.isShowing()) {
            try {
                // Use reflection to access the miniStage's scene since it's private
                java.lang.reflect.Field miniStageField = MiniTimerWindowGUI.class.getDeclaredField("miniStage");
                miniStageField.setAccessible(true);
                Stage miniStage = (Stage) miniStageField.get(null); // static field

                if (miniStage != null && miniStage.getScene() != null) {
                    Scene miniScene = miniStage.getScene();
                    miniScene.getStylesheets().clear();
                    String cssUrl = getClass().getResource(newStylesheet).toExternalForm();
                    if (cssUrl != null) {
                        miniScene.getStylesheets().add(cssUrl);
                    }
                }
            } catch (Exception e) {
                // In case of reflection error, fallback to StyleManager
                // The next repaint should apply the style via StyleManager
            }
        }
    }
}
