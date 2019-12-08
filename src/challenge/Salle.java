package challenge;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Salle {
    private static final Logger logger = Logger.getLogger(Salle.class.getName());

    // Compteur d'instances (atomique pour le multi-thread)
    private static final AtomicInteger count = new AtomicInteger(0);
    // ID de la salle
    private final int id;
    // Le challenge en cours dans la salle (polymorphisme)
    private Challenge challenge;
    // Les IDs des joueurs présents dans la salle
    private List<Integer> joueurs;
    // Nombre de joueurs présents dans la salle
    private final AtomicInteger nbJoueursConnectes = new AtomicInteger(0);
    // Nombre de joueurs maximum
    private int nbJoueursMax;
    // Indique si la salle est fermée (partie terminée)
    private boolean fermee;
    // Temps de la partie
    private long tempsPartie;
    private boolean save;

    /*public Salle() {
        this(null);
    }

    public Salle(Challenge challenge) {
        this(challenge, 2);
    }*/

    public Salle(Challenge challenge, int nbJoueursMax) {
        this.nbJoueursMax = Math.min(nbJoueursMax, 4);
        this.challenge = challenge;

        this.joueurs = new ArrayList<>(this.nbJoueursMax);

        for (int i = 0; i < this.nbJoueursMax; ++i) {
            this.joueurs.add(-1);
        }

        this.id = count.getAndIncrement();
        this.fermee = false;
        this.save = false;
    }

    public void addJoueur(int joueurId) {
        if (nbJoueursConnectes.get() < this.nbJoueursMax) {
            this.joueurs.add(joueurId);
            nbJoueursConnectes.incrementAndGet();
        }
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public List<Integer> getJoueurs() {
        return joueurs;
    }

    public int getNbJoueursConnectes() {
        return nbJoueursConnectes.get();
    }

    public boolean estPleine() {
        logger.info("Number of connected players: " + nbJoueursConnectes.get() + "/" + this.nbJoueursMax);

        return nbJoueursConnectes.get() == this.nbJoueursMax;
    }

    public boolean estFermee() {
        return fermee;
    }

    public void fermer() {
        this.fermee = true;
    }

    public int getId() {
        return id;
    }

    public void debutPartie() {
        tempsPartie = System.currentTimeMillis();
    }

    public long finPartie() {
        // On divise par 1000 pour avoir le temps en seconde
        return (int)((System.currentTimeMillis() - tempsPartie)/1000);
    }

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }
}
