package com.example.desktoppet.UI;

import com.example.desktoppet.Model.PetData;
import com.example.desktoppet.Controller.PetSelect;
import com.example.desktoppet.Interfaces.PetSelectInterface;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Handles UI construction and components for the pet selection window
 */
public class PetSelectGUI implements PetSelectInterface {
    private final PetSelect petSelectLogic;
    private final PetData petController;

    private Button sharkSelect = new Button("");
    private Button hedgehogSelect = new Button("");
    private Button dogSelect = new Button("");
    private Stage stage;

    public PetSelectGUI(PetSelect petSelectLogic, PetData petController) {
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
        Label petSelectionTitle = new Label("Pets");
        petSelectionTitle.setId("petSelectionTitle");

        Button backButton = new Button("");
        Image backButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/arrow.png")));
        ImageView backButtonImgView = new ImageView(backButtonImg);
        backButtonImgView.setFitWidth(15);
        backButtonImgView.setFitHeight(15);
        backButtonImgView.setPreserveRatio(true);
        backButton.setGraphic(backButtonImgView);
        backButton.setId("backButton");

        Image sharkButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/shark.png")));
        ImageView sharkButtonImgView = new ImageView(sharkButtonImg);
        sharkButtonImgView.setFitWidth(50);
        sharkButtonImgView.setFitHeight(50);
        sharkButtonImgView.setPreserveRatio(true);
        sharkSelect.setGraphic(sharkButtonImgView);
        sharkSelect.setId("shark");

        Image hedgehogButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/hedgehog.png")));
        ImageView hedgehogButtonImgView = new ImageView(hedgehogButtonImg);
        hedgehogButtonImgView.setFitWidth(50);
        hedgehogButtonImgView.setFitHeight(50);
        hedgehogButtonImgView.setPreserveRatio(true);
        hedgehogSelect.setGraphic(hedgehogButtonImgView);
        hedgehogSelect.setId("hedgehog");

        Image dogButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/dog.png")));
        ImageView dogButtonImgView = new ImageView(dogButtonImg);
        dogButtonImgView.setFitWidth(50);
        dogButtonImgView.setFitHeight(50);
        dogButtonImgView.setPreserveRatio(true);
        dogSelect.setGraphic(dogButtonImgView);
        dogSelect.setId("dog");


        HBox petsHBox = new HBox(19);
        petsHBox.getChildren().addAll(
                sharkSelect,
                hedgehogSelect,
                dogSelect
        );

        petsHBox.setId("petsHBox");

        VBox petButtons = new VBox(30);
        petButtons.getChildren().addAll(
                backButton,
                petsHBox
        );

        petButtons.setId("petButtons");

        VBox rootVBox = new VBox(5);
        rootVBox.getChildren().addAll(
                petSelectionTitle,
                petButtons
        );

        rootVBox.setId("rootVBox");

        Group root = new Group(rootVBox);
        Scene scene = new Scene(root, 300, 200);

        root.setId("scene");

        scene.setFill(Color.web("#B8CCCB"));

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
