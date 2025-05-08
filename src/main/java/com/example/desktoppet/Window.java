package com.example.desktoppet;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Window extends Application {
    Stage primaryStage = new Stage();
    Stage stage = new Stage();
    Rectangle2D screenBounds;

    SharedTextAreaManager chatManager = new SharedTextAreaManager();
    SharedTextAreaManager connectionManager = new SharedTextAreaManager();
    SharedTextAreaManager statusManager = new SharedTextAreaManager();
    NetworkManager networkManager = new NetworkManager(new Logic(), this, chatManager, connectionManager, statusManager);

    private TextArea connectionStatus = new TextArea();
    private Label test = new Label();

    Button chatButton = new Button("Chat");
    Chat chat = new Chat(networkManager, chatManager);

    Button networkButton = new Button("Network");
    NetworkConnector networkConnector = new NetworkConnector(networkManager, connectionStatus, connectionManager);

    Button timerButton = new Button("Timer");
    Timer timer = new Timer();

    Button pinWindow = new Button("Pin Window");
    boolean windowOnTop = true;

    Button petButton = new Button("Select Pet");
    PetSelect petSelect;

    Button settingsButton = new Button("Settings");


    public Window() {
        stage.setAlwaysOnTop(true);

        connectionStatus.setEditable(false);
        connectionStatus.setPrefRowCount(1);
        connectionStatus.setPrefHeight(25);
        connectionStatus.getStyleClass().add("disconnected-status");
        statusManager.registerTextArea(connectionStatus);
        connectionStatus.setText("Disconnected");

        connectionStatus.textProperty().addListener((observable, oldValue, newValue) -> {
            connectionStatus.getStyleClass().removeAll("connected-status", "disconnected-status");
            if (newValue.equals("Connected")) {
                connectionStatus.getStyleClass().add("connected-status");
                test.getStyleClass().add("connected-status");
            } else if (newValue.equals("Disconnected")) {
                connectionStatus.getStyleClass().add("disconnected-status");
            }
        });

    }

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        petSelect = new PetSelect(primaryStage, screenBounds);
        petSelect.startPet();
        System.out.println("program started");

        petSelect.pet.pet.setOnAction(e -> {
            openWindow();
        });
    }

    public void openWindow() {

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

        Group root = new Group(rootVBox);
        Scene scene = new Scene(root, 300, 400);

        String css = this.getClass().getResource("/application.css").toExternalForm();
        if (css != null) {
            scene.getStylesheets().add(css);
        }

        stage.setTitle("Apps");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

        chatButton.setOnAction(e -> {
            chat.changeScene(stage, scene);
        });

        networkButton.setOnAction(e -> {
            networkConnector.changeScene(stage, scene);
        });

        timerButton.setOnAction(e -> {
            timer.changeScene(stage, scene);
        });

        petButton.setOnAction(e -> {
            petSelect.changeScene(stage, scene);
        });

        settingsButton.setOnAction(e -> {
            petSelect.settings.changeScene(stage, scene);
        });

        pinWindow.setOnAction(e -> {
            if (windowOnTop == false) {
                stage.setAlwaysOnTop(true);
                windowOnTop = true;
            } else {
                stage.setAlwaysOnTop(false);
                windowOnTop = false;
            }

        });
    }

}