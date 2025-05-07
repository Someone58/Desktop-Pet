package com.example.desktoppet;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static com.sun.javafx.css.StyleClassSet.getStyleClass;

public class Window {
    Stage stage = new Stage();

    SharedTextAreaManager chatManager = new SharedTextAreaManager();
    SharedTextAreaManager connectionManager = new SharedTextAreaManager();
    SharedTextAreaManager statusManager = new SharedTextAreaManager();
    NetworkManager networkManager = new NetworkManager(new Logic(), this, chatManager, connectionManager, statusManager);
//    SharedTextAreaManager textAreaManagerServer = new SharedTextAreaManager();

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

    public void openWindow() {

        VBox rootVBox = new VBox(5);
        rootVBox.getChildren().addAll(
                connectionStatus,
                chatButton,
                networkButton,
                timerButton,
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

        pinWindow.setOnAction(e -> {
            if (windowOnTop == false) {
                stage.setAlwaysOnTop(true);
                windowOnTop = true;
            }
            else{
                stage.setAlwaysOnTop(false);
                windowOnTop = false;
            }

        });
    }

}