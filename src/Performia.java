import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import http.ParameterFilter;
import http.RequestHandler;
import requete.*;
import tcp.ServeurTCP;

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

        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(25000), 0);
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
