package challenge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Reflex extends Challenge implements Runnable{
	
	private boolean[] grille;
	private int[] score;
	private int tour = 0;
	private int change_round = 0;
	
	public Reflex(Participant[] participants) {
		super("Reflex");
		this.players = new Participant[4];
		
		this.players[0] = participants[0];
		this.players[1] = participants[1];
		this.players[2] = participants[2];
		this.players[3] = participants[3];
		

		this.grille = new boolean[5*5];
		
		this.score = new int[this.players.length];
	}
	
	public Reflex(Participant p) {
				
		super("Reflex");
		this.players = new Participant[4];
		
		this.players[0] = p;
		this.players[1] = null;
		this.players[2] = null;
		this.players[3] = null;
		

		this.grille = new boolean[5*5];
		
		this.score = new int[this.players.length];
		
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
		json.put("score", this.score);
		json.put("round", this.tour);

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
		boolean fin = false;
		if(this.tour >= 20) {
			fin = true;
			this.setWinners();
		}
		
		return fin;
	}
	
	void updateScore(int indiceParticipant, int scoreAdd) {
		this.score[indiceParticipant]+= scoreAdd;
	}
	
	void setWinners() {
		int indMax = 0;
		boolean egalite = false;
		for(int i = 0; i < this.players.length; i++) {
			if(this.score[i] > this.score[indMax]) {
				indMax = i;
			}
			if(i != 0 && this.score[i] == this.score[indMax]) {
				egalite = true;
			}
		}
		if(!egalite) this.winner = this.players[indMax].getId(); 
	}

	@Override
	public boolean jouerCoup(JSONObject json){
		int case_select = json.getInt("case");
		int id_p= json.getInt("id_player");
		int pos_player =-1;
		for(int i =0; i < 4; i++) {
			if(this.players[i].getId() == id_p){
				pos_player = i;
			}
		}
		if(pos_player != -1){
//			System.out.println(pos_player);
			if(this.grille[case_select] == true){
				this.change_round = 1;
				this.grille[case_select] = false;
				this.updateScore(pos_player, 5);
				this.setTour(this.getTour()+1);
			}else {
				this.updateScore(pos_player, -1);
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
        this.display_grid();
        
	}
	
	void set_change_round(int val) {
		this.change_round = val;
	}
	
	void display_score() {
		System.out.println("\n\n1 : "+this.score[0]);
		System.out.println("2 : "+this.score[1]);
		System.out.println("3 : "+this.score[2]);
		System.out.println("4 : "+this.score[3]);
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
			new Thread(r).start();
			Scanner myObj = new Scanner(System.in);  // Create a Scanner object
		    while(r.change_round == 0){
		    	System.out.println("\n\n Enter the good number");
			    int caseSelect = (int)Integer.parseInt(myObj.nextLine());  // Read user input
			    Random rand = new Random();
			    
			    JSONObject j= new JSONObject();
			    j.put("id_player", rand.nextInt(4)+1);
			    j.put("case", caseSelect);
			    r.jouerCoup(j);
		    }
		    
		    r.set_change_round(0);
		    r.display_score();
			
			
			
			
		}
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		setRandomTrue();
		
	}

}
