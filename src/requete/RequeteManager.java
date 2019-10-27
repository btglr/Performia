/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requete;

import challenge.Connect4;
import challenge.Salle;
import java.util.logging.Level;
import java.util.logging.Logger;
import data.DBManager;
import challenge.Participant;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONException;
import org.json.JSONObject;
import tcp.Performia;
import utils.MessageCode;

import static utils.MessageCode.getRequest;

/**
 *
 * @author Noizet Mathieu
 */
public class RequeteManager implements Runnable {
    private RequestQueue requestQueue;
    private ResponseQueue responseQueue;
    private static final Object lock = new Object();
    private static volatile RequeteManager instance = null;

    private RequeteManager(RequestQueue requestQueue, ResponseQueue responseQueue) {
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
    }

    public static RequeteManager getInstance() {
        RequeteManager r = instance;

        if (r == null) {
            synchronized (lock) {
                r = instance;
                if (r == null) {
                    r = new RequeteManager(RequestQueue.getInstance(), ResponseQueue.getInstance());
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
                synchronized (lock) {
                    System.out.println("Je m'endors car je n'ai plus de travail");
                    try {
                        lock.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RequeteManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            // Attente terminée, on a été notify
            req = requestQueue.getMessage();
            MessageCode code = getRequest(req.getCode());

            switch (code) {
                // Authentification
                case CONNECTION: {
                    try {
                        int id;
                        id = connexion(req);

                    } catch (SQLException ex) {
                        Logger.getLogger(RequeteManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;

                // Choix d'un challenge
                case CHOOSE_CHALLENGE:
                    System.out.println("Choix de challenge");
                    break;

                // Jouer un tour
                case PLAY_TURN:
                    jouerTour(req);
                    break;

                // Demande d'actualisation de l'état du jeu
                case GET_GAME_STATE:
                    actualisation(req);
                    break;
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
        } catch (JSONException ex) {
            Logger.getLogger(RequeteManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        DBManager db = new DBManager();
        PreparedStatement query = db.getConnection().prepareStatement("SELECT user_id FROM user WHERE user_name= ? AND  user_pass =?");
        query.setString(1, login);
        query.setString(2, password);
        resultat = query.executeQuery();

        if (resultat.next()) {
            id = resultat.getInt(1);
        }
        if (resultat.next()) {
            id = -1; // Erreur plusieurs même login/mdp
        }
        return id;
    }

    public void actualisation(Message requete) {
        /* Récupérer user*/
        int idUser = requete.getData().getInt("id");
        Participant p = Performia.getParticipantByID(idUser);
        if(p != null) {
            /* Récupérer le challenge*/
            Salle s = Performia.getSalleByID(idUser);
            if(s != null) {
                /* Envoyer l'état de jeu*/
                p.getPrintWriter().print(s.getChallenge().toJson());
            }
            else {
                System.out.println("Erreur.");
            }
        }
        else {
            System.out.println("Erreur.");
        }
    }

    public void choisirChallenge(Message requete) {
        int idUser = requete.getData().getInt("id");
        Salle s = Performia.nonPleine();
        if (s == null)
            s = new Salle(new Connect4());
        s.addJoueur(idUser);
        if (s.getNbJoueursConnectes() == 2) {
            int[] joueurs = s.getJoueurs();
            Participant p;
            JSONObject json = s.getChallenge().toJson();
            json.put("id_player",joueurs[json.getInt("id_player")-1]);
            for (int i = 0; i < 2; ++i) {
                p = Performia.getParticipantByID(joueurs[i]);
                if(p == null) {
                    System.out.println("Erreur du participant");
                    return;
                }
                p.getPrintWriter().println(s.getChallenge().toJson());//
            }
        }
    }

    public void jouerTour (Message requete){
        int idUser = requete.getData().getInt("id");
        Participant p = Performia.getParticipantByID(idUser);
        Salle s = Performia.getSalleByID(idUser);
        if(p == null) {
            System.out.println("Erreur du participant");
            return;
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
        p.getPrintWriter().print(s.getChallenge().toJson());
    }
}
