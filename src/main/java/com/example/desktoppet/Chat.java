package com.example.desktoppet;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Chat {
    Stage stage = new Stage();
    private TextArea chatArea = new TextArea();
    private TextArea messageArea;

    private TextField messageField = new TextField();
    private Button sendButton = new Button("Send");

    private NetworkManager networkManager;
    private SharedTextAreaManager textAreaManager;


    public Chat(NetworkManager networkManager, SharedTextAreaManager textAreaManager
    ) {
        this.networkManager = networkManager;
        this.textAreaManager = textAreaManager;

//        this.chatArea = chatArea;
        textAreaManager.registerTextArea(chatArea);

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

        VBox rootVBox = new VBox(5);
        rootVBox.getChildren().addAll(
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

}
