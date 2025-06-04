package com.example.desktoppet;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class PetController {
    //Window
    String css = this.getClass().getResource("/application.css").toExternalForm();
    String darkmode = this.getClass().getResource("/darkmode.css").toExternalForm();
    Notification notification = new Notification(this);

    boolean timeropened = false;
    boolean minitimeropened = false;
    boolean chatopened = false;
    double petSize = 100;
    double xSpeed = 1.5;

    private Stage primaryStage = new Stage();
    Stage stage = new Stage();
    private Scene windowScene;
    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

    SharedTextAreaManager chatManager = new SharedTextAreaManager();
    SharedTextAreaManager connectionManager = new SharedTextAreaManager();
    SharedTextAreaManager statusManager = new SharedTextAreaManager();
//    NetworkManager networkManager; //??
    NetworkManager networkManager = new NetworkManager(this);
    private TextArea connectionStatus = new TextArea();

    Button chatButton = new Button("Chat");

    Button networkButton = new Button("Network");

    Button timerButton = new Button("Timer");

    Button pinWindow = new Button("Pin Window");
    boolean windowOnTop = true;

    Button petButton = new Button("Select Pet");
    PetSelect petSelect;

    Button settingsButton = new Button("Settings");

    //PetSelect
    Settings settings = new Settings(this);
    Image sharkIdle = new Image(getClass().getResource("/Pets/Shark_Animation.gif").toExternalForm());
    Image hedgehogIdle = new Image(getClass().getResource("/Pets/hedgehogIdle.png").toExternalForm());

    //PetAnimation
    ImageView petImage = new ImageView();

    //Notification

    public Notification getNotification() {
//        System.out.println(notification);
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
    }

    public Scene getWindowScene() {
        return windowScene;
    }

    public void setWindowScene(Scene scene) {
        this.windowScene = scene;
    }

    public Rectangle2D getScreenBounds() {
        return screenBounds;
    }

    public void setScreenBounds(Rectangle2D screenBounds) {
        this.screenBounds = screenBounds;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Image getSharkIdle() {
        return sharkIdle;
    }

    public void setSharkIdle(Image sharkIdle) {
        this.sharkIdle = sharkIdle;
    }

    public Image getHedgehogIdle() {
        return hedgehogIdle;
    }

    public void setHedgehogIdle(Image hedgehogIdle) {
        this.hedgehogIdle = hedgehogIdle;
    }

    public ImageView getPetImage() {
        return petImage;
    }

    public void setPetImage(ImageView petImage) {
        this.petImage = petImage;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public void setNetworkManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    public SharedTextAreaManager getStatusManager() {
        return statusManager;
    }

    public void setStatusManager(SharedTextAreaManager statusManager) {
        this.statusManager = statusManager;
    }

    public TextArea getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(TextArea connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public SharedTextAreaManager getConnectionManager() {
        return connectionManager;
    }

    public void setConnectionManager(SharedTextAreaManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public SharedTextAreaManager getChatManager() {
        return chatManager;
    }

    public void setChatManager(SharedTextAreaManager chatManager) {
        this.chatManager = chatManager;
    }

    public boolean isChatopened() {
        return chatopened;
    }

    public void setChatopened(boolean chatopened) {
        this.chatopened = chatopened;
    }

    public double getPetSize() {
        return petSize;
    }

    public void setPetSize(double petSize) {
        this.petSize = petSize;
    }

    public double getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(double xSpeed) {
        this.xSpeed = xSpeed;
    }

    public String getDarkmode() {
        return darkmode;
    }

    public void setDarkmode(String darkmode) {
        this.darkmode = darkmode;
    }

    public boolean isTimeropened() {
        return timeropened;
    }

    public void setTimeropened(boolean timeropened) {
        this.timeropened = timeropened;
    }

    public boolean isMinitimeropened() {
        return minitimeropened;
    }

    public void setMinitimeropened(boolean minitimeropened) {
        this.minitimeropened = minitimeropened;
    }
}
