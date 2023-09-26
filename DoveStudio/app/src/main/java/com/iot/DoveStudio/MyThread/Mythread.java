package com.iot.DoveStudio.MyThread;

import com.iot.DoveStudio.ServerConnection.ServerConnection;

public class Mythread implements Runnable{
    private ServerConnection serverConnection;
    private String TAG= "MyThread";
    private String message;
    private String typeMessage;


    private void sendMessageToServer(String message) {
        // Invia un messaggio al server utilizzando la connessione
        serverConnection.sendMessage(message);
    }

    private String receiveMessageFromServer() {
        // Ricevi un messaggio dal server utilizzando la connessione
        return serverConnection.receiveMessage();
    }

    @Override
    public void run() {
        // Inizializza la connessione al server
        serverConnection = new ServerConnection();

        sendMessageToServer(typeMessage);

        message= receiveMessageFromServer();

        serverConnection.closeConnection();
    }

    public String getMessage(){
        return message;
    }

    public void setTypeMessage(String type){
        typeMessage= type;
    }
}