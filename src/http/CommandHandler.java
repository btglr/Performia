package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import data.Config;
import requete.Message;

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

        @SuppressWarnings("unchecked")
        Map<String, String> parameters = (Map<String, String>) exchange.getAttribute("parameters");

        logger.info("Received command with parameters " + query);

        if (parameters != null) {
            if (parameters.containsKey("host") && parameters.containsKey("port") && parameters.containsKey("login") && parameters.containsKey("password")) {
                String host = parameters.get("host");
                int port = Integer.parseInt(parameters.get("host"));
                String login = parameters.get("login");
                String password = parameters.get("password");

                Config config = new Config("config/config.json");

                Socket socket;
                PrintWriter out;
                try {
                    socket = new Socket(host, port);
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                    Message req = new Message();
                    req.addData("login", login);
                    req.addData("password", password);
                    req.addData("host", config.getString("ip"));
                    req.addData("port", config.getInt("port_tcp"));
                    req.addData("account_type", AI.getValue());

                    out.println(req.toJSON());
                } catch (UnknownHostException e) {
                    System.err.println("Erreur sur l'hôte : " + e);
                } catch (IOException e) {
                    System.err.println("Création de la socket impossible : " + e);
                }
            }
        }
    }
}
