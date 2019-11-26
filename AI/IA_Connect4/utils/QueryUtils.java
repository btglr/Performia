package utils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.*;

public class QueryUtils {
    public static URLConnection getHTTPConnection(URL url) throws IOException {
        // Establishing connection
        URLConnection connection;

        connection = url.openConnection();
        connection.setDoOutput(true);
        connection.setConnectTimeout(2000);

        return connection;
    }

    public static String sendHTTPMessage(String address, int port, String handler, String data) throws MalformedURLException {
        String fullUrl = String.format("http://%s:%d/%s", address, port, handler);
        StringBuilder builder = new StringBuilder();
        String result;

        // Creating the URL
        URL url = new URL(fullUrl);
        URLConnection connection;
        try {
            connection = getHTTPConnection(url);
        } catch (IOException e) {
            System.err.println("An error occurred while trying to connect: " + e);
            return null;
        }

        try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
            writer.write(data);
            writer.flush();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String tmp;
                while((tmp = reader.readLine()) != null) {
                    builder.append(tmp);
                }
            } catch(IOException e) {
                System.err.println("An error occurred while sending the request: " + e);
                return null;
            }

            result = builder.toString();
        } catch(IOException e) {
            System.err.println("An error occurred while sending the request: " + e);
            return null;
        }

        return result;
    }

    public static void sendHTTPResponse(HttpExchange t, String response) {
        // Sending HTTP headers
        try {
            Headers h = t.getResponseHeaders();

            // For sending JSON data
            h.set("Content-Type", "application/json");
            // For sending text or html
//            h.set("Content-Type", "text/html; charset=utf-8");

            // Required for cross-origin requests
            h.set("Access-Control-Allow-Origin", "*");
            t.sendResponseHeaders(200, response.getBytes().length);
        } catch(IOException e) {
            System.err.println("An error occurred while sending the headers: " + e);
        }

        // Sending the data
        try {
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch(IOException e) {
            System.err.println("An error occurred while sending the body: " + e);
        }
    }
}
