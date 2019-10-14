package http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

public class ServerHTTP {
    public static void main(String[] args) {
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(25633), 0);
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
    }
}
