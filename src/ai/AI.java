package ai;/* Dreugui && Laurie
 * //========================================<>=================================================//
 * //========================================<>=================================================//
 * //===									 AI												====//
 * //========================================<>=================================================//
 * //========================================<>=================================================//
 * 	IA puissance 4 V1.2 Aleatoire
 * */

import static java.lang.Thread.sleep;

import data.Config;
import org.json.JSONArray;
import org.json.JSONObject;
import requete.Message;

public class AI {
	private static int randomlyChoose(int grille[]) {
		int res;

		res = (int)(Math.random() * 7);
		while (grille[res] != 0) {
			res = (int)(Math.random() * 7);
			System.out.println("Res = " + res);
		}

		return res;
	}

	public static void main(String[] args) {
		// Login + password JSON
		Config ia = new Config("config/ia.json");
		TCPClient tcpClient = new TCPClient();
		int choice;
		int[] grid;
        boolean ongoingChallenge = true;
        int challengeID = 1;
        JSONObject info;
        JSONObject result;

        tcpClient.connect(ia.getString("login"), ia.getString("password"));

        /*Demande de chalege au serveur et recuperation de l'ID*/
        JSONObject initialGameState = tcpClient.chooseChallenge(challengeID);
        info = initialGameState.getJSONObject("data");

		while (ongoingChallenge) {
            JSONObject response = new JSONObject();

            ongoingChallenge = !info.getBoolean("fini");

            JSONArray gridArray = info.getJSONArray("grille");
            grid = new int[gridArray.length()];

            for (int i = 0; i < gridArray.length(); ++i) {
                grid[i] = gridArray.getInt(i);
            }

            if (tcpClient.getUserId() == info.getInt("id_player") && ongoingChallenge) {
                // Choose randomly (smart AI)
                choice = randomlyChoose(grid);
                response.put("user_id", tcpClient.getUserId());
                response.put("colonne", choice);

                // Play turn
                info = tcpClient.playTurn(response).getJSONObject("data");
            }

            else {
                // Ask for the state of the challenge to check if the other player has made a move
                info = tcpClient.getChallengeState().getJSONObject("data");
            }

            try {
                // Attente entre 1 et 5s
                sleep((long) (1000 + (Math.random() * 4000)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

		tcpClient.closeSocket();
	}
}