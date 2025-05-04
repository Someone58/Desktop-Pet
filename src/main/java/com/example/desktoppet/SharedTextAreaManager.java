package com.example.desktoppet;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javafx.scene.control.TextArea;

public class SharedTextAreaManager {
    private StringProperty sharedContent = new SimpleStringProperty("");

    public void registerTextArea(TextArea textArea) {
        // Bidirectional binding
        textArea.textProperty().bindBidirectional(sharedContent);
    }

    public void appendText(String text) {
        Platform.runLater(() -> {
            sharedContent.set(sharedContent.get() + text + "\n");
        });
    }

    public StringProperty sharedContentProperty() {
        return sharedContent;
    }


}