package tcp;

import org.json.JSONException;
import org.json.JSONObject;
import requete.Message;
import requete.MessageManager;
import requete.RequestQueue;
import requete.ResponseQueue;
import utils.MessageCode;
import utils.ProtocolType;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Noizet Mathieu
 */
public class ThreadGestionClient extends Thread {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public ThreadGestionClient(Socket socketClient) {
        socket = socketClient;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        } catch (IOException ex) {
            Logger.getLogger(ThreadGestionClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        // Traitement a faire sur une socket cliente
        boolean connected = false;
        boolean notMyResponse = true;
        RequestQueue requestQueue = RequestQueue.getInstance();
        ResponseQueue responseQueue = ResponseQueue.getInstance();
        String message = "";
        Message req = null;

        do {
            try {
                message = input.readLine();
            } catch (IOException ex) {
                Logger.getLogger(ThreadGestionClient.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }

            if (!message.equals("")) {
                try {
                    req = Message.fromJSON(new JSONObject(message));
                    req.setProtocolType(ProtocolType.TCP);
                } catch (JSONException ex) {
                    Logger.getLogger(ThreadGestionClient.class.getName()).log(Level.SEVERE, null, ex);
                    continue;
                }

                if (!requestQueue.addRequest(req)) {
                    System.err.println("Erreur lors de l'ajout de la requete dans la file");
                }

                Message m = null;
                // ATTENTE DE LA REPONSE ICI
                synchronized (ResponseQueue.getLock()) {
                    int myRequestId = req.getId();

                    while (notMyResponse) {
                        try {
                            while (responseQueue.isEmpty()) {
                                Logger.getLogger(ThreadGestionClient.class.getName()).log(Level.INFO, "Going to sleep as I'm waiting for a response");
                                ResponseQueue.getLock().wait();
                            }
                        } catch (InterruptedException e) {
                            Logger.getLogger(ThreadGestionClient.class.getName()).log(Level.SEVERE, null, e);
                        }

                        m = responseQueue.getMessage();
                        notMyResponse = (m.getDestination() != myRequestId);

                        if (notMyResponse) {
                            responseQueue.addResponse(m);
                        }

                        else {
                            Logger.getLogger(ThreadGestionClient.class.getName()).log(Level.INFO, "Received response");
                        }
                    }
                }

                if (m != null) {
                    Logger.getLogger(ThreadGestionClient.class.getName()).log(Level.INFO, "Received response: " + m.getData().toString());

                    connected = m.getCode() == MessageCode.CONNECTION_OK.getCode();

                    output.println(m.toJSON());
                }
            }
        } while (connected);

        try {
            socket.close();
            input.close();
            output.close();
        } catch (IOException ex) {
            Logger.getLogger(ThreadGestionClient.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
}
