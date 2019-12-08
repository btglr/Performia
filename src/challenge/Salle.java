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
    // Nombre de joueurs présents dans la salle
    private int nbJoueursConnectes;
    // Nombre de joueurs maximum
    private int nbJoueursMax;
    // Temps de la partie
    private long tempsPartie;

    public Salle() {
        this(null);
    }

    public Salle(Challenge challenge) {
        this(challenge, 2);
    }

    public Salle(Challenge challenge, int nbJoueursMax) {
        this.nbJoueursMax = Math.max(nbJoueursMax, 2);
        this.challenge = challenge;

        this.joueurs = new int[this.nbJoueursMax];

        for (int i = 0; i < this.nbJoueursMax; ++i) {
            this.joueurs[i] = -1;
        }

        this.nbJoueursConnectes = 0;
        this.id = count.incrementAndGet();
    }

    public void addJoueur(int joueurId) {
        if (this.nbJoueursConnectes < this.nbJoueursMax) {
            this.joueurs[this.nbJoueursConnectes++] = joueurId;
        }
    }

    public void demarrerJeu() {
        if (this.nbJoueursConnectes == this.nbJoueursMax) {
            // Démarrage du jeu
        }
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public int[] getJoueurs() {
        return joueurs;
    }

    public int getNbJoueursConnectes() {
        return nbJoueursConnectes;
    }

    public boolean estPleine() {
        return this.nbJoueursConnectes == this.nbJoueursMax;
    }

    public void debutPartie() {
        tempsPartie = System.currentTimeMillis();
    }

    public long finPartie() {
        // On divise par 1000 pour avoir le temps en seconde
        return (int)((System.currentTimeMillis() - tempsPartie)/1000);
    }
}
