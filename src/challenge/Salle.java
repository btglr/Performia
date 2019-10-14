package challenge;

import java.util.concurrent.atomic.AtomicInteger;

public class Salle {
    // Compteur d'instances (atomique pour le multi-thread)
    private static final AtomicInteger count = new AtomicInteger(0);
    // ID de la salle
    private final int id;
    // Le challenge en cours dans la salle (polymorphisme)
    private Challenge challenge;
    // Les IDs des joueurs présents dans la salle
    private int[] joueurs;
    // Nombre de joueurs présents dans la salle (1 ou 2)
    private int nbJoueursConnectes;

    public Salle() {
        this(null);
    }

    public Salle(Challenge challenge) {
        this.challenge = challenge;
        this.joueurs = new int[] {-1, -1};
        this.nbJoueursConnectes = 0;
        this.id = count.incrementAndGet();
    }

    public void addJoueur(int joueurId) {
        if (this.nbJoueursConnectes < 2) {
            this.joueurs[this.nbJoueursConnectes++] = joueurId;
        }
    }

    public void demarrerJeu() {
        if (this.nbJoueursConnectes == 2) {
            // Démarrage du jeu
        }
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public int[] getJoueurs() {
        return joueurs;
    }
}
