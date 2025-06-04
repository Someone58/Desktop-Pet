package com.example.desktoppet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;

/**
 * A singleton class that manages the application's stylesheets
 * Allows centralized style changes that affect all registered scenes at runtime
 */
public class StyleManager {
    private static StyleManager instance;

    // Default stylesheet path
    private String currentStylesheet = "/application.css";

    // List of all scenes that are managed by this StyleManager
    private final ObservableList<Scene> managedScenes = FXCollections.observableArrayList();

    private StyleManager() {
        // Private constructor to enforce singleton pattern
    }

    /**
     * Get the singleton instance of the StyleManager
     * @return The StyleManager instance
     */
    public static synchronized StyleManager getInstance() {
        if (instance == null) {
            instance = new StyleManager();
        }
        return instance;
    }

    /**
     * Register a scene to be managed by the StyleManager
     * @param scene The scene to register
     */
    public void registerScene(Scene scene) {
        if (!managedScenes.contains(scene)) {
            // Clear any existing stylesheets
            scene.getStylesheets().clear();

            // Add the current stylesheet
            String cssUrl = this.getClass().getResource(currentStylesheet).toExternalForm();
            scene.getStylesheets().add(cssUrl);

            // Add to managed scenes list
            managedScenes.add(scene);
        }
    }

    /**
     * Unregister a scene from the StyleManager
     * @param scene The scene to unregister
     */
    public void unregisterScene(Scene scene) {
        managedScenes.remove(scene);
    }

    /**
     * Change the stylesheet for all managed scenes
     * @param stylesheetPath The path to the new stylesheet
     */
    public void setStylesheet(String stylesheetPath) {
        this.currentStylesheet = stylesheetPath;
        updateAllScenes();
    }

    /**
     * Get the current stylesheet path
     * @return The current stylesheet path
     */
    public String getCurrentStylesheet() {
        return currentStylesheet;
    }

    /**
     * Update all managed scenes with the current stylesheet
     */
    private void updateAllScenes() {
        for (Scene scene : managedScenes) {
            // Clear existing stylesheets
            scene.getStylesheets().clear();

            // Add the current stylesheet
            String cssUrl = this.getClass().getResource(currentStylesheet).toExternalForm();
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl);
            }
        }
    }
}
