package challenge;

import org.json.JSONObject;

public abstract class Challenge {
	protected String nom;
	protected boolean fini;
	protected int id_player; //J1 ou J2

	public Challenge(String nom) {
		this.nom = nom;
		this.fini = false;
		this.id_player = 1;
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
}
