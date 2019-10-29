package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import requete.RequestQueue;
import requete.Message;
import requete.ResponseQueue;
import utils.MessageCode;
import utils.QueryUtils;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utils.MessageCode.*;

public class RequestHandler implements HttpHandler {
    private static final Logger logger = Logger.getLogger(RequestHandler.class.getName());
    
    public void handle(HttpExchange exchange) {
        String query = exchange.getAttribute("query").toString();

        @SuppressWarnings("unchecked")
        Map<String, String> parameters = (Map<String, String>) exchange.getAttribute("parameters");

        logger.info("Received query with parameters " + query);

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

                            logger.info("User has asked to connect");

                            if (requestQueue.addRequest(req)) {
                                logger.info("Request was added to the RequestQueue");
                            }
                        }

                        else {
                            logger.info("Missing a parameter with request CONNECTION");
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

                            logger.info("User has chosen a challenge");

                            if (requestQueue.addRequest(req)) {
                                logger.info("Request was added to the RequestQueue");
                            }
                        }

                        else {
                            logger.info("Missing a parameter with request CHOOSE_CHALLENGE");
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

                            logger.info("User has made a move");

                            if (requestQueue.addRequest(req)) {
                                logger.info("Request was added to the RequestQueue");
                            }
                        }

                        else {
                            logger.info("Missing a parameter with request PLAY_TURN");
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
                    case GET_CHALLENGE_STATE:
                        // Pas de break, même traitement que la requête du dessous

                    /**
                     * Attente de l'interface pour le démarrage du challenge
                     * @in code : 5
                     * @in id_utilisateur : int
                     * Si ok
                     * @out code : 504
                     * Si pas ok
                     * @out code : 505
                     */
                    case WAIT_CHALLENGE_START:
                        if (parameters.containsKey("id_utilisateur")) {
                            int id_utilisateur = Integer.parseInt(parameters.get("id_utilisateur"));

                            req.addData("id_utilisateur", id_utilisateur);

                            if (code == WAIT_CHALLENGE_START) {
                                logger.info("Web interface is waiting for the game to start");
                            }

                            else {
                                logger.info("Web interface has asked for the game state");
                            }

                            if (requestQueue.addRequest(req)) {
                                logger.info("Request was added to the RequestQueue");
                            }
                        }

                        else {
                            logger.info("Missing a parameter with request GET_CHALLENGE_STATE / WAIT_CHALLENGE_START");
                        }

                        break;

                    case UNKNOWN:
                    default:
                        logger.info("Received an unknown request");
                }

                // ATTENTE DE LA REPONSE ICI
                synchronized (ResponseQueue.getLock()) {
                    int myRequestId = req.getId();

                    boolean notMyResponse = true;
                    while (notMyResponse) {
                        try {
                            while (responseQueue.isEmpty()) {
                                logger.info("Going to sleep as I'm waiting for a response");
                                ResponseQueue.getLock().wait();
                            }
                        } catch (InterruptedException e) {
                            logger.log(Level.SEVERE, null, e);
                        }

                        Message message = responseQueue.getMessage();
                        jsonResponse = message.getData();
                        notMyResponse = (message.getDestination() != myRequestId);

                        if (notMyResponse) {
                            responseQueue.addResponse(message);
                        }

                        else {
                            logger.info("Received response: " + message.getData().toString());
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
