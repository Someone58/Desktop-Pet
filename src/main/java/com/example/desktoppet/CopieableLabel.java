package com.example.desktoppet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.geometry.Insets;

import java.util.Objects;

public class CopieableLabel extends Label {
    public CopieableLabel() {
        addCopyButton();
    }

    public CopieableLabel(String text) {
        super(text);
        addCopyButton();
    }

    private void addCopyButton() {
        Button copyButton = new Button("");
        Image copyButtonImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/copy.png")));
        ImageView copyButtonImgView = new ImageView(copyButtonImg);
        copyButton.setGraphic(copyButtonImgView);

        copyButton.visibleProperty().bind(textProperty().isEmpty().not());
        copyButton.managedProperty().bind(textProperty().isEmpty().not());
        copyButton.setFocusTraversable(false);
        copyButton.setPadding(new Insets(0.0, 4.0, 0.0, 4.0));
        copyButton.setOnAction(actionEvent -> copyToClipboard(getText()));
        setGraphic(copyButton);
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
