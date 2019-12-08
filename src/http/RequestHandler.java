package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import requete.RequestQueue;
import requete.Message;
import requete.ResponseQueue;
import utils.MessageCode;
import utils.QueryUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utils.AccountType.*;
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
        boolean requestAdded = false;

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
                     * @out user_id : int
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

                            if ((requestAdded = requestQueue.addRequest(req))) {
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
                     * @in user_id : int
                     * @in challenge_id : int
                     * Si ok
                     * @out code : 500
                     * @out etat_jeu : JSONObject (ici, état initial)
                     * Si pas ok
                     * @out code : 1002
                     * @out error_message : string
                     */
                    case CHOOSE_CHALLENGE:
                        if (parameters.containsKey("user_id") && parameters.containsKey("challenge_id")) {
                            int user_id = Integer.parseInt(parameters.get("user_id"));
                            int challenge_id = Integer.parseInt(parameters.get("challenge_id"));

                            req.addData("user_id", user_id);
                            req.addData("challenge_id", challenge_id);

                            logger.info("User has chosen a challenge");

                            if ((requestAdded = requestQueue.addRequest(req))) {
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
                     * @in user_id : int
                     * @in colonne : int (le numéro de colonne dans lequel placer le disque)
                     * Si ok
                     * @out code : 501
                     * @out etat_jeu : l'état du jeu après coup
                     * Si pas ok
                     * @out code : 1000
                     * @out error_message : string
                     */
                    case PLAY_TURN:
                        if (parameters.containsKey("user_id") && parameters.containsKey("colonne") && parameters.containsKey("room_id")) {
                            int user_id = Integer.parseInt(parameters.get("user_id"));
                            int colonne = Integer.parseInt(parameters.get("colonne"));
                            int room_id = Integer.parseInt(parameters.get("room_id"));

                            req.addData("user_id", user_id);
                            req.addData("room_id", room_id);
                            req.addData("colonne", colonne);

                            logger.info("User has made a move");

                            if ((requestAdded = requestQueue.addRequest(req))) {
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
                     * @in user_id : int
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
                     * @in user_id : int
                     * Si ok
                     * @out code : 504
                     * Si pas ok
                     * @out code : 505
                     */
                    case WAIT_CHALLENGE_START:
                        if (parameters.containsKey("user_id") && parameters.containsKey("room_id")) {
                            int user_id = Integer.parseInt(parameters.get("user_id"));
                            int room_id = Integer.parseInt(parameters.get("room_id"));

                            req.addData("user_id", user_id);
                            req.addData("room_id", room_id);

                            if (code == WAIT_CHALLENGE_START) {
                                logger.info("Web interface is waiting for the game to start");
                            }

                            else {
                                logger.info("Web interface has asked for the game state");
                            }

                            if ((requestAdded = requestQueue.addRequest(req))) {
                                logger.info("Request was added to the RequestQueue");
                            }
                        }

                        else {
                            logger.info("Missing a parameter with request GET_CHALLENGE_STATE / WAIT_CHALLENGE_START");
                        }

                        break;

                    /**
                     * Détails de la requête de la liste des challeges
                     * @in code : 6
                     * @in user_id : int
                     * Si ok
                     * @out code : 508
                     * @out list_challenges : JSONArray [{"challenge_id" : XX, "challenge_name" : "name", "challenge_description" : "description"},{...},...]
                     * Si pas ok
                     * @out code :
                     */
                    case GET_LIST_CHALLENGE:
                        if (parameters.containsKey("user_id")) {
                            int user_id = Integer.parseInt(parameters.get("user_id"));

                            req.addData("user_id", user_id);

                            if ((requestAdded = requestQueue.addRequest(req))) {
                                logger.info("Request was added to the RequestQueue");
                            }
                        }

                        else {
                            logger.info("Missing a parameter with request GET_LIST_CHALLENGE");
                        }

                        break;

                    /**
                     * Détails de la requête de la liste des challenges
                     * @in code : 7
                     * @in user_id : int
                     * @in challenge_id : int
                     * Si ok
                     * @out code : 509
                     * Si pas ok
                     * @out code : 1000
                     * @out code : 1002
                     */
                    case GET_CHALLENGE_DETAILS:
                        if (parameters.containsKey("user_id") && parameters.containsKey("challenge_id")) {
                            int user_id = Integer.parseInt(parameters.get("user_id"));
                            int challenge_id = Integer.parseInt(parameters.get("challenge_id"));

                            req.addData("user_id", user_id);
                            req.addData("challenge_id", challenge_id);

                            if ((requestAdded = requestQueue.addRequest(req))) {
                                logger.info("Request was added to the RequestQueue");
                            }
                        }

                        else {
                            logger.info("Missing a parameter with request GET_CHALLENGE_DETAILS");
                        }

                        break;

                    /**
                     * Détails de la requête d'inscription
                     * @in code : 8
                     * @in login : string
                     * @in password : sha1
                     * @in birthdate : date
                     * @in gender : int
                     * Si ok
                     * @out code : 510
                     * @out user_id : int
                     * Si pas ok
                     * @out code : 1000
                     * @out code : 1002
                     */
                    case REGISTER:
                        if (parameters.containsKey("login") && parameters.containsKey("password") && parameters.containsKey("birthdate") && parameters.containsKey("gender")) {
                            String login = parameters.get("login");
                            String password = parameters.get("password");

                            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.FRANCE);

                            LocalDate birthdate = null;
                            try {
                                birthdate = LocalDate.parse(parameters.get("birthdate"), f);
                            } catch (DateTimeParseException e) {
                                logger.log(Level.SEVERE, null, e);
                            }

                            int gender = Integer.parseInt(parameters.get("gender"));

                            if (birthdate != null) {
                                req.addData("login", login);
                                req.addData("password", password);
                                req.addData("birthdate", birthdate);
                                req.addData("gender", gender);
                                req.addData("account_type", USER.getValue());

                                if ((requestAdded = requestQueue.addRequest(req))) {
                                    logger.info("Request was added to the RequestQueue");
                                }
                            }

                            else {
                                logger.info("Date parameter was incorrect with request REGISTER");
                            }
                        }

                        else {
                            logger.info("Missing a parameter with request REGISTER");
                        }

                        break;

                    case GET_AI_LIST:
                        if (parameters.containsKey("user_id")) {
                            int user_id = Integer.parseInt(parameters.get("user_id"));
                            req.addData("user_id", user_id);

                            if ((requestAdded = requestQueue.addRequest(req))) {
                                logger.info("Request was added to the RequestQueue");
                            }
                        }

                        else {
                            logger.info("Missing a parameter with request GET_AI_LIST");
                        }

                        break;

                    /**
                     * Détails de la requête pour que l'user dise si c'est une AI ou non
                     * @in code : 9
                     * @in user_id : int (l'id joueur)
                     * @in user_id_2 : int (l'id adversaire)
                     * @in is_AI : int (1 = vrai, 0 = faux)
                     *
                     **/
                    case GUESS_IS_AI:
                        if (parameters.containsKey("user_id") && parameters.containsKey("is_AI") && parameters.containsKey("user_id_2")) {
                            int id_user_1 = 0, id_user_2 = 0, is_AI = -1;
                            try {
                                id_user_1 = Integer.parseInt(parameters.get("user_id"));
                                id_user_2 = Integer.parseInt(parameters.get("user_id_2"));
                                is_AI = Integer.parseInt(parameters.get("is_AI"));
                            }
                            catch (Exception e) {
                                logger.log(Level.SEVERE, null, e);
                            }
                            if(id_user_1 != 0 && id_user_2 != 0 && is_AI != -1) {
                                req.addData("user_id", id_user_1);
                                req.addData("user_id_2", id_user_2);
                                req.addData("is_AI", id_user_1);
                                if ((requestAdded = requestQueue.addRequest(req))) {
                                    logger.info("Request was added to the RequestQueue");
                                }
                            }
                            else {
                                logger.info("Date parameter was incorrect with request GUESS_IS_AI");
                            }
                        }

                        else {
                            logger.info("Missing a parameter with request GUESS_IS_AI");
                        }
                        break;
                    /**
                     * Détails de la requête pour obtenir les stats de leur IA
                     * @in code : 10
                     * @in user_id : int (l'id joueur)
                     **/
                    case GET_STATS:
                        if (parameters.containsKey("user_id")) {
                            int user_id = 0;
                            try {
                                user_id = Integer.parseInt(parameters.get("user_id"));
                            }
                            catch (Exception e) {
                                logger.log(Level.SEVERE, null, e);
                            }
                            if(user_id != 0) {
                                req.addData("user_id", user_id);
                                if ((requestAdded = requestQueue.addRequest(req))) {
                                    logger.info("Request was added to the RequestQueue");
                                }
                            }
                            else {
                                logger.info("Date parameter was incorrect with request GET_STATS");
                            }
                        }

                        else {
                            logger.info("Missing a parameter with request GET_STATS");
                        }
                        break;
                    case UNKNOWN:
                    default:
                        logger.info("Received an unknown request");
                }

                if (requestAdded) {
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
                    if (!jsonResponse.has("code")) {
                        jsonResponse.put("code", MISSING_PARAMETERS.getCode());
                    }
                }
            }

            else {
                jsonResponse.put("code", MISSING_REQUEST_CODE.getCode());
            }
        }

        else {
            jsonResponse.put("code", MISSING_PARAMETERS.getCode());
        }

        String response = jsonResponse.toString();
        QueryUtils.sendHTTPResponse(exchange, response);
    }
}
