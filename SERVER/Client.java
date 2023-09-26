import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_IP = "130.251.254.112"; // Indirizzo IP del server
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_IP, PORT);

            // Creazione di oggetti per la lettura e la scrittura dei dati
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Invio dei dati al server
            String requestData = "Client: Salve, sono il client!";
            out.println(requestData);
            System.out.println("Dati inviati al server: " + requestData);

            // Lettura della risposta del server
            String responseData = in.readLine();
            System.out.println("Risposta ricevuta dal server: " + responseData);

            // Chiusura delle risorse
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}