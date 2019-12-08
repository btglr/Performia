package challenge;
import org.json.JSONArray;
import org.json.JSONObject;


public class Connect4 extends Challenge {

	private int[] grille;

	public Connect4(Participant p1) {
		this(p1, null);
		this.id_player = p1.getId();
	}

	public Connect4(Participant p1, Participant p2) {
		super(1, "Connect 4");

		this.players = new Participant[2];
		this.players[0] = p1;
		this.players[1] = p2;

		this.grille = new int[6 * 7];
	}

	public Connect4(int[] grille, Participant p1, Participant p2) {
		super(1, "Connect 4");

		this.players = new Participant[2];
		this.players[0] = p1;
		this.players[1] = p2;

		this.grille = grille;
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		json.put("fini", this.fini);
		json.put("id_player", this.id_player);

		JSONArray arrayPlayers = new JSONArray();
		arrayPlayers.put(this.players[0].toJson());
		arrayPlayers.put(this.players[1].toJson());

		json.put("players", arrayPlayers);
		json.put("grille", this.grille);

		return json;
	}

	public Object fromJson(JSONObject json) {
		int[] grille = (int[]) json.get("grille");
		JSONArray arrayPlayers = json.getJSONArray("players");
		Participant p1 = Participant.fromJson(arrayPlayers.getJSONObject(0));
		Participant p2 = Participant.fromJson(arrayPlayers.getJSONObject(1));

		return new Connect4(grille, p1, p2);
	}

	public boolean estFini() {
		return fini;
	}

	public void majFini(int pos) {
		int[][] gr = new int[7][6];
		int cpt = 0;
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				gr[j][i] = grille[cpt];
				cpt++;
			}
		}

		int x = pos % 7;
		int y = pos / 7;

		int nb = 1;

		boolean c = true;

		//droite
		while (c) {
			if (x < 6 && gr[x + 1][y] == id_player) {
				nb++;
				x++;
			}
			else {
				c = false;
			}
		}
		//gauche
		c = true;
		x = pos % 7;
		while (c) {
			if (x > 0 && gr[x - 1][y] == id_player) {
				nb++;
				x--;
			}
			else {
				c = false;
			}
		}
		if (nb >= 4) fini = true;

		nb = 1;
		if (!fini) {
			//haut
			c = true;
			while (c) {
				if (y > 0 && gr[x][y - 1] == id_player) {
					nb++;
					y--;
				}
				else {
					c = false;
				}
			}
			//bas
			c = true;
			y = pos / 7;
			while (c) {
				if (y < 5 && gr[x][y + 1] == id_player) {
					nb++;
					y++;
				}
				else {
					c = false;
				}
			}
			if (nb >= 4) fini = true;
		}

		nb = 1;
		if (!fini) {
			y = pos / 7;
			c = true;
			//bas droite
			while (c) {
				if (y < 5 && x < 6 && gr[x + 1][y + 1] == id_player) {
					nb++;
					x++;
					y++;
				}
				else {
					c = false;
				}
			}

			x = pos % 7;
			y = pos / 7;
			c = true;
			//haut gauche
			while (c) {
				if (y > 0 && x > 0 && gr[x - 1][y - 1] == id_player) {
					nb++;
					x--;
					y--;
				}
				else {
					c = false;
				}
			}
			if (nb >= 4) fini = true;

			nb = 1;
			if (!fini) {
				y = pos / 7;
				c = true;
				//bas gauche
				while (c) {
					if (y < 5 && x > 0 && gr[x - 1][y + 1] == id_player) {
						nb++;
						x--;
						y++;
					}
					else {
						c = false;
					}
				}

				x = pos % 7;
				y = pos / 7;
				c = true;
				//haut droite
				while (c) {
					if (y > 0 && x < 6 && gr[x + 1][y - 1] == id_player) {
						nb++;
						x++;
						y--;
					}
					else {
						c = false;
					}
				}
				if (nb >= 4) fini = true;
			}
		}

		boolean complet = true;
		for (int i = 0; i < 7; i++) {
			if (gr[i][0] == 0) complet = false;
		}
		if (complet) fini = true;
		if(fini) {
			if(!complet) {
				if (this.id_player == this.players[0].getId()) this.winner = this.players[0].getId();
				else this.winner = this.players[1].getId();
			}

		}
	}

	public boolean jouerCoup(JSONObject colonne) {
		int col = colonne.getInt("colonne");
		boolean ok = false;

		if (col >= 0 && col <= 6 && grille[col] == 0) {
			while (col < 35 && grille[col + 7] == 0) col += 7;
			grille[col] = id_player;
			majFini(col);
			if (this.id_player == this.players[0].getId()) id_player = this.players[1].getId();
			else id_player = this.players[0].getId();
			ok = true;
		}
		return ok;
	}

	@Override
	public Participant prochainJoueur() {
		Participant prochain;

		if (this.id_player == this.players[0].getId()) {
			prochain = this.players[1];
		}

		else {
			prochain = this.players[0];
		}

		return prochain;
	}

	@Override
	public int getCurrentPlayerId() {
		return id_player;
	}

	@Override
	public boolean addPlayer(Participant p) {
		int i;

		for (i = 0; i < this.players.length; ++i) {
			this.players[i] = (this.players[i] == null) ? p : this.players[i];
		}

		return (i != this.players.length);
	}
}
