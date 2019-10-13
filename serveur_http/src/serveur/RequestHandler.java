package serveur;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import utils.QueryUtils;
import utils.RequestCode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import static utils.RequestCode.*;

public class RequestHandler implements HttpHandler {
    public void handle(HttpExchange t) {
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

        String query = t.getAttribute("query").toString();
        Map<String, String> parameters = (Map<String, String>) t.getAttribute("parameters");

        System.out.println(timestamp + " - Received query with parameters " + query);

        // Vérifications de base
        if (parameters != null) {
            if (parameters.containsKey("code")) {
                RequestCode code = getRequest(Integer.parseInt(parameters.get("code")));

                // Le code permet d'identifier la requête
                switch (code) {
                    case CHOOSE_CHALLENGE:
                        System.out.println("User has chosen a challenge");
                        break;

                    case PLAY_TURN:
                        System.out.println("User had made a move");
                        break;

                    case UNKNOWN:
                    default:
                        System.out.println("Received an unknown request");
                }
            }
        }

        String r = "<h1>Welcome</h1>";
        QueryUtils.sendHTTPResponse(t, r);
    }
}
