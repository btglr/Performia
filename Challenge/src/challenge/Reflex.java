package challenge;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

public class Reflex extends Challenge {
	
	private boolean[] grille;
	private int[] score;
	private int tour = 0;
	
	public Reflex(Participant[] participants) {
		super("Reflex");
		this.players = new Participant[4];
		
		this.players[0] = participants[0];
		this.players[1] = participants[1];
		this.players[2] = participants[2];
		this.players[3] = participants[3];
		

		this.grille = new boolean[5*5];
	}
	
	public Reflex(boolean[] grille, Participant[] participants) {
		super("Reflex");
		this.players = new Participant[4];
		
		this.players[0] = participants[0];
		this.players[1] = participants[1];
		this.players[2] = participants[2];
		this.players[3] = participants[3];
		

		this.grille = grille;
	}

	@Override
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		json.put("id_player", this.id_player);

		JSONArray arrayPlayers = new JSONArray();
		arrayPlayers.put(this.players[0].toJson());
		arrayPlayers.put(this.players[1].toJson());
		arrayPlayers.put(this.players[2].toJson());
		arrayPlayers.put(this.players[3].toJson());

		json.put("players", arrayPlayers);
		json.put("grille", this.grille);

		return json;
	}

	@Override
	public Object fromJson(JSONObject json) {
		boolean[] grille = (boolean[]) json.get("grille");
		JSONArray arrayPlayers = json.getJSONArray("players");
		Participant[] p = new Participant[4];
		for(int i =0; i <p.length; i++){
			p[i] = Participant.fromJson(arrayPlayers.getJSONObject(i));
		}
		
		return new Reflex(grille, p);
	}

	@Override
	public boolean estFini() {
		if(this.tour != 20){
			return false;
		}else{
			return true;
		}
	}
	
	void updateScore(int indiceParticipant, int scoreAdd) {
		this.score[indiceParticipant]+= scoreAdd;
	}

	@Override
	public boolean jouerCoup(JSONObject json) {
		int case_select = json.getInt("case");
		int id_p= json.getInt("id_player");
		int pos_player =-1;
		for(int i =0; i < 4; i++) {
			if(this.players[i].getId() == id_p){
				pos_player = i;
			}
		}
		if(pos_player != -1){
			if(this.grille[case_select] == true){
				this.grille[case_select] = false;
				this.updateScore(id_player, 5);
			}else {
				this.updateScore(id_player, -1);
			}
			return true;
		}
		
		return false;
	}

	@Override
	public Participant prochainJoueur() {
//		Participant prochain;
		
//		for(int i =0; i < 4; i++){
//			if (this.id_player == this.players[i].getId()) {
//				prochain = this.players[i+1];
//				return prochain;
//			}
//		}
//
//		prochain = this.players[0];
//		
//		return prochain;
		return null;
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
	
	void setTour(int newTour) {
		this.tour = newTour;
	}
	
	int getTour() {
		return this.tour;
	}
	
	void display_grid() {
		for(int i =0; i < 5*5 ; i++){
			if(i%5 == 0) {
				System.out.println("");
			}
			System.out.print(this.grille[i] +" ");
			
		}
	}
	
	void setRandomTrue(){
		Random r = new Random();
        int indice = r.nextInt(20);
        //Sleep entre 3 et 10 secondes (0:7 +3 => [3;10]) 
        int timeSleep = r.nextInt(8)+3;
        try {
			Thread.sleep(timeSleep*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.grille[indice] =true;
        
	}
	
	
	
	
	
	public static void main(String[] args) {
		Participant[] p = new Participant[4];
		p[0] = new Participant(1);
		p[1] = new Participant(2);
		p[2] = new Participant(3);
		p[3] = new Participant(4);
		
		Reflex r = new Reflex(p);
		
		
		while(r.estFini() == false){
			
			System.out.println("\n\nround : "+ (r.getTour()+1));
			
			r.display_grid();
			r.setRandomTrue();
			r.display_grid();
			
			//WAIT CLICK
			//PARALELLE JOUER_COUP()
			
			r.setTour(r.getTour()+1);
		}
		
	}

}
