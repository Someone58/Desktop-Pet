package com.example.desktoppet;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.text.Element;

public class PetSelect {
    Stage primaryStage;
    Rectangle2D screenBounds;

    Button sharkSelect = new Button("Shark");
    Image sharkIdle = new Image(getClass().getResource("/Pets/sharkIdle.png").toExternalForm());

    Button hedgehogSelect = new Button("Hedgehog");
    Image hedgehogIdle = new Image(getClass().getResource("/Pets/hedgehogIdle.png").toExternalForm());

    PetAnimation pet = new PetAnimation(screenBounds, 1.5);

    public PetSelect(Stage primatyStage, Rectangle2D screenBounds) {
        this.primaryStage = primatyStage;
    }

    public void changeScene(Stage stage, Scene windowScene) {

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
            stage.setScene(windowScene);
            stage.setTitle("Apps");
        });

        sharkSelect.setOnAction(e -> {
            pet.setupPet(sharkIdle);
        });

        hedgehogSelect.setOnAction(e -> {
            pet.setupPet(hedgehogIdle);
        });
    }

    public void startPet() {
        pet.setupStage(primaryStage, sharkIdle);
        System.out.println("clicked on button");
    }

}
