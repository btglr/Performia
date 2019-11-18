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
    private static final Logger logger = Logger.getLogger(ThreadGestionClient.class.getName());

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public ThreadGestionClient(Socket socketClient) {
        socket = socketClient;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        // Traitement a faire sur une socket cliente
        boolean connected = false;
        RequestQueue requestQueue = RequestQueue.getInstance();
        ResponseQueue responseQueue = ResponseQueue.getInstance();
        String message;
        Message req;

        do {
            try {
                message = input.readLine();
            } catch (IOException e) {
                logger.info("Socket client has disconnected");
                message = null;
            }

            if (message != null && !message.equals("")) {
                try {
                    req = Message.fromJSON(new JSONObject(message));
                } catch (JSONException ex) {
                    logger.log(Level.SEVERE, null, ex);
                    continue;
                }

                logger.info("Received query with parameters " + message);

                if (requestQueue.addRequest(req)) {
                    logger.info("Request was added to the RequestQueue from TCP Thread");
                }

                Message m = null;
                // ATTENTE DE LA REPONSE ICI
                synchronized (ResponseQueue.getLock()) {
                    int myRequestId = req.getId();

                    boolean notMyResponse = true;
                    while (notMyResponse) {
                        try {
                            while (responseQueue.isEmpty()) {
                                logger.info("Going to sleep as I'm waiting for a response");
                                ResponseQueue.getLock().wait();
                            }
                        } catch (InterruptedException e) {
                            logger.log(Level.SEVERE, null, e);
                        }

                        m = responseQueue.getMessage();
                        notMyResponse = (m.getDestination() != myRequestId);

                        if (notMyResponse) {
                            responseQueue.addResponse(m);
                        }

                        else {
                            logger.info("Received response: " + m.getData().toString());
                        }
                    }
                }

//                connected = m.getCode() != MessageCode.LOGOUT_OK.getCode();
//                connected = m.getCode() == MessageCode.CONNECTION_OK.getCode();
                connected = true;

                output.println(m.toJSON());
            }

            else {
                connected = false;
            }
        } while (connected);

        try {
            socket.close();
            input.close();
            output.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
       
    }
}
