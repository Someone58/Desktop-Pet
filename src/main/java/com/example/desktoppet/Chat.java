package com.example.desktoppet;

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

public class Chat {
    Stage stage = new Stage();
    private TextArea chatArea;
    private TextArea messageArea;
//    private Logic logic = Window.logic;
//    NetworkManager networkManager = new NetworkManager(logic, this);

//    private Label connectionStatus = new Label("Disconnected");
//    private TextField portField = new TextField("5555");
//    private TextField hostField = new TextField("localhost");
//    private Button hostButton = new Button("Host Game");
//    private Button joinButton = new Button("Join Game");
    private TextField messageField = new TextField();
    private Button sendButton = new Button("Send");

    private NetworkManager networkManager;

    public Chat(NetworkManager networkManager, TextArea chatArea) {
        this.networkManager = networkManager;
        this.chatArea = chatArea;
    }

    public void changeScene(Stage stage, Scene windowScene) {

        Button backButton = new Button("Back");

        // Set up chat area
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setPrefRowCount(10);
        chatArea.setPrefColumnCount(30);

        // Set up message input area
        HBox messageBox = new HBox(5);
        messageField.setPrefWidth(150);
        messageBox.getChildren().addAll(messageField, sendButton);

        // Set up connection controls
//        HBox connectionBox = new HBox(5);
//        connectionBox.getChildren().addAll(new Label("Host:"), hostField, new Label("Port:"), portField);

        VBox rootVBox = new VBox(5);
        rootVBox.getChildren().addAll(
//                connectionBox,
//                hostButton,
//                joinButton,
                backButton,
                chatArea,
                messageBox
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


        sendButton.setOnAction(e -> sendMessage());
        messageField.setOnAction(e -> sendMessage());

        backButton.setOnAction(e -> {
            stage.setScene(windowScene);
        });
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty() && networkManager.isConnected()) {
            networkManager.sendMessage(message);
            messageField.clear();
        }
    }

//    public void appendToChatArea(String message) {
//        chatArea.appendText(message + "\n");
//    }
//
//    public void updateTurnStatus() {
//        if (networkManager.isConnected()) {
//            connectionStatus.setText("Connected");
//            connectionStatus.setTextFill(Color.DARKGREEN);
//        } else {
//            connectionStatus.setText("Disconnected");
//            connectionStatus.setTextFill(Color.DARKRED);
//        }
//    }
}
