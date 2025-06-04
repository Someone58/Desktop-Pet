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
                heightSlider
        );

        Group root = new Group(rootVBox);
        Scene scene = new Scene(root, 300, 400);

        String css = this.getClass().getResource("/application.css").toExternalForm();
        if (css != null) {
            scene.getStylesheets().add(css);
        }

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
}
