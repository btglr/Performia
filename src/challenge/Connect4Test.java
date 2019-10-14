package challenge;
import java.util.Scanner;
import org.json.JSONObject;



public class Connect4Test{

	private int[] grille;
	private String nom;
	private boolean fini;
	private int tour; //J1 ou J2

	public Connect4Test() {
		this.nom = "Connect 4";
		this.fini = false;
		this.tour = 1;
		this.grille = new int[6*7];
	}
	
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		json.put("fini", this.fini);
		json.put("tour", this.tour);
		json.put("grille", this.grille);
		
		return json;
	}
	
	public Object fromJson(JSONObject json) {
		return json.getInt("column");
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
		
		boolean complet = true;
		for(int i = 0; i < 7; i++) {
			if(gr[i][0]==0) complet = false;
		}
		if(complet) fini = true;
	}

	public void recevoirEtJouerCoup() {
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

	public void finPartie() {
		if(this.tour == 1)System.out.println("Le joueur 2 a gagné.");
		else System.out.println("Le joueur 1 a gagné.");
	}

	public void lancer() {
		JSONObject json_grille_and_tour = new JSONObject();
		int column_select;
		while(!estFini()) {
			System.out.println("Tour du J"+tour);
			//JSON A ENVOYE AU JOUEUR
			json_grille_and_tour = this.toJson();
			this.afficherGrille(); // A remplacer par l'envoi de la nouvelle grille au joueur et à l'ia
			
			//JSON RECU PAR LE JOUEUR/IA
			//column_select = this.fromJson(  );
			this.recevoirEtJouerCoup();
			System.out.println();
		}
		this.afficherGrille();// A remplacer par l'envoi de la nouvelle grille au joueur et à l'ia
		this.finPartie();
	}

	public static void main (String[] args){
		Connect4Test c = new Connect4Test();
		c.lancer();
	}

}
