/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcp;

import java.util.ArrayList;

import challenge.Salle;
import requete.FileRequete;
import requete.RequeteManager;
import challenge.Participant;

/**
 *
 * @author Noizet Mathieu
 */
public class Performia {

    /**
     * @param args the command line arguments
     */
    
    public static ArrayList<Participant> participants = new ArrayList<>();
    public static ArrayList<Salle> salles = new ArrayList<>();
    
    public static void main(String[] args) {
        RequeteManager gestionnaireRequete = new RequeteManager();
        FileRequete listeRequete = new FileRequete(gestionnaireRequete);
        
        Thread serveurTCP = new Thread(new ServeurTCP(25154));
        serveurTCP.start();
        
        /*
            Thread serveurHTTP = new Thread(new ServeurHTTP(port));
            serveurHTTP.start();
        */

        /* Permet de traité les requêtes clients avec une mise en attente passive */
        gestionnaireRequete.traitementRequete(listeRequete);

    }


    
    public Participant getParticipantByID(int id) {
        Participant participant = null;
        for (Participant p : participants) {
            if (p.getId() == id) {
                participant = p;
            }
        }
        return participant;
    }
    
   
}


