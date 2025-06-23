package com.example.desktoppet.UI;

import com.example.desktoppet.*;
import com.example.desktoppet.Controller.Chat;
import com.example.desktoppet.Controller.NetworkConnector;
import com.example.desktoppet.Controller.PetSelect;
import com.example.desktoppet.Controller.Timer;
import com.example.desktoppet.Interfaces.WindowInterface;
import com.example.desktoppet.Model.PetData;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.util.Objects;

/**
 * Handles UI construction and components for the main window
 */
public class WindowGUI extends BaseGUI implements WindowInterface {
    private Scene scene;

    // UI Components
    private TextArea connectionStatus;
    private Button chatButton;
    private Button networkButton;
    private Button timerButton;
    private Button petButton;
    private Button settingsButton;

    // Component handlers
    private Chat chat;
    private NetworkConnector networkConnector;
    private Timer timer;
    private PetSelect petSelect;
    private boolean windowOnTop = true;

    private Label currentStatus = new Label("");
    private Image disconnectedImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/disconnected.png")));
    private ImageView disconnectedImgView = new ImageView(disconnectedImg);
    private Image connectedImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/connected.png")));
    private ImageView connectedImgView = new ImageView(connectedImg);
    private Label currentPetLabel;
    private String currentPetName;
    private Image sharkImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/shark.png")));
    private ImageView sharkImgView = new ImageView(sharkImg);
    private Image hedgehogImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/hedgehog.png")));
    private ImageView hedgehogImgView = new ImageView(hedgehogImg);
    private Image dogImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/dog.png")));
    private ImageView dogImgView = new ImageView(dogImg);


    public WindowGUI(PetData petController) {
        super(petController);

        // Initialize UI components
        connectionStatus = new TextArea();
        connectionStatus.setId("connectionStatus");

        petButton = new Button("");
        configureButton(petButton, "/pet.png", 25, 25, "petButton");

        networkButton = new Button("");
        configureButton(networkButton, "/network.png", 25, 25, "networkButton");

        chatButton = new Button("");
        configureButton(chatButton, "/chat.png", 25, 25, "chatButton");

        timerButton = new Button("");
        configureButton(timerButton, "/timer.png", 25, 25, "timerButton");

        settingsButton = new Button("");
        configureButton(settingsButton, "/settings.png", 25, 25, "settingsButton");

        // Initialize handlers with their respective UI implementations
        chat = new Chat(petController); // This now uses ChatGUI internally
        networkConnector = new NetworkConnector(petController);
        timer = new Timer(petController);
        petSelect = new PetSelect(petController);
        ((PetSelectGUI)petSelect.getPetSelectUI()).setWindowGUI(this);
    }

    @Override
    public void initializeUI() {
        stage.setAlwaysOnTop(true);

        SharedTextAreaManager statusManager = petController.getStatusManager();

        // Bilder für Status konfigurieren
        connectedImgView.setFitWidth(15);
        connectedImgView.setFitHeight(15);
        connectedImgView.setPreserveRatio(true);

        disconnectedImgView.setFitWidth(15);
        disconnectedImgView.setFitHeight(15);
        disconnectedImgView.setPreserveRatio(true);

        connectionStatus.setEditable(false);
        connectionStatus.setPrefRowCount(1);
        connectionStatus.setPrefHeight(25);
        connectionStatus.setMaxWidth(180);
        connectionStatus.setMaxHeight(15);
        connectionStatus.getStyleClass().add("disconnected-status");
        statusManager.registerTextArea(connectionStatus);
        connectionStatus.setText("Disconnected");

        // Status-Label konfigurieren
        currentStatus = new Label("Disconnected");
        currentStatus.setId("currentStatus");
        currentStatus.setGraphic(disconnectedImgView);
        currentStatus.getStyleClass().add("disconnected-status");

        // NetworkManager Status-Änderungen überwachen;
        statusManager.sharedContentProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                if ("Connected".equals(newValue)) {
                    connectionStatus.setText("Connected");
                    currentStatus.setGraphic(connectedImgView);
                    currentStatus.getStyleClass().remove("disconnected-status");
                    currentStatus.getStyleClass().add("connected-status");
                } else if ("Disconnected".equals(newValue)) {
                    connectionStatus.setText("Disconnected");
                    currentStatus.setGraphic(disconnectedImgView);
                    currentStatus.getStyleClass().remove("connected-status");
                    currentStatus.getStyleClass().add("disconnected-status");
                }
            });
        });
    }

    public void updatePetGraphic(String newPetName) {
        currentPetName = newPetName;

        // Aktualisiere das Bild
        ImageView selectedPetImage;
        String displayName;
        switch (newPetName) {
            case "Dog" -> {
                selectedPetImage = dogImgView;
                displayName = "Mister Dog";
            }
            case "Hedgehog" -> {
                selectedPetImage = hedgehogImgView;
                displayName = "Mister Hedgehog";
            }
            default -> {
                selectedPetImage = sharkImgView;
                displayName = "Mister Shark";
            }
        }

        selectedPetImage.setFitWidth(60);
        selectedPetImage.setFitHeight(60);
        selectedPetImage.setPreserveRatio(true);
        currentPetLabel.setGraphic(selectedPetImage);

        // Aktualisiere den Namen
        VBox descriptionVBox = (VBox) ((HBox) currentPetLabel.getParent()).getChildren().get(1);
        Label petNameLabel = (Label) descriptionVBox.getChildren().get(0);
        petNameLabel.setText(displayName);

    }


    @Override
    public void openWindow() {

        Label homeTitle = createTitleLabel(" Home", "homeTitle");

        Label petName = new Label("Mister Pet");
        petName.setId("petName");

        currentPetLabel = new Label();
        currentPetLabel.setId("currentPet");
        currentPetLabel.setGraphic(sharkImgView);
        currentPetName = "Shark"; // Startzustand

        HBox connectionHBox = createHBox(0, "connectionHBox");
        connectionHBox.getChildren().addAll(
                currentStatus,
                connectionStatus
        );

        VBox describitionVBox = new VBox(5);
        describitionVBox.getChildren().addAll(
                petName,
                connectionHBox
        );

        describitionVBox.setId("describitionVBox");

        HBox currentPetHBox = new HBox(5);
        currentPetHBox.getChildren().addAll(
                currentPetLabel,
                describitionVBox
        );

        currentPetHBox.setId("currentPetHBox");

        HBox appsHBox = new HBox(10);
        appsHBox.getChildren().addAll(
                networkButton,
                chatButton,
                timerButton,
                petButton,
                settingsButton
        );

        appsHBox.setId("appsHBox");

        // Build UI layout
        VBox rootVBox = new VBox(10);
        rootVBox.setId("windowRootVBox");
        rootVBox.getChildren().addAll(
                homeTitle,
                currentPetHBox,
                appsHBox
        );
        rootVBox.setTranslateX(-2); // Fix für die Position
        rootVBox.setTranslateY(2); // Fix für die Position

        // Set up scene
        Group root = new Group(rootVBox);
        scene = new Scene(root, 300, 193);
        scene.setFill(javafx.scene.paint.Color.web("#B8CCCB"));
        applyCSS(scene);
        petController.setWindowScene(scene);

        // Configure stage
        stage.setTitle("Apps");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

        // Set up button event handlers
        setupEventHandlers();
        updatePetGraphic(currentPetName);
    }

    /**
     * Erstellt ein Label für den Titel
     */
    public Label createTitleLabel(String text, String id) {
        Label label = new Label(text);
        label.setId(id);
        return label;
    }

    /**
     * Erstellt eine HBox mit Abstand und ID
     */
    public HBox createHBox(double spacing, String id) {
        HBox hbox = new HBox(spacing);
        hbox.setId(id);
        return hbox;
    }

    private void setupEventHandlers() {
        chatButton.setOnAction(e -> chat.changeScene());
        networkButton.setOnAction(e -> networkConnector.changeScene());
        timerButton.setOnAction(e -> timer.changeScene());

        // Fixe den Pet Selection Button
        petButton.setOnAction(e -> {
            try {
                if (petSelect != null && petSelect.getPetSelectUI() != null) {
                    petSelect.changeScene();
                } else {
                    System.err.println("Error: petSelect or petSelectUI is null");
                }
            } catch (Exception ex) {
                System.err.println("Error in pet selection: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        settingsButton.setOnAction(e -> petSelect.settings.changeScene());
    }

    @Override
    public void setAlwaysOnTop(boolean onTop) {
        stage.setAlwaysOnTop(onTop);
        windowOnTop = onTop;
    }




    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    // Getters for components
    public PetSelect getPetSelect() {
        return petSelect;
    }
}


