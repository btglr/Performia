package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import utils.QueryUtils;
import utils.RequestCode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import static utils.RequestCode.*;

public class RequestHandler implements HttpHandler {
    public void handle(HttpExchange exchange) {
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

        String query = exchange.getAttribute("query").toString();

        @SuppressWarnings("unchecked")
        Map<String, String> parameters = (Map<String, String>) exchange.getAttribute("parameters");

        System.out.println(timestamp + " - Received query with parameters " + query);

        JSONObject jsonResponse = new JSONObject();

        // Vérifications de base
        if (parameters != null) {
            if (parameters.containsKey("code")) {
                RequestCode code = getRequest(Integer.parseInt(parameters.get("code")));

                // Le code permet d'identifier la requête
                switch (code) {
                    case CHOOSE_CHALLENGE:
                        System.out.println(timestamp + " - User has chosen a challenge");

                        jsonResponse.append("code", INITIAL_GAME_STATE.getCode());
                        // Etat du challenge à envoyer

                        break;

                    case PLAY_TURN:
                        System.out.println(timestamp + " - User had made a move");

                        // Vérifier si action est correcte ou non ? Avec qui ?

                        // Si ok
                        jsonResponse.append("code", ACTION_OK.getCode());

                        // Si pas ok
                        jsonResponse.append("code", ACTION_NOT_OK.getCode());

                        break;

                    case GET_GAME_STATE:
                        System.out.println(timestamp + " - Web interface has asked for the game state");

                        jsonResponse.append("code", GAME_STATE.getCode());

                        break;

                    case UNKNOWN:
                    default:
                        System.out.println(timestamp + " - Received an unknown request");
                }
            }
        }

        String response = jsonResponse.toString();
        QueryUtils.sendHTTPResponse(exchange, response);
    }
}
