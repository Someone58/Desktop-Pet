package com.example.desktoppet.UI;

import com.example.desktoppet.*;
import com.example.desktoppet.Controller.MiniTimerWindow;
import com.example.desktoppet.Controller.Settings;
import com.example.desktoppet.Interfaces.MiniTimerWindowInterface;
import com.example.desktoppet.Interfaces.SettingsInterface;
import com.example.desktoppet.Model.PetData;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Objects;

public class SettingsGUI implements SettingsInterface {
    private final Settings settingsLogic;
    private final PetData petController;
    
    // UI Components
    private final Label speedLabel = new Label("Speed   ");
    private final Label speedTextLabel = new Label("(Changes the maximal speed)");
    private final Slider speedSlider = new Slider(0, 5, 1);
    private final Label activityLabel = new Label("Activity");
    private final Label activityTextLabel = new Label("(Changes the chance for the pet to move)");
    private final Slider activitySlider = new Slider(0.1, 1, 0.5);
    private final Label sizeLabel = new Label("Size    ");
    private final Label sizeTextLabel = new Label("(Changes the size of the pet)");
    private final Slider sizeSlider = new Slider(30, 300, 100);
    private final Label heightLabel = new Label("Height  ");
    private final Label heightTextLabel = new Label("(Changes the height of the pet)");
    private final Slider heightSlider = new Slider(0, 100, 0);

    private final Button darkmode = new Button("Dark Mode");
    private final Button pinWindow = new Button("");
    private boolean isDarkMode = false;
    private boolean windowOnTop = true;
    private final Image pinButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pin.png")));
    private final ImageView pinButtonImgView = new ImageView(pinButtonImg);
    private final Image unpinButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/unpin.png")));
    private final ImageView unpinButtonImgView = new ImageView(unpinButtonImg);
    
    public SettingsGUI(Settings settingsLogic, PetData petController) {
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

        // Setup button actions
        darkmode.setOnAction(e -> toggleDarkMode());
        pinWindow.setOnAction(e -> toggleAlwaysOnTop());

        // Initialize pin window button text
        updatePinWindowText();
    }
    
    @Override
    public void changeScene() {
        Stage stage = petController.getStage();
        Scene windowScene = petController.getWindowScene();

        Label settingsTitle = new Label(" Settings");
        settingsTitle.setId("settingsTitle");

        Button backButton = new Button("");
        Image backButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/arrow.png")));
        ImageView backButtonImgView = new ImageView(backButtonImg);
        backButtonImgView.setFitWidth(15);
        backButtonImgView.setFitHeight(15);
        backButtonImgView.setPreserveRatio(true);
        backButton.setGraphic(backButtonImgView);
        backButton.setId("backButton");

        speedTextLabel.setId("speedTextLabel");
        activityTextLabel.setId("activityTextLabel");
        sizeTextLabel.setId("sizeTextLabel");
        heightTextLabel.setId("heightTextLabel");

        speedSlider.setShowTickMarks(true);
        speedSlider.setShowTickLabels(true);

        activitySlider.setShowTickMarks(true);
        activitySlider.setShowTickLabels(true);

        sizeSlider.setShowTickMarks(true);
        sizeSlider.setShowTickLabels(true);

        heightSlider.setShowTickMarks(true);
        heightSlider.setShowTickLabels(true);

        HBox navigationButtons = new HBox(25);
        navigationButtons.getChildren().addAll(
                backButton,
                pinWindow);

        HBox speedHBox = new HBox(5);
        speedHBox.getChildren().addAll(
                speedLabel,
                speedTextLabel
        );

        HBox activityHBox = new HBox(5);
        activityHBox.getChildren().addAll(
                activityLabel,
                activityTextLabel
        );

        HBox sizeHBox = new HBox(5);
        sizeHBox.getChildren().addAll(
                sizeLabel,
                sizeTextLabel
        );

        HBox heightHBox = new HBox(5);
        heightHBox.getChildren().addAll(
                heightLabel,
                heightTextLabel
        );


        VBox rootVBox = new VBox(5);
        rootVBox.getChildren().addAll(
                settingsTitle,
                navigationButtons,
                speedHBox,
                speedSlider,
                activityHBox,
                activitySlider,
                sizeHBox,
                sizeSlider,
                heightHBox,
                heightSlider
        );

        rootVBox.setId("rootVBox");

        Group root = new Group(rootVBox);
        Scene scene = new Scene(root, 300, 400);

        scene.setFill(Color.web("#B8CCCB"));

        // Register the scene with StyleManager to handle stylesheets
        StyleManager.getInstance().registerScene(scene);

        stage.setTitle("Settings");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);


        pinButtonImgView.setFitWidth(15);
        pinButtonImgView.setFitHeight(15);
        pinButtonImgView.setPreserveRatio(true);
        pinWindow.setGraphic(pinButtonImgView);
        pinWindow.setId("pinWindow");


        unpinButtonImgView.setFitWidth(15);
        unpinButtonImgView.setFitHeight(15);
        unpinButtonImgView.setPreserveRatio(true);

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
            petController.setCss("/styling/darkmode.css");
            darkmode.setText("Light Mode");
        } else {
            petController.setCss("/styling/styles.css");
            darkmode.setText("Dark Mode");
        }

        // Make sure all existing scenes are updated immediately
        updateCurrentScenes();
    }

    /**
     * Toggles the always-on-top state of the window
     */
    private void toggleAlwaysOnTop() {
        windowOnTop = !windowOnTop;
        Stage stage = petController.getStage();
        stage.setAlwaysOnTop(windowOnTop);
        updatePinWindowText();
    }

    /**
     * Updates the text of the pin window button based on current state
     */
    private void updatePinWindowText() {
        pinWindow.setText("");
        if (windowOnTop) {
            pinWindow.setGraphic(unpinButtonImgView);
        } else {
            pinWindow.setGraphic(pinButtonImgView);
        }
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
