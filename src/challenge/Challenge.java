package challenge;

import java.util.ArrayList;

import org.json.JSONObject;

public abstract class Challenge {
	protected String nom;
	protected boolean fini;
	protected Participant[] players;
	protected int id_player; //J1 ou J2
	protected ArrayList<Participant> winners;

	public Challenge(String nom) {
		this.nom = nom;
		this.fini = false;
		this.winners = new ArrayList<Participant>();
	}

	/**
	 * Converti les données du challenge à envoyer en Json
	 */
	public abstract JSONObject toJson();

	/**
	 * Transforme les données reçu des participants en Objet
	 */
	public abstract Object fromJson(JSONObject json);

	/**
	 * Retourne true si la partie est finie
	 */
	public abstract boolean estFini();

	/**
	 * Attend l'action du joueur
	 */
	public abstract boolean jouerCoup(JSONObject colonne);

	/**
	 * Détermine le prochain joueur
	 */
	public abstract Participant prochainJoueur();

	/**
	 * Retourne l'id du joueur courant
	 * @return l'id du joueur courant
	 */
	public abstract int getCurrentPlayerId();

	/**
	 * Ajoute un participant/joueur au challenge
	 * @param p le participant
	 * @return vrai ou faux si un joueur a bien été ajouté
	 */
	public abstract boolean addPlayer(Participant p);

	public ArrayList<Participant> getWinners() {
		return winners;
	}
}
