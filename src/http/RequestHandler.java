package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import requete.RequestQueue;
import requete.Requete;
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

        RequestQueue requestQueue = RequestQueue.getInstance();
        JSONObject jsonResponse = new JSONObject();

        // Vérifications de base
        if (parameters != null) {
            if (parameters.containsKey("code")) {
                RequestCode code = getRequest(Integer.parseInt(parameters.get("code")));
                Requete req = new Requete(code.getCode());

                // Le code permet d'identifier la requête
                switch (code) {
                    /**
                     * Détails de la requête de connexion
                     * @in login : string
                     * @in password : string
                     * @out connected : 0 ou 1
                     * si connected = 1
                     * @out id_utilisateur : int
                     */
                    case CONNECTION:
                        // TODO
                        break;

                    /**
                     * Détails de la requête de choix d'un challenge
                     * @in id_utilisateur : int
                     * @in numero_challenge : int
                     * @out reponse : int (ex : 1000 si ok, 1001 si pas ok (TODO : à déterminer)
                     * @out id_salle : int
                     * @out etat_jeu : JSONObject (ici, état initial)
                     * @out prochain_joueur : int (id du prochain joueur ?) (TODO : pour quelle raison ?)
                     */
                    case CHOOSE_CHALLENGE:
                        if (parameters.containsKey("id_utilisateur") && parameters.containsKey("numero_challenge")) {
                            int id_utilisateur = Integer.parseInt(parameters.get("id_utilisateur"));
                            int numero_challenge = Integer.parseInt(parameters.get("numero_challenge"));

                            req.addData("id_utilisateur", id_utilisateur);
                            req.addData("numero_challenge", numero_challenge);

                            System.out.println(timestamp + " - User has chosen a challenge");

                            if (requestQueue.addRequest(req)) {
                                System.out.println(timestamp + " - Request was added to the RequestQueue");
                            }
                        }

                        else {
                            System.out.println(timestamp + " - Missing a parameter with request CHOOSE_CHALLENGE");
                        }

                        break;

                    /**
                     * Détails de la requête envoyée lorsqu'un tour est joué
                     * @in id_utilisateur : int
                     * @in action : JSON ? (TODO : à déterminer)
                     * @out reponse : int (TODO : à déterminer)
                     */
                    case PLAY_TURN:
                        if (parameters.containsKey("id_utilisateur") && parameters.containsKey("action")) {
                            int id_utilisateur = Integer.parseInt(parameters.get("id_utilisateur"));

                            req.addData("id_utilisateur", id_utilisateur);

                            System.out.println(timestamp + " - User has made a move");

                            if (requestQueue.addRequest(req)) {
                                System.out.println(timestamp + " - Request was added to the RequestQueue");
                            }
                        }

                        else {
                            System.out.println(timestamp + " - Missing a parameter with request PLAY_TURN");
                        }

                        break;

                    /**
                     * Détails de la requête de demande d'état du jeu (périodiquement, utilisée pour rafraîchir l'interface)
                     * @in id_utilisateur : int (peut être optionnel, dépend si l'on autorise n'importe qui à "spectate" le jeu d'un autre)
                     * @in id_salle : int
                     * @out etat_jeu : JSONObject
                     */
                    case GET_GAME_STATE:
                        if (parameters.containsKey("id_utilisateur") && parameters.containsKey("id_salle")) {
                            int id_utilisateur = Integer.parseInt(parameters.get("id_utilisateur"));
                            int id_salle = Integer.parseInt(parameters.get("id_salle"));

                            req.addData("id_utilisateur", id_utilisateur);
                            req.addData("id_salle", id_salle);

                            System.out.println(timestamp + " - Web interface has asked for the game state");

                            if (requestQueue.addRequest(req)) {
                                System.out.println(timestamp + " - Request was added to the RequestQueue");
                            }
                        }

                        else {
                            System.out.println(timestamp + " - Missing a parameter with request GET_GAME_STATE");
                        }

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
