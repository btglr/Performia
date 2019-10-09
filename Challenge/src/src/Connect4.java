package src;

import java.util.Scanner;

import org.json.JSONObject;

public class Connect4 extends Challenge{

	private int[] grille;

	public Connect4() {
		super("Connect 4");
		this.grille = new int[6*7];
	}

	boolean estFini() {
		return fini;
	}

	public void afficherGrille() {
		int cpt = 0;
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 7; j++) {
				System.out.print(grille[cpt]+" ");
				cpt++;
			}
			System.out.println("");
		}
	}

	public void majFini(int pos) {
		int[][] gr = new int[7][6];
		int cpt = 0;
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 7; j++) {
				gr[j][i] = grille[cpt];
				cpt++;
			}
		}

		int x = pos%7;
		int y = pos/7;

		int nb = 1;

		boolean c = true;

		//droite
		while(c) {
			if(x<6 && gr[x+1][y] == tour) {
				nb++;
				x++;
			}else {
				c = false;
			}
		}
		//gauche
		c = true;
		x = pos%7;
		while(c) {
			if(x>0 && gr[x-1][y] == tour) {
				nb++;
				x--;
			}else {
				c = false;
			}
		}
		if(nb >= 4) fini = true;

		nb=1;
		if(!fini) {
			//haut
			c = true;
			while(c) {
				if(y>0 && gr[x][y-1] == tour) {
					nb++;
					y--;
				}else {
					c = false;
				}
			}
			//bas
			c = true;
			y = pos/7;
			while(c) {
				if(y<5 && gr[x][y+1] == tour) {
					nb++;
					y++;
				}else {
					c = false;
				}
			}
			if(nb >= 4) fini = true;
		}

		nb=1;
		if(!fini) {
			y = pos/7;
			c = true;
			//bas droite
			while(c) {
				if(y<5 && x < 6 && gr[x+1][y+1] == tour) {
					nb++;
					x++;
					y++;
				}else {
					c = false;
				}
			}

			x = pos%7;
			y = pos/7;
			c = true;
			//haut gauche
			while(c) {
				if(y>0 && x > 0 && gr[x-1][y-1] == tour) {
					nb++;
					x--;
					y--;
				}else {
					c = false;
				}
			}
			if(nb >= 4) fini = true;

			nb=1;
			if(!fini) {
				y = pos/7;
				c = true;
				//bas gauche
				while(c) {
					if(y<5 && x > 0 && gr[x-1][y+1] == tour) {
						nb++;
						x--;
						y++;
					}else {
						c = false;
					}
				}

				x = pos%7;
				y = pos/7;
				c = true;
				//haut droite
				while(c) {
					if(y>0 && x < 6 && gr[x+1][y-1] == tour) {
						nb++;
						x++;
						y--;
					}else {
						c = false;
					}
				}
				if(nb >= 4) fini = true;
			}
		}
	}

	public void attendreCoup() {
		boolean ok = false;												//
		int tmp = 1;													// A remplacer par la reception d'une action côte joueur ou ia
		Scanner sc = new Scanner(System.in);							//
		System.out.println("Entrez un numéro de colonne entre 0 et 6.");//
		while(!ok) {
			tmp = sc.nextInt();
			if(tmp < 0 || tmp > 6) System.out.println("Valeur invalide."); // traitement à faire du côté joueur et ia
			else if(grille[tmp] != 0)System.out.println("Colonne pleine.");//
			else {
				while(tmp < 35 && grille[tmp+7]==0)tmp+=7;
				grille[tmp]=tour;
				majFini(tmp);
				ok = true;
				if(this.tour==1)tour = 2;
				else tour = 1;
			}
		}
	}
	
	public void envoyerDonnees() {}

	public void finPartie() {
		if(this.tour == 1)System.out.println("Le joueur 2 a gagné.");
		else System.out.println("Le joueur 1 a gagné.");
	}
	
	public JSONObject fromJson(String json) {
		return new JSONObject(json);
	}
	
	public String toJson(JSONObject json) {
		return json.toString();
	}

	public void lancer() {
		while(!estFini()) {
			System.out.println("Tour du J"+tour);
			this.afficherGrille(); // A remplacer par l'envoi de la nouvelle grille au joueur et à l'ia
			this.attendreCoup();
			System.out.println();
		}
		this.finPartie();
	}

	public static void main (String[] args){
		Connect4 c = new Connect4();
		c.lancer();
	}

}
