package challenge;
import org.json.JSONObject;



public class Connect4 extends Challenge{

	private int[] grille;

	public Connect4() {
		super("Connect 4");
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
	
	public boolean jouerCoup(JSONObject colonne) {
		int col = colonne.getInt("colonne");
		boolean ok = false;
		
		if(col >= 0 && col <= 6 && grille[col] == 0) {
			while(col < 35 && grille[col+7]==0)col+=7;
			grille[col]=tour;
			majFini(col);
			if(this.tour==1)tour = 2;
			else tour = 1;
			ok = true;
		}
		return ok;
	}

	public void finPartie() {
		
	}

	public static void main (String[] args){
		Connect4 c = new Connect4();
	}

}
