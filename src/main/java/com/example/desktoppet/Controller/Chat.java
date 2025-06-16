package com.example.desktoppet.Controller;

import com.example.desktoppet.Interfaces.ChatInterface;
import com.example.desktoppet.Model.PetData;
import com.example.desktoppet.UI.ChatGUI;

/**
 * Handles chat logic and functionality, delegating UI operations to ChatInterface
 */
public class Chat {
    private final PetData petController;
    private final NetworkManager networkManager;
    private final ChatInterface chatUI;
    
    /**
     * Constructor that uses the default ChatGUI implementation
     * @param petController the pet controller instance
     */
    public Chat(PetData petController) {
        this.petController = petController;
        this.networkManager = petController.getNetworkManager();
        
        // Create and initialize the default UI implementation
        this.chatUI = new ChatGUI(this, petController);
    }
    
    /**
     * Constructor that accepts a custom UI implementation
     * @param petController the pet controller instance
     * @param chatUI custom UI implementation
     */
    public Chat(PetData petController, ChatInterface chatUI) {
        this.petController = petController;
        this.networkManager = petController.getNetworkManager();
        this.chatUI = chatUI;
    }
    
    /**
     * Changes to the chat scene using the UI implementation
     */
    public void changeScene() {
        chatUI.changeScene();
    }
    
    /**
     * Attempts to send a chat message
     * @param message the message to send
     * @return true if message was sent, false otherwise
     */
    public boolean sendChatMessage(String message) {
        if (!message.isEmpty() && networkManager.isConnected()) {
            networkManager.sendMessage(message);
            return true;
        }
        return false;
    }
    
    /**
     * Get the UI implementation
     * @return the chat UI interface
     */
    public ChatInterface getChatUI() {
        return chatUI;
    }
    
    /**
     * Get the pet controller
     * @return the pet controller
     */
    public PetData getPetController() {
        return petController;
    }
}

