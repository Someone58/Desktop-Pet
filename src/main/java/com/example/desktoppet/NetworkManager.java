
package com.example.desktoppet;

import javafx.application.Platform;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkManager {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean isServer;
    private Logic gameLogic;
    private Window window;
    private boolean connected = false;
    private static final int CONNECTION_TIMEOUT = 5000;

    public boolean isConnected() {
        return connected;
    }

    public NetworkManager(Logic logic, Window window) {
        this.gameLogic = logic;
        this.window = window;
    }

    public void startServer(int port) throws IOException {
        isServer = true;
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket()) {
                // Bind with a backlog of 1
                serverSocket.bind(new InetSocketAddress(port), 1);
                System.out.println("Server started on port " + port);
                socket = serverSocket.accept();
                setupStreams();
                connected = true;
                Platform.runLater(() -> {
                    notifyConnectionSuccess();
                    window.appendToChatArea("Server started. Waiting for client...\n");
                });
                listenForMessages();
            } catch (IOException e) {
                Platform.runLater(() -> {
                    notifyConnectionError();
                    window.appendToChatArea("Server error: " + e.getMessage() + "\n");
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
                    window.appendToChatArea("Connected to server!\n");
                });
                listenForMessages();
            } catch (IOException e) {
                Platform.runLater(() -> {
                    notifyConnectionError();
                    window.appendToChatArea("Connection failed: " + e.getMessage() + "\n");
                });
            }
        }).start();
    }

    private void notifyConnectionSuccess() {
        window.updateTurnStatus();
    }

    private void notifyConnectionError() {
        connected = false;
        window.updateTurnStatus();
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
                        window.appendToChatArea("Received: " + receivedMessage + "\n");
                    });
                }
            } catch (IOException e) {
                Platform.runLater(() -> {
                    notifyConnectionError();
                    window.appendToChatArea("Connection lost: " + e.getMessage() + "\n");
                });
            }
        }).start();
    }

    public void sendMessage(String message) {
        if (out != null && connected) {
            out.println(message);
            Platform.runLater(() -> {
                window.appendToChatArea("Sent: " + message + "\n");
            });
        }
    }

    public boolean isServer() {
        return isServer;
    }
}