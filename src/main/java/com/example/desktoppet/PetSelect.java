package com.example.desktoppet;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PetSelect {
    PetController petController;
    Settings settings;
    double height = 100;
    double size = 100;
    Stage stage;

    Button sharkSelect = new Button("Shark");
    Image sharkIdle;
    String sharkName = "Shark";

    Button hedgehogSelect = new Button("Hedgehog");
    Image hedgehogIdle;
    String hedgehogName = "Hedgehog";

    PetAnimation pet;

    public PetSelect(PetController petController) {
        this.petController = petController;

        pet = new PetAnimation(petController);
        sharkIdle = petController.getSharkIdle();
        hedgehogIdle = petController.getHedgehogIdle();
        settings = petController.getSettings();

        settings.speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            pet.setxSpeed(newValue.doubleValue());
        });

        settings.activitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            pet.setActivity(newValue.doubleValue());
        });

        settings.sizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.size = newValue.doubleValue();
            pet.setSizeHeight(newValue.doubleValue(), height);
        });

        settings.heightSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double reversedValue = 100 - newValue.doubleValue();
            this.height = reversedValue;
            pet.setSizeHeight(size, reversedValue);
        });
    }

    public void changeScene() {
        stage = petController.getStage();

        Button backButton = new Button("Back");

        VBox petsVBox = new VBox(sharkSelect, hedgehogSelect);

        VBox rootVBox = new VBox(5);
        rootVBox.getChildren().addAll(
                backButton,
                petsVBox
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
            stage.setScene(petController.getWindowScene());
            stage.setTitle("Apps");
        });

        sharkSelect.setOnAction(e -> {
            pet.setupPet(sharkIdle, sharkName);
        });

        hedgehogSelect.setOnAction(e -> {
            pet.setupPet(hedgehogIdle, hedgehogName);
        });
    }

    public void startPet() {
        pet = new PetAnimation(petController);
        pet.setupStage();
        System.out.println("clicked on button");
    }

}
