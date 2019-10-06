package src;

abstract class Challenge {

	protected String nom;
	protected boolean fini;
	protected int tour; //J1 ou J2
	
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
	 * Lance la partie
	 */
	abstract void lancer();
}
