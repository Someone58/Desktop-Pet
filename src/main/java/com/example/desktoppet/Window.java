package com.example.desktoppet;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Window extends Application {
    private PetController petController = new PetController();

    Stage stage;

    private TextArea connectionStatus = new TextArea();
    SharedTextAreaManager statusManager = petController.getStatusManager();

    Button chatButton = new Button("Chat");
    Chat chat = new Chat(petController);

    Button networkButton = new Button("Network");
    NetworkConnector networkConnector = new NetworkConnector(petController);

    Button timerButton = new Button("Timer");
    Timer timer = new Timer(petController);

    Button petButton = new Button("Select Pet");
    PetSelect petSelect = new PetSelect(petController);

    Button settingsButton = new Button("Settings");

    Button pinWindow = new Button("Pin Window");
    boolean windowOnTop = true;


    public Window() {
        stage = petController.getStage();

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
//                test.getStyleClass().add("connected-status");
            } else if (newValue.equals("Disconnected")) {
                connectionStatus.getStyleClass().add("disconnected-status");
            }
        });

    }

    @Override
    public void start(Stage primaryStage){
        petSelect.startPet();
        System.out.println("program started");

        petSelect.pet.pet.setOnAction(e -> {
            openWindow();
            stage.setIconified(false);
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
        petController.setWindowScene(scene);

        String css = this.getClass().getResource("/application.css").toExternalForm();
        if (css != null) {
            scene.getStylesheets().add(css);
        }

        stage.setTitle("Apps");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

        chatButton.setOnAction(e -> {
            chat.changeScene();
        });

        networkButton.setOnAction(e -> {
            networkConnector.changeScene();
        });

        timerButton.setOnAction(e -> {
            timer.changeScene();
        });

        petButton.setOnAction(e -> {
            petSelect.changeScene();
        });

        settingsButton.setOnAction(e -> {
            petSelect.settings.changeScene();
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