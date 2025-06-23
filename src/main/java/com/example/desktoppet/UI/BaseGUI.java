package com.example.desktoppet.UI;

import com.example.desktoppet.Model.PetData;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
        if (this.stage == null) {
            this.stage = new Stage();
            petController.setStage(this.stage);
        }
    }

    /**
     * Create an ImageView for a button with the specified resource path
     * @param resourcePath the path to the image resource
     * @param width the desired width
     * @param height the desired height
     * @return configured ImageView instance
     */
    protected ImageView createButtonImageView(String resourcePath, double width, double height) {
        if (resourcePath == null || resourcePath.isEmpty()) {
            throw new IllegalArgumentException("Resource path cannot be null or empty");
        }

        try {
            Image buttonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(resourcePath)));
            ImageView buttonImgView = new ImageView(buttonImg);
            buttonImgView.setFitWidth(width);
            buttonImgView.setFitHeight(height);
            buttonImgView.setPreserveRatio(true);
            return buttonImgView;
        } catch (Exception e) {
            System.err.println("Error loading image from " + resourcePath + ": " + e.getMessage());
            // Fallback to empty image view
            ImageView fallbackView = new ImageView();
            fallbackView.setFitWidth(width);
            fallbackView.setFitHeight(height);
            return fallbackView;
        }
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
     * Create a styled title label
     * @param text the label text
     * @param id the label ID
     * @return configured Label instance
     */
    protected Label createTitleLabel(String text, String id) {
        Label titleLabel = new Label(text);
        if (id != null && !id.isEmpty()) {
            titleLabel.setId(id);
        }
        return titleLabel;
    }

    /**
     * Create a horizontal layout container
     * @param spacing spacing between elements
     * @param id container ID (optional)
     * @return configured HBox instance
     */
    protected HBox createHBox(double spacing, String id) {
        HBox hbox = new HBox(spacing);
        if (id != null && !id.isEmpty()) {
            hbox.setId(id);
        }
        return hbox;
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
     * Create a basic scene with the standard background color
     * @param root the root node for the scene
     * @param width the scene width
     * @param height the scene height
     * @return configured Scene instance with CSS applied
     */
    protected Scene createBasicScene(javafx.scene.Parent root, double width, double height) {
        Scene scene = new Scene(root, width, height);
        scene.setFill(Color.web("#B8CCCB"));
        applyCSS(scene);
        return scene;
    }

    /**
     * Initialize the UI components and setup
     */
    public abstract void initializeUI();
}
