package challenge;

import org.json.JSONObject;

public abstract class Challenge {
	protected String nom;
	protected boolean fini;
	protected int[] id_players;
	protected int id_player; //J1 ou J2

	public Challenge(String nom, int id1, int id2) {
		this.nom = nom;
		this.fini = false;
		id_players = new int[2];
		id_players[0] = id1;
		id_players[1] = id2;
		this.id_player = id_players[0];
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
