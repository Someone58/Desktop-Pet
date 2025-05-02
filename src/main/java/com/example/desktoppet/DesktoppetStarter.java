package com.example.desktoppet;
import javafx.application.Application;

/**
 * Main class
 */
public class DesktoppetStarter {
    public static void main(String[] args) {
        Application.launch(PetWindow.class, args);
        System.setProperty("javafx.version", "21");
    }
}
