package serveur;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import utils.QueryUtils;

public class RequestHandler implements HttpHandler {
    public void handle(HttpExchange t) {
        String r = "<h1>Welcome</h1>";

        QueryUtils.sendHTTPResponse(t, r);
    }
}
