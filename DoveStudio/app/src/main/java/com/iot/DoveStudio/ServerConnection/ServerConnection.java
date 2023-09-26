package com.iot.DoveStudio.ServerConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerConnection {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public ServerConnection() {
        // Inizializza la connessione al server nel costruttore
        String serverIpAddress = "172.20.10.3";
        int serverPort = 8080;

        try {
            socket = new Socket(serverIpAddress, serverPort);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            // Invia dati al server
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receiveMessage() {
        try {
            // Ricevi dati dal server
            byte[] buffer = new byte[2048];
            int bytesRead = inputStream.read(buffer);
            return new String(buffer, 0, bytesRead);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void closeConnection() {
        try {
            // Chiudi la connessione al server
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
