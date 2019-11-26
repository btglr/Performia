/* Dreugui && Laurie
 * //========================================<>=================================================//
 * //========================================<>=================================================//
 * //===								AI_CONNECT4											====//
 * //========================================<>=================================================//
 * //========================================<>=================================================//
 * 	IA connect 4 V1.2 Aleatoire
 * //===========================================================================================//
 * //										IMPORT												//
 * //===========================================================================================//
 * */
	/*================================<DEFINE>================================*/
package ai;

	/*================================<GENERAL>================================*/
import org.json.simple.JSONObject;
import static java.lang.Thread.sleep;
import data.Config;
import org.json.JSONArray;
import org.json.JSONObject;
	/*================================<PERSONEL>================================*/
import TCPClient.*;
import requete.Message;

	/*================================<DECLARATION>================================*/
public class AI{
	
/*
 * //===========================================================================================//
 * //										MAIN												//
 * //===========================================================================================//
 * */
	/*================================<FONCTION>================================*/
	private static int randomlyChoose(JSONArray grille) {
		int res;

		res = (int)(Math.random() * 7);
		while (grille.getInt(res) != 0) {
			res = (int)(Math.random() * 7);
			System.out.println("Res = " + res);
		}

		return res;
	}
	
	/*================================<MAIN>================================*/

	public static void main(String[] args){
		
		/*--------------------------------<DECLARATION>--------------------------------*/
		// lecteur JSON ID MPD
		/*Conexion au serveur id+mdp*/
		Config ia = new Config("config/ia.json");
		TCPClient tcpClient = new TCPClient();
		int choice;
        boolean ongoingChallenge = true;
        int challengeID = 1;
        JSONObject info;
        JSONObject result;
        tcpClient.connect(ia.getString("login"), ia.getString("password"));
		
		/*--------------------------------<INITIALISATION>--------------------------------*/
		/*Demande de chalege au serveur et recuperation de l'ID*/
		JSONObject initialGameState = tcpClient.chooseChallenge(challengeID);
        info = initialGameState.getJSONObject("data");
		
		/*--------------------------------<PARTIE>--------------------------------*/
		while (ongoingChallenge) {
            JSONObject response = new JSONObject();

            ongoingChallenge = !info.getBoolean("fini");

            JSONArray gridArray = info.getJSONArray("grille");

            if (tcpClient.getUserId() == info.getInt("id_player") && ongoingChallenge) {
                // Choose randomly (smart AI)
                choice = randomlyChoose(gridArray);
                response.put("id_utilisateur", tcpClient.getUserId());
                response.put("colonne", choice);
				try {
				// Attente entre 1 et 5s
					sleep((long) (1000 + (Math.random() * 4000)));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
                // Play turn
                info = tcpClient.playTurn(response).getJSONObject("data");
            }

            else {
                // Ask for the state of the challenge to check if the other player has made a move
                info = tcpClient.getChallengeState().getJSONObject("data");
            }
        }
		
		/*--------------------------------<FIN>--------------------------------*/
		tcpClient.closeSocket();
	}
}








