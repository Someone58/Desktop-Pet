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

public class Window {
    Stage stage = new Stage();

    private TextArea chatArea = new TextArea();
    private TextArea messageArea = new TextArea();

    private Logic logic = new Logic();
    NetworkManager networkManager = new NetworkManager(logic, this, messageArea, chatArea);

    private Label connectionStatus = new Label("Disconnected");
    Button chatButton = new Button("Chat");
    Chat chat = new Chat(networkManager, chatArea);

    Button networkButton = new Button("Network");
    NetworkConnector networkConnector = new NetworkConnector(networkManager, connectionStatus, messageArea);



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


//    public void updateConnectionStatus() {
//        Platform.runLater(() -> {
//            if (connectionStatus != null) {
//                if (networkManager.isConnected()) {
//                    connectionStatus.setText("Connected");
//                    connectionStatus.setTextFill(Color.DARKGREEN);
//                } else {
//                    connectionStatus.setText("Disconnected");
//                    connectionStatus.setTextFill(Color.DARKRED);
//                }
//            }
//        });
//    }


}