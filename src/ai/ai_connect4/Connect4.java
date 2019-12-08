package ai.ai_connect4;

import org.json.JSONException;
import org.json.JSONObject;
import requete.Message;
import tcp.ThreadGestionClient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utils.AccountType.AI;

public class Connect4 {
    private static final Logger logger = Logger.getLogger(Connect4.class.getName());

    public static void main(String[] args) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(40000);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
            return;
        }

        boolean stop = false;

        Socket socketClient = null;
        ThreadConnect4 connect4;

        Message test = new Message();
        test.setCode(800);
        test.addData("login", "connect4_random");
        test.addData("password", "connect4_random");
        test.addData("host", "ec2-35-180-228-52.eu-west-3.compute.amazonaws.com");
        test.addData("port", 30000);
        test.addData("account_type", AI.getValue());

        System.out.println("test.toJSON() = " + test.toJSON());

        while (!stop) {
            Message req = null;

            try {
                socketClient = serverSocket.accept();
                logger.info("Socket client has connected");
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }

            BufferedReader input = null;
            PrintWriter output = null;
            try {
                input = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
                output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream())), true);
            } catch (IOException e) {
                logger.log(Level.SEVERE, null, e);
            }

            if (input != null) {
                String message = null;

                try {
                    message = input.readLine();
                } catch (IOException e) {
                    logger.info("Socket client has disconnected");
                }

                if (message != null && !message.equals("")) {
                    logger.info("Received message");

                    try {
                        req = Message.fromJSON(new JSONObject(test.toJSON().toString()));
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
                            connect4 = new ThreadConnect4(socketClient, login, password, host, port, account_type);
                            Thread t = new Thread(connect4);
                            t.start();
                        }

                        else {
                            logger.info("Request missing parameters");
                        }
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