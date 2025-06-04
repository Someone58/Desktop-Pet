package com.example.desktoppet;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Handles UI construction and components for the pet selection window
 */
public class PetSelectGUI implements PetSelectInterface {
    private final PetSelect petSelectLogic;
    private final PetController petController;

    private Button sharkSelect = new Button("Shark");
    private Button hedgehogSelect = new Button("Hedgehog");
    private Button dogSelect = new Button("Dog");
    private Stage stage;

    public PetSelectGUI(PetSelect petSelectLogic, PetController petController) {
        this.petSelectLogic = petSelectLogic;
        this.petController = petController;
        initializeUI();
    }

    @Override
    public void initializeUI() {
        // Get references to the stage
        this.stage = petController.getStage();
    }

    @Override
    public void changeScene() {
        Button backButton = new Button("Back");

        VBox petsVBox = new VBox(sharkSelect, hedgehogSelect, dogSelect);

        VBox rootVBox = new VBox(5);
        rootVBox.getChildren().addAll(
                backButton,
                petsVBox
        );

        Group root = new Group(rootVBox);
        Scene scene = new Scene(root, 300, 400);

        String css = petController.getCss();
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
            applyPetSelection(petSelectLogic.getSharkName());
        });

        hedgehogSelect.setOnAction(e -> {
            applyPetSelection(petSelectLogic.getHedgehogName());
        });

        dogSelect.setOnAction(e -> {
            applyPetSelection(petSelectLogic.getDogName());
        });
    }

    @Override
    public void applyPetSelection(String petName) {
        petSelectLogic.setupPet(petName);
    }
}
