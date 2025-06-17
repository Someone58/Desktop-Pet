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
    
    public WindowGUI(PetData petController) {
        this.petController = petController;
        this.stage = petController.getStage();
        
        // Initialize UI components
        connectionStatus = new TextArea();

        petButton = new Button("");
        Image petButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pet.png")));
        ImageView petButtonImgView = new ImageView(petButtonImg);
        petButtonImgView.setFitWidth(30);
        petButtonImgView.setFitHeight(30);
        petButtonImgView.setPreserveRatio(true);
        petButton.setGraphic(petButtonImgView);
        petButton.setId("petButton");

        networkButton = new Button("");
        Image networkButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/network.png")));
        ImageView networkButtonImgView = new ImageView(networkButtonImg);
        networkButtonImgView.setFitWidth(30);
        networkButtonImgView.setFitHeight(30);
        networkButtonImgView.setPreserveRatio(true);
        networkButton.setGraphic(networkButtonImgView);
        networkButton.setId("networkButton");

        chatButton = new Button("");
        Image chatButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/chat.png")));
        ImageView chatButtonImgView = new ImageView(chatButtonImg);
        chatButtonImgView.setFitWidth(30);
        chatButtonImgView.setFitHeight(30);
        chatButtonImgView.setPreserveRatio(true);
        chatButton.setGraphic(chatButtonImgView);
        chatButton.setId("chatButton");

        timerButton = new Button("");
        Image timerButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/timer.png")));
        ImageView timerButtonImgView = new ImageView(timerButtonImg);
        timerButtonImgView.setFitWidth(30);
        timerButtonImgView.setFitHeight(30);
        timerButtonImgView.setPreserveRatio(true);
        timerButton.setGraphic(timerButtonImgView);
        timerButton.setId("timerButton");

        settingsButton = new Button("");
        Image settingsButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/settings.png")));
        ImageView settingsButtonImgView = new ImageView(settingsButtonImg);
        settingsButtonImgView.setFitWidth(30);
        settingsButtonImgView.setFitHeight(30);
        settingsButtonImgView.setPreserveRatio(true);
        settingsButton.setGraphic(settingsButtonImgView);
        settingsButton.setId("settingsButton");
        
        // Initialize handlers with their respective UI implementations
        chat = new Chat(petController); // This now uses ChatGUI internally
        networkConnector = new NetworkConnector(petController);
        timer = new Timer(petController);
        petSelect = new PetSelect(petController);
    }
    
    @Override
    public void initializeUI() {
        stage.setAlwaysOnTop(true);
        
        // Configure connection status area
        SharedTextAreaManager statusManager = petController.getStatusManager();
        connectionStatus.setEditable(false);
        connectionStatus.setPrefRowCount(1);
        connectionStatus.setPrefHeight(25);
        connectionStatus.getStyleClass().add("disconnected-status");
        statusManager.registerTextArea(connectionStatus);
        connectionStatus.setText("Disconnected");
        
        // Setup connection status styling
        connectionStatus.textProperty().addListener((observable, oldValue, newValue) -> {
            connectionStatus.getStyleClass().removeAll("connected-status", "disconnected-status");
            if (newValue.equals("Connected")) {
                connectionStatus.getStyleClass().add("connected-status");
            } else if (newValue.equals("Disconnected")) {
                connectionStatus.getStyleClass().add("disconnected-status");
            }
        });
    }


    @Override
    public void openWindow() {

        Label homeTitle = new Label("Home");
        homeTitle.setId("homeTitle");

        Label currentPet = new Label("");
        Image sharkImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/shark.png")));
        ImageView sharkImgView = new ImageView(sharkImg);
        sharkImgView.setFitWidth(80);
        sharkImgView.setFitHeight(80);
        sharkImgView.setPreserveRatio(true); // Damit das Bild nicht verzerrt wird
        currentPet.setGraphic(sharkImgView);
        currentPet.setId("currentPet");


        Label petName = new Label("Mister Shark");
        petName.setId("petName");

        Label currentStatus = new Label("");
        Image statusImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/disconnected.png")));
        ImageView statusImgView = new ImageView(statusImg);
        statusImgView.setFitWidth(15);
        statusImgView.setFitHeight(15);
        statusImgView.setPreserveRatio(true);
        currentStatus.setGraphic(statusImgView);
        currentStatus.setId("currentStatus");

        HBox connectionHBox = new HBox(5);
        connectionHBox.getChildren().addAll(
                currentStatus,
                connectionStatus
        );

        VBox describitionVBox = new VBox(5);
        describitionVBox.getChildren().addAll(
                petName,
                currentStatus
        );

        HBox currentPetHBox = new HBox(5);
        currentPetHBox.getChildren().addAll(
                currentPet,
                describitionVBox
        );

        HBox appsHBox = new HBox(5);
        appsHBox.getChildren().addAll(
                petButton,
                networkButton,
                chatButton,
                timerButton,
                settingsButton
        );

        appsHBox.setId("appsHBox");


        // Build UI layout
        VBox rootVBox = new VBox(5);
        rootVBox.getChildren().addAll(
                homeTitle,
                currentPetHBox,
                appsHBox
        );

        rootVBox.setId("rootVBox");
        
        // Set up scene
        Group root = new Group(rootVBox);
        scene = new Scene(root, 300, 200);
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
