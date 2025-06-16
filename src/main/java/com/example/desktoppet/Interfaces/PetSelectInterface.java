package com.example.desktoppet.Interfaces;

/**
 * Interface for PetSelect UI operations.
 * This allows for different UI implementations while maintaining consistent functionality.
 */
public interface PetSelectInterface {
    /**
     * Display the pet selection window
     */
    void changeScene();

    /**
     * Initialize UI components with current settings
     */
    void initializeUI();

    /**
     * Apply selected pet
     * @param petName the name of the selected pet
     */
    void applyPetSelection(String petName);
}
