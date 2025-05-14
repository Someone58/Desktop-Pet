package com.example.desktoppet;

import javafx.application.Platform;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.scene.control.TextArea;

public class NetworkManager {
    private PetController petController;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean isServer;
    private boolean connected = false;
    private static final int CONNECTION_TIMEOUT = 5000;

    private SharedTextAreaManager chatManager;
    private SharedTextAreaManager connectionManager;
    private SharedTextAreaManager statusManager;

    public boolean isConnected() {
        return connected;
    }

    public NetworkManager(PetController petController) {
        this.petController = petController;

        chatManager = petController.getChatManager();
        connectionManager = petController.getConnectionManager();
        statusManager = petController.getStatusManager();
    }

    public void startServer(int port) throws IOException {
        isServer = true;
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket()) {
                // Bind with a backlog of 1
                serverSocket.bind(new InetSocketAddress(port), 1);
                Platform.runLater(() -> {
                    appendConnectionMessage("Server started on port " + port);
                });
                socket = serverSocket.accept();
                setupStreams();
                connected = true;
                Platform.runLater(() -> {
                    notifyConnectionSuccess();
                    appendConnectionMessage("Client connected!\n");
                });
                listenForMessages();
            } catch (IOException e) {
                Platform.runLater(() -> {
                    notifyConnectionError();
                    appendConnectionMessage("Server error: " + e.getMessage() + "\n");
                });
            }
        }).start();
    }

    public void connectToServer(String host, int port) throws IOException {
        new Thread(() -> {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(host, port), CONNECTION_TIMEOUT);
                setupStreams();
                connected = true;
                Platform.runLater(() -> {
                    notifyConnectionSuccess();
                    appendConnectionMessage("Connected to server!\n");
                });
                listenForMessages();
            } catch (IOException e) {
                Platform.runLater(() -> {
                    notifyConnectionError();
                    appendConnectionMessage("Connection failed: " + e.getMessage() + "\n");
                });
            }
        }).start();
    }

    private void notifyConnectionSuccess() {
        connected = true;
        if (statusManager != null) {
            Platform.runLater(() -> {
                statusManager.sharedContentProperty().set("Connected");
            });
        }
    }

    private void notifyConnectionError() {
        connected = false;
        if (statusManager != null) {
            Platform.runLater(() -> {
                statusManager.sharedContentProperty().set("Disconnected");
            });
        }
    }

    private void setupStreams() throws IOException {
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private void listenForMessages() {
        new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    final String receivedMessage = message;
                    Platform.runLater(() -> {
                        appendMessage("Received: " + receivedMessage);
                    });
                }
            } catch (IOException e) {
                Platform.runLater(() -> {
                    notifyConnectionError();
                    appendMessage("Connection lost: " + e.getMessage());
                });
            }
        }).start();
    }

    public void sendMessage(String message) {
        if (out != null && connected) {
            out.println(message);
            Platform.runLater(() -> {
                appendMessage("Sent: " + message);
            });
        }
    }

    public boolean isServer() {
        return isServer;
    }

    private void appendMessage(String message) {
        if (chatManager != null) {
            chatManager.appendText(message);
        }
    }

    private void appendConnectionMessage(String message) {
        if (connectionManager != null) {
            connectionManager.appendText(message);
        }
    }

}