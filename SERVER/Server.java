import java.io.*;
import java.net.*;
import java.time.*;

public class Server {
    private static final int PORT = 8080;
    private static final int BUFFER_SIZE = 2048;
    private static final String path = "C:/Users/omitk/Desktop/IoT/SERVER/Date.txt";
    //Dichiarazione di un array di oggetti MyStruct, con dimenzione CountLine che conta il numero di righe del file che contiente ne aule
    private static MyStruct[] Aule= new MyStruct[CountLine()];
    private static ServerSocket serverSocket= null;

    public static void main(String[] args){
        try {
            newFile();           

            //CREAZIONE AULE
            popolazioneStruct(Aule);

            serverSocket = new ServerSocket(PORT);
            System.out.println("Server in ascolto sulla porta " + PORT + "...");

            //AGGIORNAMENTO ASSENZA
            Thread thread = new Thread(() -> {
                synchronized (Aule) {                
                    while(true){
                        try {
                            Thread.sleep(120000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        
                        for(MyStruct aula: Aule){
                            LocalTime time= aula.currenTime;
                            time= time.
                            plusMinutes(2);
                            
                            if(time.compareTo(LocalTime.now()) < 0){
                                aula.update(false);
                                System.out.println("MODIFICA");
                            }
                        }
                    }
                }
            });
            thread.start();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuova connessione accettata.");

                // Creazione di oggetti per la lettura e la scrittura dei dati
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Lettura dei dati inviati dal client
                char[] buffer = new char[BUFFER_SIZE];
                int bytesRead = in.read(buffer, 0, BUFFER_SIZE);
                String requestData = new String(buffer, 0, bytesRead);

                String responseData= "";
                /***********************************************************************/
                if(requestData.equals("SHOW")){
                    for(MyStruct aula: Aule)
                        responseData += aula.getResults();
                }
                    
                else{
                    if(update_Presenza(requestData))
                        responseData= "Inserimento andato a buona termine";
                    else
                        responseData= "Inserimento fallito";
                }
                /***********************************************************************/
                
                // Invio della risposta al client
                out.println(responseData);
                System.out.println("Risposta inviata.");

                // Chiusura delle risorse
                in.close();
                out.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Listato 1. Creazione di un file vuoto
    public static void newFile() {
        try {
            File file = new File(path);
            if (file.exists())
                System.out.println("Il file " + path + " esiste");
            else if (file.createNewFile())
                System.out.println("Il file " + path + " e' stato creato");
            else
                System.out.println("Il file " + path + " non può essere creato");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Conta il numero di righe nel file
    private static int CountLine(){
        int lines= 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            while (reader.readLine() != null) 
                lines ++;   
        }catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    //Inizializza l'array di oggetti MyStruct
    private static void popolazioneStruct(MyStruct[] aule){
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            int riga= 0; 

            while ((line = reader.readLine()) != null) {
                String[] lineSubStrings;
                lineSubStrings= line.split(" ");

                aule[riga]= new MyStruct(lineSubStrings[0], Integer.valueOf(lineSubStrings[1]), false, lineSubStrings[2]);
                riga++;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean update_Presenza(String dati){
        boolean itsOk= false;
        
        for(MyStruct aula: Aule){
            if(aula.getBeacon().equals(dati)){
                aula.update(true);
                itsOk= true;
            }
        }

        return itsOk;
    }
}