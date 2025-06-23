package com.example.desktoppet.UI;

import com.example.desktoppet.Model.PetData;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Base class for GUI components with common functionality
 */
public abstract class BaseGUI {
    protected final PetData petController;
    protected Stage stage;

    /**
     * Constructor for the base GUI class
     * @param petController the pet controller instance
     */
    public BaseGUI(PetData petController) {
        this.petController = petController;
        this.stage = petController.getStage();
    }

    /**
     * Create an ImageView for a button with the specified resource path
     * @param resourcePath the path to the image resource
     * @param width the desired width
     * @param height the desired height
     * @return configured ImageView instance
     */
    protected ImageView createButtonImageView(String resourcePath, double width, double height) {
        Image buttonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(resourcePath)));
        ImageView buttonImgView = new ImageView(buttonImg);
        buttonImgView.setFitWidth(width);
        buttonImgView.setFitHeight(height);
        buttonImgView.setPreserveRatio(true);
        return buttonImgView;
    }

    /**
     * Configure a button with an image
     * @param button the button to configure
     * @param resourcePath the path to the image resource
     * @param width the desired width
     * @param height the desired height
     * @param id the button ID
     */
    protected void configureButton(Button button, String resourcePath, double width, double height, String id) {
        ImageView buttonImgView = createButtonImageView(resourcePath, width, height);
        button.setGraphic(buttonImgView);
        if (id != null && !id.isEmpty()) {
            button.setId(id);
        }
    }

    /**
     * Apply CSS styling to a scene
     * @param scene the scene to style
     */
    protected void applyCSS(Scene scene) {
        String css = petController.getCss();
        if (css != null) {
            scene.getStylesheets().add(css);
        }
    }

    /**
     * Initialize the UI components and setup
     */
    public abstract void initializeUI();
}
