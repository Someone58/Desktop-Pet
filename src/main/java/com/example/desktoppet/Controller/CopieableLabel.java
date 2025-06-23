package com.example.desktoppet.Controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;

import java.util.Objects;

/**
 * A custom label class that allows copying text to clipboard
 */
public class CopieableLabel extends Label {
    private boolean copyOnClick = false;

    /**
     * Default constructor
     */
    public CopieableLabel() {
        super();
        initialize();
    }

    /**
     * Constructor with text
     * @param text the label text
     */
    public CopieableLabel(String text) {
        super(text);
        initialize();
    }

    /**
     * Constructor with text and copy-on-click behavior
     * @param text the label text
     * @param copyOnClick whether to copy on click
     */
    public CopieableLabel(String text, boolean copyOnClick) {
        super(text);
        this.copyOnClick = copyOnClick;
        initialize();
    }

    /**
     * Initialize the label with event handlers
     */
    private void initialize() {
        if (copyOnClick) {
            this.setOnMouseClicked(this::handleMouseClick);
        }
    }

    /**
     * Handles mouse click events
     * @param event the mouse event
     */
    private void handleMouseClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            copyToClipboard(this.getText());
        }
    }

    /**
     * Set whether to copy on click
     * @param copyOnClick true to copy on click, false otherwise
     */
    public void setCopyOnClick(boolean copyOnClick) {
        this.copyOnClick = copyOnClick;
        if (copyOnClick) {
            this.setOnMouseClicked(this::handleMouseClick);
        } else {
            this.setOnMouseClicked(null);
        }
    }

    /**
     * Copies text to the system clipboard
     * @param text the text to copy
     */
    public static void copyToClipboard(String text) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
    }
}
