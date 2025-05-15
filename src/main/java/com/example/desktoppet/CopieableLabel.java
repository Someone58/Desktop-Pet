package com.example.desktoppet;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.geometry.Insets;

public class CopieableLabel extends Label {
    public CopieableLabel() {
        addCopyButton();
    }

    public CopieableLabel(String text) {
        super(text);
        addCopyButton();
    }

    private void addCopyButton() {
        Button button = new Button("📋");
        button.visibleProperty().bind(textProperty().isEmpty().not());
        button.managedProperty().bind(textProperty().isEmpty().not());
        button.setFocusTraversable(false);
        button.setPadding(new Insets(0.0, 4.0, 0.0, 4.0));
        button.setOnAction(actionEvent -> copyToClipboard(getText()));
        setGraphic(button);
        setContentDisplay(ContentDisplay.RIGHT);
    }

    // Utility method as a private static method
    private static void copyToClipboard(String text) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
    }
}
