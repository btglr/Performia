package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import requete.RequestQueue;
import requete.Message;
import requete.ResponseQueue;
import utils.MessageCode;
import utils.QueryUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utils.MessageCode.*;

public class RequestHandler implements HttpHandler {
    public void handle(HttpExchange exchange) {
        String query = exchange.getAttribute("query").toString();

        @SuppressWarnings("unchecked")
        Map<String, String> parameters = (Map<String, String>) exchange.getAttribute("parameters");

        Logger.getLogger(RequestHandler.class.getName()).log(Level.INFO, "Received query with parameters " + query);

        RequestQueue requestQueue = RequestQueue.getInstance();
        ResponseQueue responseQueue = ResponseQueue.getInstance();
        JSONObject jsonResponse = new JSONObject();

        // Vérifications de base
        if (parameters != null) {
            if (parameters.containsKey("code")) {
                MessageCode code = getRequest(Integer.parseInt(parameters.get("code")));
                Message req = new Message(code.getCode());

                // Le code permet d'identifier la requête
                switch (code) {
                    /**
                     * Détails de la requête de connexion
                     * @in code : 1
                     * @in login : string
                     * @in password : string
                     * Si ok
                     * @out code : 503
                     * @out id_utilisateur : int
                     * Si pas ok
                     * @out code : 1001
                     * @out error_message : string
                     */
                    case CONNECTION:
                        if (parameters.containsKey("login") && parameters.containsKey("password")) {
                            String login = parameters.get("login");
                            String password = parameters.get("password");

                            req.addData("login", login);
                            req.addData("password", password);

                            Logger.getLogger(RequestHandler.class.getName()).log(Level.INFO, "User has asked to connect");

                            if (requestQueue.addRequest(req)) {
                                Logger.getLogger(RequestHandler.class.getName()).log(Level.INFO, "Request was added to the RequestQueue");
                            }
                        }

                        else {
                            Logger.getLogger(RequestHandler.class.getName()).log(Level.INFO,"Missing a parameter with request CONNECTION");
                        }

                        break;

                    /**
                     * Détails de la requête de choix d'un challenge
                     * @in code : 2
                     * @in id_utilisateur : int
                     * @in numero_challenge : int
                     * Si ok
                     * @out code : 500
                     * @out etat_jeu : JSONObject (ici, état initial)
                     * Si pas ok
                     * @out code : 1002
                     * @out error_message : string
                     */
                    case CHOOSE_CHALLENGE:
                        if (parameters.containsKey("id_utilisateur") && parameters.containsKey("numero_challenge")) {
                            int id_utilisateur = Integer.parseInt(parameters.get("id_utilisateur"));
                            int numero_challenge = Integer.parseInt(parameters.get("numero_challenge"));

                            req.addData("id_utilisateur", id_utilisateur);
                            req.addData("numero_challenge", numero_challenge);

                            Logger.getLogger(RequestHandler.class.getName()).log(Level.INFO, "User has chosen a challenge");

                            if (requestQueue.addRequest(req)) {
                                Logger.getLogger(RequestHandler.class.getName()).log(Level.INFO, "Request was added to the RequestQueue");
                            }
                        }

                        else {
                            Logger.getLogger(RequestHandler.class.getName()).log(Level.INFO, "Missing a parameter with request CHOOSE_CHALLENGE");
                        }

                        break;

                    /**
                     * Détails de la requête envoyée lorsqu'un tour est joué
                     * @in code : 3
                     * @in id_utilisateur : int
                     * @in colonne : int (le numéro de colonne dans lequel placer le disque)
                     * Si ok
                     * @out code : 501
                     * @out etat_jeu : l'état du jeu après coup
                     * Si pas ok
                     * @out code : 1000
                     * @out error_message : string
                     */
                    case PLAY_TURN:
                        if (parameters.containsKey("id_utilisateur") && parameters.containsKey("colonne")) {
                            int id_utilisateur = Integer.parseInt(parameters.get("id_utilisateur"));
                            int colonne = Integer.parseInt(parameters.get("colonne"));

                            req.addData("id_utilisateur", id_utilisateur);
                            req.addData("colonne", colonne);

                            Logger.getLogger(RequestHandler.class.getName()).log(Level.INFO, "User has made a move");

                            if (requestQueue.addRequest(req)) {
                                Logger.getLogger(RequestHandler.class.getName()).log(Level.INFO, "Request was added to the RequestQueue");
                            }
                        }

                        else {
                            Logger.getLogger(RequestHandler.class.getName()).log(Level.INFO, "Missing a parameter with request PLAY_TURN");
                        }

                        break;

                    /**
                     * Détails de la requête de demande d'état du jeu (périodiquement, utilisée pour rafraîchir l'interface)
                     * @in code : 4
                     * @in id_utilisateur : int
                     * Si ok
                     * @out code : 502
                     * @out etat_jeu : JSONObject
                     * Si pas ok
                     * @out code : 1003
                     * @out error_message : string
                     */
                    case GET_GAME_STATE:
                        if (parameters.containsKey("id_utilisateur")) {
                            int id_utilisateur = Integer.parseInt(parameters.get("id_utilisateur"));

                            req.addData("id_utilisateur", id_utilisateur);

                            Logger.getLogger(RequestHandler.class.getName()).log(Level.INFO, "Web interface has asked for the game state");

                            if (requestQueue.addRequest(req)) {
                                Logger.getLogger(RequestHandler.class.getName()).log(Level.INFO, "Request was added to the RequestQueue");
                            }
                        }

                        else {
                            Logger.getLogger(RequestHandler.class.getName()).log(Level.INFO, "Missing a parameter with request GET_GAME_STATE");
                        }

                        break;

                    case UNKNOWN:
                    default:
                        Logger.getLogger(RequestHandler.class.getName()).log(Level.INFO, "Received an unknown request");
                }

                // ATTENTE DE LA REPONSE ICI
                synchronized (ResponseQueue.getLock()) {
                    int myRequestId = req.getId();

                    boolean notMyResponse = true;
                    while (notMyResponse) {
                        try {
                            while (responseQueue.isEmpty()) {
                                Logger.getLogger(RequestHandler.class.getName()).log(Level.INFO, "Going to sleep as I'm waiting for a response");
                                ResponseQueue.getLock().wait();
                            }
                        } catch (InterruptedException e) {
                            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, e);
                        }

                        Message message = responseQueue.getMessage();
                        jsonResponse = message.getData();
                        notMyResponse = (message.getDestination() != myRequestId);

                        if (notMyResponse) {
                            responseQueue.addResponse(message);
                        }

                        else {
                            Logger.getLogger(RequestHandler.class.getName()).log(Level.INFO, "Received response");
                        }
                    }
                }
            }

            else {
                // Erreur code manquant
            }
        }

        else {
            // Erreur aucun paramètre soumis
        }

        String response = jsonResponse.toString();
        QueryUtils.sendHTTPResponse(exchange, response);
    }
}
