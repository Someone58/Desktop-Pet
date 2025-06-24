package com.example.desktoppet.UI;

import com.example.desktoppet.Controller.Chat;
import com.example.desktoppet.Interfaces.ChatInterface;
import com.example.desktoppet.Model.PetData;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.Objects;

/**
 * Handles UI construction and components for the chat window
 */
public class ChatGUI implements ChatInterface {
    private final Chat chatLogic;
    private final PetData petController;
    private final TextArea chatArea = new TextArea();
    private final TextField messageField = new TextField();
    private final Button sendButton = new Button("");
    
    private double position = 0;
    private double newPosition = 0;
    private double scrollBottom = 0;
    private boolean scrolledUp = false;
    private double lastPosition = 0;
    
    // Fixed line height in pixels (adjust as needed)
    private final double LINE_HEIGHT = 18;

    public ChatGUI(Chat chatLogic, PetData petController) {
        this.chatLogic = chatLogic;
        this.petController = petController;
        
        // Initialize UI components
        setupComponents();
        registerTextArea();
    }
    
    private void setupComponents() {
        // Set up chat area with fixed font size to ensure integer sizes
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setPrefRowCount(10);
        chatArea.setPrefColumnCount(30);
        chatArea.setMaxWidth(245);
        chatArea.setMinHeight(230);

        // Set fixed font size (an integer value in pixels)
        chatArea.setStyle("-fx-font-size: 14px;");
        chatArea.setId("chatArea");

        messageField.setMinWidth(180);
        messageField.setMinHeight(10);

        Image sendButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/send.png")));
        ImageView sendButtonImgView = new ImageView(sendButtonImg);
        sendButtonImgView.setFitWidth(25);
        sendButtonImgView.setFitHeight(25);
        sendButtonImgView.setPreserveRatio(true);
        sendButton.setGraphic(sendButtonImgView);
        sendButtonImgView.setId("sendButtonImgView");
        
        // Set up scroll listeners
        setupScrollListeners();
    }
    
    private void setupScrollListeners() {
        chatArea.scrollTopProperty().addListener((obs, oldVal, newVal) -> {
            // Round the raw scroll value to the nearest multiple of LINE_HEIGHT
            double rawScroll = (double) newVal;
            double adjustedScroll = Math.round(rawScroll / LINE_HEIGHT) * LINE_HEIGHT;
            System.out.println("Adjusted Scroll: " + adjustedScroll);

            if (adjustedScroll != 0 && adjustedScroll > scrollBottom) {
                scrollBottom = adjustedScroll;
                System.out.println("At bottom: " + scrollBottom);
            }

            newPosition = adjustedScroll;

            if (position > newPosition) {
                scrolledUp = true;
            } else if (Math.abs(newPosition - scrollBottom) <= 50) {
                scrolledUp = false;
            }
            position = newPosition;

            if (newPosition != 0) {
                lastPosition = newPosition;
            }
        });

        chatArea.textProperty().addListener((obs, oldText, newText) -> {
            if (!scrolledUp) {
                Platform.runLater(() -> {
                    chatArea.positionCaret(newText.length());
                });
            } else {
                chatArea.setScrollTop(lastPosition);
                scrollToTop();
            }
        });
    }
    
    @Override
    public void changeScene() {
        petController.setChatopened(true);
        petController.getNotification().setNoIcon();

        Stage stage = petController.getStage();
        Scene windowScene = petController.getWindowScene();

        Label chatTitle = new Label(" Chat");
        chatTitle.setId("chatTitle");

        Button backButton = new Button("");
        Image backButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/arrow.png")));
        ImageView backButtonImgView = new ImageView(backButtonImg);
        backButtonImgView.setFitWidth(15);
        backButtonImgView.setFitHeight(15);
        backButtonImgView.setPreserveRatio(true);
        backButton.setGraphic(backButtonImgView);
        backButton.setId("backButton");

        // Set up message input area
        HBox messageBox = new HBox(17);
        messageField.setPrefWidth(150);
        messageBox.getChildren().addAll(messageField, sendButton);
        messageBox.setId("messageBox");

        VBox chatHeader = new VBox(5);
        chatHeader.getChildren().addAll(chatTitle, backButton);

        VBox messagingBox = new VBox(18);
        messagingBox.getChildren().addAll(chatArea, messageBox);
        messagingBox.setId("messagingBox");

        VBox rootVBox = new VBox(20);
        rootVBox.setId("chatRoot");
        rootVBox.getChildren().addAll(
                chatHeader,
                messagingBox
        );

        rootVBox.setId("rootVBox");

        Group root = new Group(rootVBox);
        Scene scene = new Scene(root, 300, 400);
        scene.setFill(Color.web("#B8CCCB"));

        String css = petController.getCss();
        if (css != null) {
            scene.getStylesheets().add(css);
        }

        stage.setTitle("Chat");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

        // Set up event handlers
        sendButton.setOnAction(e -> sendMessage());
        messageField.setOnAction(e -> sendMessage());

        backButton.setOnAction(e -> {
            petController.setChatopened(false);
            stage.setScene(windowScene);
            stage.setTitle("Apps");
        });

        stage.setOnCloseRequest(event -> petController.setChatopened(false));
    }

    @Override
    public void sendMessage() {
        String message = messageField.getText().trim();
        if (chatLogic.sendChatMessage(message)) {
            messageField.clear();
        }
    }
    
    @Override
    public void registerTextArea() {
        petController.getChatManager().registerTextArea(chatArea);
    }

    private void scrollToTop() {
        // Increase delay from 0.005 to 0.05 seconds (50ms) to reduce the frequency of the scroll update
        PauseTransition delay = new PauseTransition(Duration.seconds(0.05));
        delay.setOnFinished(ev -> {
            Platform.runLater(() -> chatArea.setScrollTop(lastPosition));
        });
        delay.play();
    }


    @Override
    public void setScrolledUp(boolean scrolledUp) {
        this.scrolledUp = scrolledUp;
    }
    
    @Override
    public boolean isScrolledUp() {
        return scrolledUp;
    }
    
    // Getters for components
    public TextArea getChatArea() {
        return chatArea;
    }
    
    public TextField getMessageField() {
        return messageField;
    }
}