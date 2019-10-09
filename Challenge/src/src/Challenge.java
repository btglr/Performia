package src;

import org.json.JSONObject;

abstract class Challenge {

	protected String nom;
	protected boolean fini;
	protected int tour; //J1 ou J2
	
	public Challenge(String nom) {
		this.nom = nom;
		this.fini = false;
		this.tour = 1;
	}
	
	/**
	 * Retourne true si la partie est finie
	 */
	abstract boolean estFini();
	
	/**
	 * Attend l'action du joueur
	 */
	abstract void attendreCoup();
	
	/**
	 * Envoie les données de la partie (etat du plateau ou score par ex) au joueur et à l'ia
	 */
	abstract void envoyerDonnees();
	
	/**
	 * Affiche le resultat de la partie
	 */
	abstract void finPartie();
	
	/**
	 * Lit le fichier json
	 */
	abstract JSONObject fromJson(String json);
	
	/**
	 * Ecrit en json
	 */
	abstract String toJson(JSONObject json);
	
	/**
	 * Lance la partie
	 */
	abstract void lancer();
}
