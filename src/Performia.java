import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import data.Config;
import data.DBManager;
import http.ParameterFilter;
import http.RequestHandler;
import requete.*;
import tcp.ServeurTCP;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Performia {
    public static void main(String[] args) {
        RequestQueue requestQueue = RequestQueue.getInstance();
        ResponseQueue responseQueue = ResponseQueue.getInstance();
        RequeteManager gestionnaireRequete = RequeteManager.getInstance();

        requestQueue.addManager(gestionnaireRequete);
        responseQueue.addManager(gestionnaireRequete);
        Config config = new Config("config/config.json");

        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(config.getInt("port_http")), 0);
        } catch(IOException e) {
            System.err.println("An error occurred while creating the server: " + e);
            System.exit(-1);
        }

        HttpContext request = server.createContext("/request", new RequestHandler());
        request.getFilters().add(new ParameterFilter());

        server.setExecutor(null);
        server.start();

        String timestamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        System.out.println(timestamp + " - HTTP Server started. Press CTRL+C to stop.");

        Thread serveurTCP = new Thread(new ServeurTCP(25154));
        serveurTCP.start();

        Thread requestThread = new Thread(gestionnaireRequete);
        requestThread.start();
    }
}
