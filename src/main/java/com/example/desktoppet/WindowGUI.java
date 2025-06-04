package com.example.desktoppet;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Handles UI construction and components for the main window
 */
public class WindowGUI implements WindowUI {
    private PetController petController;
    private Stage stage;
    private Scene scene;
    
    // UI Components
    private TextArea connectionStatus;
    private Button chatButton;
    private Button networkButton;
    private Button timerButton;
    private Button petButton;
    private Button settingsButton;
    private Button pinWindow;
    
    // Component handlers
    private Chat chat;
    private NetworkConnector networkConnector;
    private Timer timer;
    private PetSelect petSelect;
    private boolean windowOnTop = true;
    
    public WindowGUI(PetController petController) {
        this.petController = petController;
        this.stage = petController.getStage();
        
        // Initialize UI components
        connectionStatus = new TextArea();
        chatButton = new Button("Chat");
        networkButton = new Button("Network");
        timerButton = new Button("Timer");
        petButton = new Button("Select Pet");
        settingsButton = new Button("Settings");
        pinWindow = new Button("Pin Window");
        
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
        // Build UI layout
        VBox rootVBox = new VBox(5);
        rootVBox.getChildren().addAll(
                connectionStatus,
                chatButton,
                networkButton,
                timerButton,
                petButton,
                settingsButton,
                pinWindow
        );
        
        // Set up scene
        Group root = new Group(rootVBox);
        scene = new Scene(root, 300, 400);
        petController.setWindowScene(scene);
        
        // Apply CSS styling
        String css = this.getClass().getResource("/application.css").toExternalForm();
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
        pinWindow.setOnAction(e -> toggleAlwaysOnTop());
    }
    
    private void toggleAlwaysOnTop() {
        setAlwaysOnTop(!windowOnTop);
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
