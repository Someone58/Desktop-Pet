package com.example.desktoppet.Interfaces;

/**
 * Interface for NetworkConnector UI operations.
 * This allows for different UI implementations while maintaining consistent functionality.
 */
public interface NetworkConnectorInterface {
    /**
     * Display the network connector window
     */
    void changeScene();

    /**
     * Append message to the chat area
     * @param message the message to append
     */
    void appendToChatArea(String message);

    /**
     * Get the host field value
     * @return the host field text
     */
    String getHostField();

    /**
     * Get the port field value
     * @return the port field text
     */
    String getPortField();

    /**
     * Update the IP address display
     */
    void updateIPAddress();
}
