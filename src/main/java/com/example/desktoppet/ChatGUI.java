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

/**
 * Handles UI construction and components for the chat window
 */
public class ChatGUI implements ChatInterface {
    private final Chat chatLogic;
    private final PetController petController;
    private final TextArea chatArea = new TextArea();
    private final TextField messageField = new TextField();
    private final Button sendButton = new Button("Send");
    
    private double position = 0;
    private double newPosition = 0;
    private double scrollBottom = 0;
    private boolean scrolledUp = false;
    private double lastPosition = 0;
    
    public ChatGUI(Chat chatLogic, PetController petController) {
        this.chatLogic = chatLogic;
        this.petController = petController;
        
        // Initialize UI components
        setupComponents();
        registerTextArea();
    }
    
    private void setupComponents() {
        // Set up chat area
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setPrefRowCount(10);
        chatArea.setPrefColumnCount(30);
        chatArea.setPrefWidth(300);
        
        // Set up scroll listeners
        setupScrollListeners();
    }
    
    private void setupScrollListeners() {
        chatArea.scrollTopProperty().addListener((obs, oldVal, newVal) -> {
            if ((double) newVal % 1 == 0 && (double) newVal != 0 && (double) newVal > scrollBottom) {
                scrollBottom = (double) newVal;
                System.out.println("At bottom:  " + scrollBottom);
            }

            newPosition = (double) newVal;

            if (position > newPosition) {
                scrolledUp = true;
            }
            else if (Math.abs(newPosition - scrollBottom) <= 50) {
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
                    System.out.println(chatArea.getCaretPosition());
                    chatArea.positionCaret(newText.length());
                });
            }
            else {
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

        Button backButton = new Button("Back");

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

        // Register scene with StyleManager
        StyleManager.getInstance().registerScene(scene);

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

        stage.setOnCloseRequest(event -> {
            System.out.println("Stage is closing");
            petController.setChatopened(false);
        });
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
        PauseTransition delay = new PauseTransition(Duration.seconds(0.005));
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
