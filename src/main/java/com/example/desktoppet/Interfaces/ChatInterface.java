package com.example.desktoppet.Interfaces;

/**
 * Interface for Chat UI operations.
 * This allows for different UI implementations while maintaining consistent functionality.
 */
public interface ChatInterface {
    /**
     * Display the chat window
     */
    void changeScene();
    
    /**
     * Send a message through the UI
     */
    void sendMessage();
    
    /**
     * Register a TextArea with the shared text area manager
     */
    void registerTextArea();
    
    /**
     * Set the scroll position behavior based on user interaction
     * @param scrolledUp whether the user has manually scrolled up
     */
    void setScrolledUp(boolean scrolledUp);
    
    /**
     * Get whether the user has manually scrolled up
     * @return true if the user has scrolled up, false otherwise
     */
    boolean isScrolledUp();
}
