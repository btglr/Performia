package ai.ai_reflex;

import org.json.JSONException;
import org.json.JSONObject;
import requete.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Reflex {
    private static final Logger logger = Logger.getLogger(Reflex.class.getName());

    public static void main(String[] args) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(40001);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
            return;
        }

        boolean stop = false;

        Socket socketClient = null;
        ThreadReflex reflex;

        while (!stop) {
            Message req = null;

            try {
                socketClient = serverSocket.accept();
                logger.info("Socket client has connected");
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
                System.exit(-1);
            }

            BufferedReader input = null;
            PrintWriter output = null;
            try {
                input = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
                output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream())), true);
            } catch (IOException e) {
                logger.log(Level.SEVERE, null, e);
                System.exit(-1);
            }

            String message = null;

            try {
                message = input.readLine();
            } catch (IOException e) {
                logger.info("Socket client has disconnected");
            }

            if (message != null && !message.equals("")) {
                logger.info("Received message");

                try {
                    req = Message.fromJSON(new JSONObject(message));
                } catch (JSONException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }

                if (req != null) {
                    logger.info("Request was properly formed");

                    JSONObject obj = req.getData();

                    if (obj.has("login") && obj.has("password") && obj.has("host") && obj.has("port") && obj.has("account_type")) {
                        String login = obj.getString("login");
                        String password = obj.getString("password");
                        String host = obj.getString("host");
                        int port = obj.getInt("port");
                        int account_type = obj.getInt("account_type");

                        logger.info("Starting new Connect4 thread");
                        reflex = new ThreadReflex(socketClient, login, password, host, port, account_type);
                        Thread t = new Thread(reflex);
                        t.start();

                        output.println("OK");
                    }

                    else {
                        logger.info("Request missing parameters");
                    }
                }
            }
        }

        try {
            serverSocket.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
}