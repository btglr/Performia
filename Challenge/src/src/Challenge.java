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
	 * Converti les donn�es du challenge � envoyer en Json
	 */
	abstract JSONObject toJson();
	
	/**
	 * Transforme les donn�es re�u des participants en Objet
	 */
	abstract Object fromJson(JSONObject json);
	
	/**
	 * Retourne true si la partie est finie
	 */
	abstract boolean estFini();
	
	/**
	 * Attend l'action du joueur
	 */
	abstract boolean jouerCoup(JSONObject colonne);
}