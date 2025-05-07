package com.example.desktoppet;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class NetworkConnector {

    private TextField portField = new TextField("5555");
    private TextField hostField = new TextField("localhost");
    private Button hostButton = new Button("Host Game");
    private Button joinButton = new Button("Join Game");

    private TextArea connectionStatus;
    private NetworkManager networkManager;

    private SharedTextAreaManager textAreaManager;
    private TextArea localTextArea;


    public NetworkConnector(NetworkManager networkManager, TextArea connectionStatus, SharedTextAreaManager textAreaManager) {
        this.networkManager = networkManager;
        this.connectionStatus = connectionStatus;
        this.textAreaManager = textAreaManager;
        this.localTextArea = new TextArea();
        this.textAreaManager.registerTextArea(localTextArea);
    }

    public void changeScene(Stage stage, Scene windowScene) {

        Button backButton = new Button("Back");

        localTextArea.setEditable(false);
        localTextArea.setWrapText(true);
        localTextArea.setPrefRowCount(10);
        localTextArea.setPrefColumnCount(30);

        HBox connectionBox = new HBox(5);
        connectionBox.getChildren().addAll(new Label("Host:"), hostField, new Label("Port:"), portField);

        VBox rootVBox = new VBox(5);
        rootVBox.getChildren().addAll(
                backButton,
                connectionBox,
                hostButton,
                joinButton,
                localTextArea
        );

        Group root = new Group(rootVBox);
        Scene scene = new Scene(root, 300, 400);

        String css = this.getClass().getResource("/application.css").toExternalForm();
        if (css != null) {
            scene.getStylesheets().add(css);
        }

        stage.setTitle("Network Connector");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

        hostButton.setOnAction(e -> {
            try {
                networkManager.startServer(Integer.parseInt(portField.getText()));
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
            } catch (IOException ex) {
                connectionStatus.setText("Connection Failed");
                appendToChatArea("Error: " + ex.getMessage());
            }
        });

        backButton.setOnAction(e -> {
            stage.setScene(windowScene);
            stage.setTitle("Apps");
        });
    }

    public void appendToChatArea(String message) {
        localTextArea.appendText(message + "\n");
    }

}

