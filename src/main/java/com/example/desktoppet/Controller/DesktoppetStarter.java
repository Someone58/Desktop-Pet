package com.example.desktoppet.Controller;
import javafx.application.Application;

/**
 * Main class that launches the desktop pet application
 */
public class DesktoppetStarter {
    public static void main(String[] args) {
        // Set Java version property before launching
        System.setProperty("javafx.version", "21");
        // Launch the application using the refactored Window class
        Application.launch(Window.class, args);
    }
}
