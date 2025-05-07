package com.example.desktoppet;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Chat {
    Stage stage = new Stage();
    private TextArea chatArea = new TextArea();
    private TextArea messageArea;

    private TextField messageField = new TextField();
    private Button sendButton = new Button("Send");

    private NetworkManager networkManager;
    private SharedTextAreaManager textAreaManager;

    double position = chatArea.getCaretPosition();
    double newPosition =  0;
    double scrollBottom = 0;
    boolean scrolledUp = false;
    double lastPosition = 0;

    Button testButton = new Button("Test");

    public Chat(NetworkManager networkManager, SharedTextAreaManager textAreaManager
    ) {
        this.networkManager = networkManager;
        this.textAreaManager = textAreaManager;

        textAreaManager.registerTextArea(chatArea);

    }

    public void changeScene(Stage stage, Scene windowScene) {

        Button backButton = new Button("Back");

        // Set up chat area
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setPrefRowCount(10);
        chatArea.setPrefColumnCount(30);
        chatArea.setPrefWidth(300);

        // Set up message input area
        HBox messageBox = new HBox(5);
        messageField.setPrefWidth(150);
        messageBox.getChildren().addAll(messageField, sendButton);

        VBox rootVBox = new VBox(5);
        rootVBox.getChildren().addAll(
                backButton,
                chatArea,
                messageBox,
                testButton
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
            stage.setTitle("Apps");
        });

        chatArea.scrollTopProperty().addListener((obs, oldVal, newVal) -> {

            if ((double) newVal % 1 == 0 && (double) newVal != 0 && (double) newVal > scrollBottom) {
                scrollBottom = (double) newVal;
                System.out.println("At bottom:  " + scrollBottom);
            }

            newPosition = (double) newVal;

            if (position > newPosition){
                scrolledUp = true;
            }
            else if (Math.abs(newPosition - scrollBottom) <= 50){
                scrolledUp = false;
            }
            position = newPosition;

            if (newPosition != 0){
                lastPosition = newPosition;
            }
        });


        chatArea.textProperty().addListener((obs, oldText, newText) -> {

            if (scrolledUp == false) {
                Platform.runLater(() -> {
                    System.out.println(chatArea.getCaretPosition());
                    chatArea.positionCaret(newText.length());
                });
            }
            else{
                chatArea.setScrollTop(lastPosition);
//                System.out.println("last Position: " + lastPosition);
                testButton.fire();
            }
        });

        testButton.setOnAction(e -> {
            PauseTransition delay = new PauseTransition(Duration.seconds(0.005));
            delay.setOnFinished(ev -> {
                Platform.runLater(() -> chatArea.setScrollTop(lastPosition));
            });
            delay.play();
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
