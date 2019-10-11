/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requete;

import java.util.logging.Level;
import java.util.logging.Logger;

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
                /* Identification */
                case 1:
                    break;
                /* Demande d'actualisation */
                case 2:
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
    
    public void authentification(Requete requete){
    
    }
    
    public void actualisation(Requete requete){
    }
    public void jouerTour(Requete requete){
    
    }
}
