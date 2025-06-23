package com.example.desktoppet.Controller;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * Base button class with common functionality for styled buttons
 */
public class BaseButton extends Button {
    /**
     * Default constructor
     */
    public BaseButton() {
        super();
    }

    /**
     * Constructor with text
     * @param text the button text
     */
    public BaseButton(String text) {
        super(text);
    }

    /**
     * Configures the button with an image
     * @param resourcePath path to the image resource
     * @param width width of the image
     * @param height height of the image
     * @param id button ID
     */
    public void configureWithImage(String resourcePath, double width, double height, String id) {
        Image buttonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(resourcePath)));
        ImageView buttonImgView = new ImageView(buttonImg);
        buttonImgView.setFitWidth(width);
        buttonImgView.setFitHeight(height);
        buttonImgView.setPreserveRatio(true);
        this.setGraphic(buttonImgView);

        if (id != null && !id.isEmpty()) {
            this.setId(id);
        }
    }
}
