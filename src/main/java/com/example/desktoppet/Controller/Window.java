package com.example.desktoppet.Controller;

import com.example.desktoppet.Model.PetData;
import com.example.desktoppet.UI.WindowGUI;
import com.example.desktoppet.Interfaces.WindowUI;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main application window that separates business logic from UI components.
 * Uses WindowGUI for UI rendering and focuses on application logic.
 */
public class Window extends Application {
    private PetData petController = new PetData();
    private WindowUI windowUI;
    
    public Window() {
        // Initialize UI implementation
        windowUI = new WindowGUI(petController);
        windowUI.initializeUI();
    }

    @Override
    public void start(Stage primaryStage) {
        // Start the pet animation
        PetSelect petSelect = ((WindowGUI)windowUI).getPetSelect();
        petSelect.startPet();

        // Set up event handler for pet button
        petSelect.pet.pet.setOnAction(e -> {
            windowUI.openWindow();
            windowUI.getStage().setIconified(false);
        });
    }
    
    /**
     * Access to the UI implementation
     * @return the window UI interface
     */
    public WindowUI getWindowUI() {
        return windowUI;
    }
}