package com.example.desktoppet.UI;

import com.example.desktoppet.*;
import com.example.desktoppet.Controller.CopieableLabel;
import com.example.desktoppet.Controller.NetworkConnector;
import com.example.desktoppet.Controller.NetworkManager;
import com.example.desktoppet.Interfaces.NetworkConnectorInterface;
import com.example.desktoppet.Model.PetData;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * Handles UI construction and components for the network connector window
 */
public class NetworkConnectorGUI implements NetworkConnectorInterface {
    private final NetworkConnector networkConnector;
    private final PetData petData;

    private CopieableLabel copieableLabel = new CopieableLabel("IP address: ");
    private Label titlePortField = new Label("Port: ");
    private Label titleHostField = new Label("Host: ");
    private TextField portField = new TextField("5555");
    private TextField hostField = new TextField("localhost");
    private Button hostButton = new Button("Host");
    private Button joinButton = new Button("Join");
    private Button copyButton = new Button();

    private TextArea connectionStatus;
    private NetworkManager networkManager;

    private SharedTextAreaManager textAreaManager;
    private TextArea localTextArea = new TextArea();

    private Label ipAddress = new Label("IP-addr: ");

    public NetworkConnectorGUI(NetworkConnector networkConnector, PetData petController) {
        this.networkConnector = networkConnector;
        this.petData = petController;

        // Initialize components
        this.networkManager = petController.getNetworkManager();
        this.connectionStatus = petController.getConnectionStatus();
        this.textAreaManager = petController.getConnectionManager();
        this.textAreaManager.registerTextArea(localTextArea);
    }

    @Override
    public void changeScene() {
        updateIPAddress();


        Image copyButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/copy.png")));
        ImageView copyButtonImgView = new ImageView(copyButtonImg);
        copyButtonImgView.setFitHeight(20);
        copyButtonImgView.setFitWidth(20);
        copyButtonImgView.setPreserveRatio(true);
        copyButton.setGraphic(copyButtonImgView);
        copyButton.setOnAction(e -> CopieableLabel.copyToClipboard(copieableLabel.getText()));

        Stage stage = petData.getStage();
        Scene windowScene = petData.getWindowScene();

        Label networkTitle = new Label(" Network");
        networkTitle.setId("networkTitle");

        Button backButton = new Button("");
        Image backButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/arrow.png")));
        ImageView backButtonImgView = new ImageView(backButtonImg);
        backButtonImgView.setFitWidth(15);
        backButtonImgView.setFitHeight(15);
        backButtonImgView.setPreserveRatio(true);
        backButton.setGraphic(backButtonImgView);
        backButton.setId("backButton");


        localTextArea.setEditable(false);
        localTextArea.setWrapText(true);
        localTextArea.setPrefRowCount(5);
        localTextArea.setPrefColumnCount(20);
        localTextArea.setMinHeight(100);
        localTextArea.setMaxWidth(250);

        portField.setMinWidth(93);
        hostField.setMinWidth(183);

        copieableLabel.setId("copieableLabel");
        copyButton.setId("copyButton");

        hostButton.setId("hostButton");
        joinButton.setId("joinButton");

        ipAddress.setId("ipAddress");

        HBox hostHBox = new HBox(7);
        hostHBox.getChildren().addAll(titleHostField, hostField);
        

        HBox portHBox = new HBox(7);
        portHBox.getChildren().addAll(titlePortField, portField);

        VBox textFieldVBox = new VBox(9);
        textFieldVBox.getChildren().addAll(hostHBox, portHBox);

        HBox connectionButtonHBox = new HBox(23);
        connectionButtonHBox.getChildren().addAll(hostButton, joinButton);

        VBox connectionViewVBox = new VBox(8);
        connectionViewVBox.getChildren().addAll(connectionButtonHBox, localTextArea);

        VBox networkComponentsVBox = new VBox(20);
        networkComponentsVBox.getChildren().addAll(textFieldVBox, connectionViewVBox);


        HBox ipHBox = new HBox(0);
        ipHBox.getChildren().addAll(
                ipAddress,
                copieableLabel,
                copyButton
        );

        VBox allNetworkComponentsVBox = new VBox(20);
        allNetworkComponentsVBox.getChildren().addAll(networkComponentsVBox, ipHBox);

        allNetworkComponentsVBox.setId("allNetworkComponentsVBox");

        VBox rootVBox = new VBox(5);
        rootVBox.getChildren().addAll(
                networkTitle,
                backButton,
                allNetworkComponentsVBox
        );

        rootVBox.setId("rootVBox");

        Group root = new Group(rootVBox);
        Scene scene = new Scene(root, 300, 400);

        scene.setFill(Color.web("#B8CCCB"));

        String css = petData.getCss();
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

    @Override
    public void appendToChatArea(String message) {
        localTextArea.appendText(message + "\n");
    }

    @Override
    public String getHostField() {
        return hostField.getText();
    }

    @Override
    public String getPortField() {
        return portField.getText();
    }

    @Override
    public void updateIPAddress() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            copieableLabel.setText(address.getHostAddress());
        } catch (UnknownHostException ex) {
            copieableLabel.setText("Could not find IP address for this host");
        }
    }

    // Getters for components
    public TextArea getLocalTextArea() {
        return localTextArea;
    }
}
