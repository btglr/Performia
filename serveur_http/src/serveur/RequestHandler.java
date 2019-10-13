package serveur;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import utils.QueryUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class RequestHandler implements HttpHandler {
    public void handle(HttpExchange t) {
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

        String query = t.getAttribute("query").toString();
        Map<String, String> parameters = (Map<String, String>) t.getAttribute("parameters");

        System.out.println(timestamp + " - Received query with data " + query);

        String r = "<h1>Welcome</h1>";
        QueryUtils.sendHTTPResponse(t, r);
    }
}
