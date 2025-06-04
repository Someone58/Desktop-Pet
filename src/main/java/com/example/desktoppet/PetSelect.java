package com.example.desktoppet;

import javafx.scene.image.Image;

/**
 * Handles pet selection logic and functionality, delegating UI operations to PetSelectInterface
 */
public class PetSelect {
    private final PetController petController;
    private final Notification notification;
    final Settings settings;
    private double height = 100;
    private double size = 100;

    private final Image sharkIdle;
    private final String sharkName = "Shark";

    private final Image hedgehogIdle;
    private final String hedgehogName = "Hedgehog";

    private final String dogName = "Dog";

    PetAnimation pet;
    private final PetSelectInterface petSelectUI;

    /**
     * Constructor that uses the default PetSelectGUI implementation
     * @param petController the pet controller instance
     */
    public PetSelect(PetController petController) {
        this.petController = petController;

        notification = petController.getNotification();
        pet = new PetAnimation(petController);
        sharkIdle = petController.getSharkIdle();
        hedgehogIdle = petController.getHedgehogIdle();
        settings = petController.getSettings();

        // Store this instance in the stage's userData for access from Settings
        petController.getStage().setUserData(this);

        // Initialize with current settings values
        this.size = settings.getSize();
        this.height = settings.getHeight();

        // Apply initial settings to pet
        pet.setxSpeed(settings.getSpeed());
        pet.setActivity(settings.getActivity());
        pet.setSizeHeight(size, height);

        // Create and initialize the default UI implementation
        this.petSelectUI = new PetSelectGUI(this, petController);
    }

    /**
     * Constructor that accepts a custom UI implementation
     * @param petController the pet controller instance
     * @param petSelectUI custom UI implementation
     */
    public PetSelect(PetController petController, PetSelectInterface petSelectUI) {
        this.petController = petController;

        notification = petController.getNotification();
        pet = new PetAnimation(petController);
        sharkIdle = petController.getSharkIdle();
        hedgehogIdle = petController.getHedgehogIdle();
        settings = petController.getSettings();

        // Store this instance in the stage's userData for access from Settings
        petController.getStage().setUserData(this);

        // Initialize with current settings values
        this.size = settings.getSize();
        this.height = settings.getHeight();

        // Apply initial settings to pet
        pet.setxSpeed(settings.getSpeed());
        pet.setActivity(settings.getActivity());
        pet.setSizeHeight(size, height);

        this.petSelectUI = petSelectUI;
    }

    /**
     * Changes to the pet selection scene using the UI implementation
     */
    public void changeScene() {
        petSelectUI.changeScene();
    }

    /**
     * Sets up the selected pet
     * @param petName the name of the pet to set up
     */
    public void setupPet(String petName) {
        pet.setupPet(petName);
    }

    /**
     * Starts the pet animation
     */
    public void startPet() {
        pet = new PetAnimation(petController);
        pet.setupStage();
    }

    /**
     * Get the pet animation instance
     * @return the current PetAnimation instance
     */
    public PetAnimation getPetAnimation() {
        return pet;
    }

    /**
     * Get the UI implementation
     * @return the pet select UI interface
     */
    public PetSelectInterface getPetSelectUI() {
        return petSelectUI;
    }

    /**
     * Get the pet controller
     * @return the pet controller
     */
    public PetController getPetController() {
        return petController;
    }

    /**
     * Get the height setting
     * @return the height value
     */
    public double getHeight() {
        return height;
    }

    /**
     * Get the size setting
     * @return the size value
     */
    public double getSize() {
        return size;
    }

    /**
     * Get the shark name
     * @return the shark name
     */
    public String getSharkName() {
        return sharkName;
    }

    /**
     * Get the hedgehog name
     * @return the hedgehog name
     */
    public String getHedgehogName() {
        return hedgehogName;
    }

    /**
     * Get the dog name
     * @return the dog name
     */
    public String getDogName() {
        return dogName;
    }
}
