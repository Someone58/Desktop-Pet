package com.example.desktoppet.Controller;

import com.example.desktoppet.Interfaces.NetworkConnectorInterface;
import com.example.desktoppet.Model.PetData;
import com.example.desktoppet.UI.NetworkConnectorGUI;

import java.io.IOException;

/**
 * Handles network connection logic and functionality, delegating UI operations to NetworkConnectorInterface
 */
public class NetworkConnector {
    private final PetData petController;
    private final NetworkManager networkManager;
    private final NetworkConnectorInterface networkUI;

    /**
     * Constructor that uses the default NetworkConnectorGUI implementation
     * @param petController the pet controller instance
     */
    public NetworkConnector(PetData petController) {
        this.petController = petController;
        this.networkManager = petController.getNetworkManager();

        // Create and initialize the default UI implementation
        this.networkUI = new NetworkConnectorGUI(this, petController);
    }

    /**
     * Constructor that accepts a custom UI implementation
     * @param petController the pet controller instance
     * @param networkUI custom UI implementation
     */
    public NetworkConnector(PetData petController, NetworkConnectorInterface networkUI) {
        this.petController = petController;
        this.networkManager = petController.getNetworkManager();
        this.networkUI = networkUI;
    }

    /**
     * Changes to the network connector scene using the UI implementation
     */
    public void changeScene() {
        networkUI.changeScene();
    }

    /**
     * Attempts to start a server
     * @param port the port to host on
     * @return true if server started, false otherwise
     */
    public boolean startServer(int port) {
        try {
            networkManager.startServer(port);
            return true;
        } catch (IOException ex) {
            petController.getConnectionStatus().setText("Hosting Failed");
            appendToChatArea("Error: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Attempts to connect to a server
     * @param host the host to connect to
     * @param port the port to connect to
     * @return true if connection succeeded, false otherwise
     */
    public boolean connectToServer(String host, int port) {
        try {
            networkManager.connectToServer(host, port);
            return true;
        } catch (IOException ex) {
            petController.getConnectionStatus().setText("Connection Failed");
            appendToChatArea("Error: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Appends a message to the chat area
     * @param message the message to append
     */
    public void appendToChatArea(String message) {
        networkUI.appendToChatArea(message);
    }

    /**
     * Get the UI implementation
     * @return the network connector UI interface
     */
    public NetworkConnectorInterface getNetworkUI() {
        return networkUI;
    }

    /**
     * Get the pet controller
     * @return the pet controller
     */
    public PetData getPetController() {
        return petController;
    }
}

