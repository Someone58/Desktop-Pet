package com.example.desktoppet;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Window {
    Stage stage = new Stage();

    SharedTextAreaManager chatManager = new SharedTextAreaManager();
    SharedTextAreaManager connectionManager = new SharedTextAreaManager();
    SharedTextAreaManager statusManager = new SharedTextAreaManager();
    NetworkManager networkManager = new NetworkManager(new Logic(), this, chatManager, connectionManager, statusManager);
//    SharedTextAreaManager textAreaManagerServer = new SharedTextAreaManager();

    private TextArea connectionStatus = new TextArea();
    Button chatButton = new Button("Chat");
    Chat chat = new Chat(networkManager, chatManager);

    Button networkButton = new Button("Network");
    NetworkConnector networkConnector = new NetworkConnector(networkManager, connectionStatus, connectionManager);


public Window() {
    connectionStatus.setEditable(false);
    connectionStatus.setPrefRowCount(1);
    connectionStatus.setPrefHeight(25);
    connectionStatus.setStyle("-fx-text-fill: darkred;");
    statusManager.registerTextArea(connectionStatus);
    connectionStatus.setText("Disconnected");

}



    public void openWindow() {

        VBox rootVBox = new VBox(5);
        rootVBox.getChildren().addAll(
                connectionStatus,
                chatButton,
                networkButton
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
    }

}