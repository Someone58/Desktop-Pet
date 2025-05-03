package com.example.desktoppet;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class NetworkConnector {

    private TextField portField = new TextField("5555");
    private TextField hostField = new TextField("localhost");
    private Button hostButton = new Button("Host Game");
    private Button joinButton = new Button("Join Game");

    private TextArea messageArea;
    private Label connectionStatus = new Label("Disconnected");
    private NetworkManager networkManager;
//    private Scene windowScene;

    public NetworkConnector(NetworkManager networkManager, Label connectionStatus, TextArea messageArea) {
        this.networkManager = networkManager;
        this.connectionStatus = connectionStatus;
        this.messageArea = messageArea;
    }

    public void changeScene(Stage stage, Scene windowScene) {

        Button backButton = new Button("Back");

        messageArea.setEditable(false);
        messageArea.setWrapText(true);
        messageArea.setPrefRowCount(10);
        messageArea.setPrefColumnCount(30);

        HBox connectionBox = new HBox(5);
        connectionBox.getChildren().addAll(new Label("Host:"), hostField, new Label("Port:"), portField);

        VBox rootVBox = new VBox(5);
        rootVBox.getChildren().addAll(
                backButton,
                connectionBox,
                hostButton,
                joinButton,
                messageArea
        );

        Group root = new Group(rootVBox);
        Scene scene = new Scene(root, 300, 400);

        String css = this.getClass().getResource("/application.css").toExternalForm();
        if (css != null) {
            scene.getStylesheets().add(css);
        }

        stage.setTitle("Chat");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

        hostButton.setOnAction(e -> {
            try {
                networkManager.startServer(Integer.parseInt(portField.getText()));
                updateConnectionStatus();
            } catch (IOException ex) {
                connectionStatus.setText("Hosting Failed");
                appendToChatArea("Error: " + ex.getMessage());
            }
        });

        joinButton.setOnAction(e -> {
            try {
                networkManager.connectToServer(
                        hostField.getText(),
                        Integer.parseInt(portField.getText())
                );
                updateConnectionStatus();
            } catch (IOException ex) {
                connectionStatus.setText("Connection Failed");
                appendToChatArea("Error: " + ex.getMessage());
            }
        });

        backButton.setOnAction(e -> {
            stage.setScene(windowScene);
        });

//        rootVBox.getChildren().add(backButton);
//
//        updateTurnStatus();
//        appendToChatArea("Welcome to the Network Connector!\n");
    }

    public void appendToChatArea(String message) {
        messageArea.appendText(message + "\n");
    }


    public void updateConnectionStatus() {
        Platform.runLater(() -> {
            if (networkManager.isConnected()) {
                connectionStatus.setText("Connected");
                connectionStatus.setTextFill(Color.DARKGREEN);
            } else {
                connectionStatus.setText("Disconnected");
                connectionStatus.setTextFill(Color.DARKRED);
            }
        });
    }
}

