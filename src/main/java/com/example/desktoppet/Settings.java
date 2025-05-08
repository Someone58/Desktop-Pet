package com.example.desktoppet;


import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Settings {
    Label speedLabel = new Label("Speed (Changes the maximal speed): ");
    Slider speedSlider = new Slider(0, 5, 1);
    Label activityLabel = new Label("Activity (Changes the chance for the pet to move): ");
    Slider activitySlider = new Slider(0.1, 1, 0.5);
    Label sizeLabel = new Label("Size (Changes the size of the pet): ");
    Slider sizeSlider = new Slider(30, 300, 100);
    Label heightLabel = new Label("Hight (Changes the hight of the pet): ");
    Slider heightSlider = new Slider(0, 100, 0);

    public void changeScene(Stage stage, Scene windowScene) {

//        speedSlider.setBlockIncrement(10);
        speedSlider.setShowTickLabels(true);
        activitySlider.setShowTickLabels(true);
        sizeSlider.setShowTickLabels(true);
        sizeSlider.setMajorTickUnit(100);
        heightSlider.setShowTickLabels(true);
//        speedSlider.setShowTickMarks(true);

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

        stage.setTitle("Select Pet");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

        backButton.setOnAction(e -> {
            stage.setScene(windowScene);
            stage.setTitle("Apps");
        });


    }

}
