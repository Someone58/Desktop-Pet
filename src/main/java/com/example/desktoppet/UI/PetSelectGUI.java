package com.example.desktoppet.UI;

import com.example.desktoppet.Model.PetData;
import com.example.desktoppet.Controller.PetSelect;
import com.example.desktoppet.Interfaces.PetSelectInterface;
import com.example. desktoppet.UI.WindowGUI;
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
public class PetSelectGUI extends BaseGUI implements PetSelectInterface {
    private final PetSelect petSelectLogic;

    private Button sharkSelect = new Button("");
    private Button hedgehogSelect = new Button("");
    private Button dogSelect = new Button("");
    private Stage stage;
    private String currentPet;
    private WindowGUI windowGUI;


    public PetSelectGUI(PetSelect petSelectLogic, PetData petController) {
        super(petController);
        this.petSelectLogic = petSelectLogic;
        this.currentPet = "Shark";
        // Initialisiere die UI-Komponenten
        this.sharkSelect = new Button("");
        this.hedgehogSelect = new Button("");
        this.dogSelect = new Button("");
        initializeUI();
    }

    @Override
    public void initializeUI() {
        // Stelle sicher, dass wir eine gültige Stage haben
        this.stage = petController.getStage();
        // Füge einen Debug-Log hinzu
        System.out.println("PetSelectGUI initialized with stage: " + stage);
    }

    private void updatePetButtonStyles() {
        // Setze zuerst alle Pet-Buttons auf die nicht-ausgewählte Farbe
        sharkSelect.setStyle("-fx-background-color: #566E73;");
        hedgehogSelect.setStyle("-fx-background-color: #566E73;");
        dogSelect.setStyle("-fx-background-color: #566E73;");

        // Setze die ausgewählte Farbe für das aktuelle Pet
        switch (currentPet) {
            case "Shark" -> sharkSelect.setStyle("-fx-background-color: #152F38;");
            case "Hedgehog" -> hedgehogSelect.setStyle("-fx-background-color: #152F38;");
            case "Dog" -> dogSelect.setStyle("-fx-background-color: #152F38;");
        }
    }

    public void setWindowGUI(WindowGUI windowGUI) {
        this.windowGUI = windowGUI;
    }

    public void setupPet(String petName) {
        petSelectLogic.setupPet(petName);  // Verwendung von petSelectLogic statt pet
        if (windowGUI != null) {
            windowGUI.updatePetGraphic(petName);
        }
    }

    @Override
    public void changeScene() {
        // Stelle sicher, dass wir eine gültige Stage haben
        if (stage == null) {
            stage = petController.getStage();
        }

        Label petSelectionTitle = new Label(" Pets");
        petSelectionTitle.setId("petSelectionTitle");

        Button backButton = new Button("");
        configureButton(backButton, "/arrow.png", 15, 15, "backButton");

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
        petsHBox.setTranslateX(11); // Fix für die Position

        VBox petButtons = new VBox(30);
        petButtons.getChildren().addAll(
                backButton,
                petsHBox
        );
        petButtons.setId("petButtons");
        petButtons.setTranslateX(5); // Fix für die Position
        petButtons.setTranslateY(1); // Fix für die Position

        VBox rootVBox = new VBox(5);
        rootVBox.getChildren().addAll(
                petSelectionTitle,
                petButtons
        );
        rootVBox.setId("rootVBox");
        rootVBox.setTranslateX(-2); // Fix für die Position
        rootVBox.setTranslateY(2); // Fix für die Position

        Group root = new Group(rootVBox);
        root.setId("scene");

        Scene scene = new Scene(root, 300, 200);
        scene.setFill(Color.web("#B8CCCB"));

        // Apply CSS
        String css = petController.getCss();
        if (css != null) {
            scene.getStylesheets().add(css);
        }

        // Setze die Scene und zeige die Stage
        System.out.println("Setting scene for stage: " + stage);
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

        updatePetButtonStyles(); // Aktualisiert die Styles beim Öffnen der Scene
    }

    @Override
    public void applyPetSelection(String petName) {
        this.currentPet = petName;
        updatePetButtonStyles();
        petSelectLogic.setupPet(petName);

        if (windowGUI != null) {
            windowGUI.updatePetGraphic(petName);
        }
    }
}