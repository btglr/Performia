package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import data.Config;
import org.json.JSONObject;
import requete.Message;
import utils.QueryUtils;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.logging.Logger;

import static utils.AccountType.AI;

public class CommandHandler  implements HttpHandler {
    private static final Logger logger = Logger.getLogger(CommandHandler.class.getName());

    public void handle(HttpExchange exchange) {
        String query = exchange.getAttribute("query").toString();
        JSONObject jsonResponse = new JSONObject();

        @SuppressWarnings("unchecked")
        Map<String, String> parameters = (Map<String, String>) exchange.getAttribute("parameters");

        if (parameters != null) {
            logger.info("Received command with parameters " + parameters);

            if (parameters.containsKey("host") && parameters.containsKey("port") && parameters.containsKey("login") && parameters.containsKey("password")) {
                String host = parameters.get("host");
                int port = Integer.parseInt(parameters.get("port"));
                String login = parameters.get("login");
                String password = parameters.get("password");

                logger.info("All parameters are correct");

                Config config = new Config("config/config.json");

                Socket socket;
                PrintWriter out;
                BufferedReader in;
                try {
                    logger.info("Starting socket");
                    socket = new Socket(host, port);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                    Message req = new Message();
                    req.addData("login", login);
                    req.addData("password", password);
                    req.addData("host", config.getString("ip"));
                    req.addData("port", config.getInt("port_tcp"));
                    req.addData("account_type", AI.getValue());

                    logger.info("Sending command to AI");

                    out.println(req.toJSON());

                    logger.info("Waiting for response");
                    String result = in.readLine();

                    if (result.equalsIgnoreCase("ok")) {
                        jsonResponse.put("status", "started");
                    }
                } catch (UnknownHostException e) {
                    System.err.println("Erreur sur l'hôte : " + e);
                } catch (IOException e) {
                    System.err.println("Création de la socket impossible : " + e);
                }
            }
        }

        logger.info("Sending HTTP response to origin");
        String response = jsonResponse.toString();
        QueryUtils.sendHTTPResponse(exchange, response);
    }
}
