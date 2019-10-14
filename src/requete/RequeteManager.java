/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requete;

import java.util.logging.Level;
import java.util.logging.Logger;
import data.DBManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONException;

/**
 *
 * @author Noizet Mathieu
 */
public class RequeteManager {

    public RequeteManager() {
    }

    public void traitementRequete(FileRequete file) {
        Requete req;
        while (true) {
            /* Attente passive d'une requete */
            while (file.estVide()) {
                synchronized (this) {
                    System.out.println("Je m'endors car je n'ai plus de travail");
                    try {
                        this.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RequeteManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            /* J'ai des requêtes à traité*/
            req = file.getRequete();
            switch (req.getCode()) {
                /* Authentification */

                case 1: {
                    try {
                        int id;
                        id = connexion(req);

                    } catch (SQLException ex) {
                        Logger.getLogger(RequeteManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                /* Demande d'actualisation */
                case 2:
                    actualisation(req);
                    break;
                /* Jouer un tour */
                case 3:
                    break;
                case 5:
                    System.out.println("J'ai une requete de type 5 a traite");
                    break;
            }
        }
    }

    public int connexion(Requete requete) throws SQLException {
        String login = "", mdp = "";
        int id = -1;
        ResultSet resultat;
        try {
            login = requete.getData().getString("login");
            mdp = requete.getData().getString("mdp");
        } catch (JSONException ex) {
            Logger.getLogger(RequeteManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        DBManager db = new DBManager();
        PreparedStatement query = db.getConnection().prepareStatement("SELECT user_id FROM user WHERE user_name= ? AND  user_pass =?");
        query.setString(1, login);
        query.setString(2, mdp);
        resultat = query.executeQuery();
        boolean encore;
        encore = resultat.next();
        if (encore == true) {
            id = resultat.getInt(1);
        }
        if (resultat.next()) {
            id = -1; // Erreur plusieurs même login/mdp
        }
        return id;
    }

    public void actualisation(Requete requete) {
        /* Récupérer user*/
 /* Récupérer le challenge*/
 /* Envoyer l'état de jeu*/
    }

    public void choisirChallenge(Requete requete) {
        /* Récupérer l'id user*/
 /* Crée un challenge avec le nombre de personne nécessaire pour le challenge choisit.*/
 /* Envoyer état initiale*/
        
    }

    public void jouerTour(Requete requete) {

    }
}
