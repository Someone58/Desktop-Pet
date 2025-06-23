package com.example.desktoppet.UI;

import com.example.desktoppet.*;
import com.example.desktoppet.Controller.Chat;
import com.example.desktoppet.Controller.NetworkConnector;
import com.example.desktoppet.Controller.PetSelect;
import com.example.desktoppet.Controller.Timer;
import com.example.desktoppet.Interfaces.WindowUI;
import com.example.desktoppet.Model.PetData;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.util.Objects;

/**
 * Handles UI construction and components for the main window
 */
public class WindowGUI implements WindowUI {
    private PetData petController;
    private Stage stage;
    private Scene scene;

    // UI Components
    private TextArea connectionStatus;
    private Button chatButton;
    private Button networkButton;
    private Button timerButton;
    private Button petButton;
    private Button settingsButton;

    // Component handlers
    private Chat chat;
    private NetworkConnector networkConnector;
    private Timer timer;
    private PetSelect petSelect;
    private boolean windowOnTop = true;

    private Label currentStatus = new Label("");
    private Image disconnectedImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/disconnected.png")));
    private ImageView disconnectedImgView = new ImageView(disconnectedImg);
    private Image connectedImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/connected.png")));
    private ImageView connectedImgView = new ImageView(connectedImg);
    private Label currentPetLabel;
    private String currentPetName;
    private Image sharkImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/shark.png")));
    private ImageView sharkImgView = new ImageView(sharkImg);
    private Image hedgehogImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/hedgehog.png")));
    private ImageView hedgehogImgView = new ImageView(hedgehogImg);
    private Image dogImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/dog.png")));
    private ImageView dogImgView = new ImageView(dogImg);


    public WindowGUI(PetData petController) {
        this.petController = petController;
        this.stage = petController.getStage();

        // Initialize UI components
        connectionStatus = new TextArea();
        connectionStatus.setId("connectionStatus");

        petButton = new Button("");
        Image petButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pet.png")));
        ImageView petButtonImgView = new ImageView(petButtonImg);
        petButtonImgView.setFitWidth(25);
        petButtonImgView.setFitHeight(25);
        petButtonImgView.setPreserveRatio(true);
        petButton.setGraphic(petButtonImgView);
        petButton.setId("petButton");

        networkButton = new Button("");
        Image networkButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/network.png")));
        ImageView networkButtonImgView = new ImageView(networkButtonImg);
        networkButtonImgView.setFitWidth(25);
        networkButtonImgView.setFitHeight(25);
        networkButtonImgView.setPreserveRatio(true);
        networkButton.setGraphic(networkButtonImgView);
        networkButton.setId("networkButton");

        chatButton = new Button("");
        Image chatButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/chat.png")));
        ImageView chatButtonImgView = new ImageView(chatButtonImg);
        chatButtonImgView.setFitWidth(25);
        chatButtonImgView.setFitHeight(25);
        chatButtonImgView.setPreserveRatio(true);
        chatButton.setGraphic(chatButtonImgView);
        chatButton.setId("chatButton");

        timerButton = new Button("");
        Image timerButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/timer.png")));
        ImageView timerButtonImgView = new ImageView(timerButtonImg);
        timerButtonImgView.setFitWidth(25);
        timerButtonImgView.setFitHeight(25);
        timerButtonImgView.setPreserveRatio(true);
        timerButton.setGraphic(timerButtonImgView);
        timerButton.setId("timerButton");

        settingsButton = new Button("");
        Image settingsButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/settings.png")));
        ImageView settingsButtonImgView = new ImageView(settingsButtonImg);
        settingsButtonImgView.setFitWidth(25);
        settingsButtonImgView.setFitHeight(25);
        settingsButtonImgView.setPreserveRatio(true);
        settingsButton.setGraphic(settingsButtonImgView);
        settingsButton.setId("settingsButton");

        // Initialize handlers with their respective UI implementations
        chat = new Chat(petController); // This now uses ChatGUI internally
        networkConnector = new NetworkConnector(petController);
        timer = new Timer(petController);
        petSelect = new PetSelect(petController);
        ((PetSelectGUI)petSelect.getPetSelectUI()).setWindowGUI(this);
    }

    @Override
    public void initializeUI() {
        stage.setAlwaysOnTop(true);

        SharedTextAreaManager statusManager = petController.getStatusManager();

        // Bilder für Status konfigurieren
        connectedImgView.setFitWidth(15);
        connectedImgView.setFitHeight(15);
        connectedImgView.setPreserveRatio(true);

        disconnectedImgView.setFitWidth(15);
        disconnectedImgView.setFitHeight(15);
        disconnectedImgView.setPreserveRatio(true);

        connectionStatus.setEditable(false);
        connectionStatus.setPrefRowCount(1);
        connectionStatus.setPrefHeight(25);
        connectionStatus.setMaxWidth(180);
        connectionStatus.setMaxHeight(15);
        connectionStatus.getStyleClass().add("disconnected-status");
        statusManager.registerTextArea(connectionStatus);
        connectionStatus.setText("Disconnected");

        // Status-Label konfigurieren
        currentStatus = new Label("Disconnected");
        currentStatus.setId("currentStatus");
        currentStatus.setGraphic(disconnectedImgView);
        currentStatus.getStyleClass().add("disconnected-status");

        // NetworkManager Status-Änderungen überwachen;
        statusManager.sharedContentProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                if ("Connected".equals(newValue)) {
                    connectionStatus.setText("Connected");
                    currentStatus.setGraphic(connectedImgView);
                    currentStatus.getStyleClass().remove("disconnected-status");
                    currentStatus.getStyleClass().add("connected-status");
                } else if ("Disconnected".equals(newValue)) {
                    connectionStatus.setText("Disconnected");
                    currentStatus.setGraphic(disconnectedImgView);
                    currentStatus.getStyleClass().remove("connected-status");
                    currentStatus.getStyleClass().add("disconnected-status");
                }
            });
        });
    }

    public void updatePetGraphic(String newPetName) {
        currentPetName = newPetName;

        // Aktualisiere das Bild
        ImageView selectedPetImage;
        String displayName;
        switch (newPetName) {
            case "Dog" -> {
                selectedPetImage = dogImgView;
                displayName = "Mister Dog";
            }
            case "Hedgehog" -> {
                selectedPetImage = hedgehogImgView;
                displayName = "Mister Hedgehog";
            }
            default -> {
                selectedPetImage = sharkImgView;
                displayName = "Mister Shark";
            }
        }

        selectedPetImage.setFitWidth(60);
        selectedPetImage.setFitHeight(60);
        selectedPetImage.setPreserveRatio(true);
        currentPetLabel.setGraphic(selectedPetImage);

        // Aktualisiere den Namen
        Label petNameLabel = (Label) ((VBox) currentPetLabel.getParent().getParent()).getChildren().get(0);
        petNameLabel.setText(displayName);
    }


    @Override
    public void openWindow() {

        Label homeTitle = new Label(" Home");
        homeTitle.setId("homeTitle");


        Label petName = new Label("Mister Pet");
        petName.setId("petName");

        currentPetLabel = new Label();
        currentPetLabel.setId("currentPet");
        currentPetLabel.setGraphic(sharkImgView);
        currentPetName = "Shark"; // Startzustand

        HBox connectionHBox = new HBox(0);
        connectionHBox.getChildren().addAll(
                currentStatus,
                connectionStatus
        );

        connectionHBox.setId("connectionHBox");

        VBox describitionVBox = new VBox(5);
        describitionVBox.getChildren().addAll(
                petName,
                connectionHBox
        );

        describitionVBox.setId("describitionVBox");

        HBox currentPetHBox = new HBox(5);
        currentPetHBox.getChildren().addAll(
                currentPetLabel,
                describitionVBox
        );

        currentPetHBox.setId("currentPetHBox");

        HBox appsHBox = new HBox(10);
        appsHBox.getChildren().addAll(
                networkButton,
                chatButton,
                timerButton,
                petButton,
                settingsButton
        );

        appsHBox.setId("appsHBox");


        // Build UI layout
        VBox rootVBox = new VBox(10);
        rootVBox.getChildren().addAll(
                homeTitle,
                currentPetHBox,
                appsHBox
        );

        rootVBox.setId("windowRootVBox");

        // Set up scene
        Group root = new Group(rootVBox);
        scene = new Scene(root, 300, 193);
        petController.setWindowScene(scene);

        scene.setFill(Color.web("#B8CCCB"));

        // Apply CSS styling
        String css = petController.getCss();
        if (css != null) {
            scene.getStylesheets().add(css);
        }

        // Configure stage
        stage.setTitle("Apps");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

        // Set up button event handlers
        setupEventHandlers();
        updatePetGraphic(currentPetName);
    }

    private void setupEventHandlers() {
        chatButton.setOnAction(e -> chat.changeScene());
        networkButton.setOnAction(e -> networkConnector.changeScene());
        timerButton.setOnAction(e -> timer.changeScene());
        petButton.setOnAction(e -> petSelect.changeScene());
        settingsButton.setOnAction(e -> petSelect.settings.changeScene());
    }

    @Override
    public void setAlwaysOnTop(boolean onTop) {
        stage.setAlwaysOnTop(onTop);
        windowOnTop = onTop;
    }




    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    // Getters for components
    public PetSelect getPetSelect() {
        return petSelect;
    }
}


