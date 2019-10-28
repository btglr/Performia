/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requete;

import challenge.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import data.DBManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;
import utils.MessageCode;

import static utils.MessageCode.getRequest;

/**
 *
 * @author Noizet Mathieu
 */
public class MessageManager implements Runnable {
    private RequestQueue requestQueue;
    private ResponseQueue responseQueue;
    private static final Object lock = new Object();
    private static volatile MessageManager instance = null;

    private ArrayList<Participant> participants = new ArrayList<>();
    private ArrayList<Salle> rooms = new ArrayList<>();

    private MessageManager(RequestQueue requestQueue, ResponseQueue responseQueue) {
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
    }

    public static MessageManager getInstance() {
        MessageManager r = instance;

        if (r == null) {
            synchronized (lock) {
                r = instance;
                if (r == null) {
                    r = new MessageManager(RequestQueue.getInstance(), ResponseQueue.getInstance());
                    instance = r;
                }
            }
        }

        return r;
    }

    public static Object getLock() {
        return lock;
    }

    public void run() {
        Message req;

        while (true) {
            // Attente passive d'une requête
            while (requestQueue.isEmpty()) {
                synchronized (RequestQueue.getLock()) {
                    Logger.getLogger(MessageManager.class.getName()).log(Level.INFO, "Going to sleep as I don't have any messages to process");

                    try {
                        RequestQueue.getLock().wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MessageManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            // Attente terminée, on a été notify
            req = requestQueue.getMessage();
            int sourceId = req.getId();

            MessageCode code = getRequest(req.getCode());
            Message response = new Message();
            JSONObject jsonObject;

            switch (code) {
                // Authentification
                case CONNECTION: {
                    int id = -1;
                    try {
                        id = connexion(req);
                    } catch (SQLException e) {
                        Logger.getLogger(MessageManager.class.getName()).log(Level.SEVERE, null, e);
                    }

                    if (id == -1) {
                        response.setCode(MessageCode.CONNECTION_ERROR.getCode());
                        response.addData("error_message", "Connection was not successful");
                    }

                    else {
                        response.setCode(MessageCode.CONNECTION_OK.getCode());
                        response.addData("id_utilisateur", id);
                    }

                    response.setProtocolType(req.getProtocolType());
                }

                break;

                // Choix d'un challenge
                case CHOOSE_CHALLENGE:
                    choisirChallenge(req);
                    break;

                // Jouer un tour
                case PLAY_TURN:
                    jsonObject = jouerTour(req);

                    response.setCode(MessageCode.PLAY_TURN.getCode());
                    response.addData("data", jsonObject);

                    break;

                // Demande d'actualisation de l'état du jeu
                case GET_GAME_STATE:
                    jsonObject = actualisation(req);

                    response.setCode(MessageCode.GAME_STATE.getCode());
                    response.addData("data", jsonObject);

                    break;
            }

            response.setDestination(sourceId);

            if (responseQueue.addResponse(response)) {
                Logger.getLogger(MessageManager.class.getName()).log(Level.INFO, "Response was added to the ResponseQueue");
            }
        }
    }

    public int connexion(Message requete) throws SQLException {
        String login = "", password = "";
        int id = -1;
        ResultSet resultat;

        try {
            login = requete.getData().getString("login");
            password = requete.getData().getString("password");
        } catch (JSONException e) {
            Logger.getLogger(MessageManager.class.getName()).log(Level.SEVERE, null, e);
        }

        DBManager db = new DBManager();

        Connection dbConnection;
        try {
            dbConnection = db.getConnection();
        } catch (SQLException e) {
            System.err.println("An exception occurred while creating the connection to the dabatase. Please check that the database is online.");
            return -1;
        } catch (JSONException e) {
            System.err.println("An exception occurred while creating the connection to the dabatase. Please check that the configuration file exists.");
            return -1;
        }

        PreparedStatement query = dbConnection.prepareStatement("SELECT id FROM user WHERE username=? AND  password=?");
        query.setString(1, login);
        // Password envoyé en SHA1 par l'interface !
        query.setString(2, password);
        resultat = query.executeQuery();

        if (resultat.next()) {
            id = resultat.getInt(1);

            participants.add(new Participant(id));
        }

        if (resultat.next()) {
            id = -1; // Erreur plusieurs même login/mdp
        }

        return id;
    }

    public JSONObject actualisation(Message requete) {
        /* Récupérer user*/
        int idUser = requete.getData().getInt("id_utilisateur");
        Participant p = getParticipantByID(idUser);

        if(p != null) {
            /* Récupérer le challenge*/
            Salle s = getRoomByID(idUser);

            if(s != null) {
                /* Envoyer l'état de jeu*/

                return s.getChallenge().toJson();
//                p.getPrintWriter().print(s.getChallenge().toJson());
            }

            else {
                Logger.getLogger(MessageManager.class.getName()).log(Level.WARNING, "User is not in a game room");
            }
        }

        else {
            Logger.getLogger(MessageManager.class.getName()).log(Level.WARNING, "User is not currently playing");
        }

        return null;
    }

    public void choisirChallenge(Message requete) {
        int idUser = requete.getData().getInt("id_utilisateur");
        Salle s = findAvailableRoom();

        if (s == null) {
            s = new Salle(new Connect4());
            rooms.add(s);
        }

        s.addJoueur(idUser);

        if (s.getNbJoueursConnectes() == 2) {
            int[] joueurs = s.getJoueurs();
            Participant p;
            JSONObject json = s.getChallenge().toJson();
            json.put("id_player", joueurs[json.getInt("id_player") - 1]);

            for (int i = 0; i < 2; ++i) {
                p = getParticipantByID(joueurs[i]);

                if(p == null) {
                    Logger.getLogger(MessageManager.class.getName()).log(Level.WARNING, "One of the players was not found");
                    return;
                }

//                p.getPrintWriter().println(s.getChallenge().toJson());
            }
        }
    }

    public JSONObject jouerTour (Message requete){
        int idUser = requete.getData().getInt("id_utilisateur");
        Participant p = getParticipantByID(idUser);
        Salle s = getRoomByID(idUser);

        if(p == null) {
            System.out.println("Erreur du participant");
            return null;
        }

        if (s == null)
            s = new Salle(new Connect4());

        if(s.getChallenge().jouerCoup(requete.getData())) {
            if(s.getChallenge().toJson().getInt("id_player") == 1) {
                s.getChallenge().fromJson(s.getChallenge().toJson().put("id_player",2));
            }

            else {
                s.getChallenge().fromJson(s.getChallenge().toJson().put("id_player",1));
            }
        }

        return s.getChallenge().toJson();
    }

    public Participant getParticipantByID(int id) {
        for (Participant p : participants) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public Salle getRoomByID(int id) {
        for (Salle s : rooms) {
            for (int i : s.getJoueurs()) {
                if (i == id) {
                    return s;
                }
            }
        }

        return null;
    }

    public Salle findAvailableRoom() {
        Salle s = null;

        for (Salle tmp : rooms) {
            if (tmp != null && tmp.getNbJoueursConnectes() < 2) {
                s = tmp;
            }
        }
        
        return s;
    }
}
