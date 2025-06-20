package com.example.desktoppet.Controller;

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
        super();
    }

    public CopieableLabel(String text) {
        super(text);
    }

    public static void copyToClipboard(String text) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
    }

}
