/* Dreugui
 * //========================================<>=================================================//
 * //========================================<>=================================================//
 * //===									AI_CLICK										====//
 * //========================================<>=================================================//
 * //========================================<>=================================================//
 * 	IA Click V1.0.1
 * //===========================================================================================//
 * //										IMPORT												//
 * //===========================================================================================//
 * */
	/*================================<DEFINE>================================*/
package ai;

	/*================================<GENERAL>================================*/
import static java.lang.Thread.sleep;
import data.Config;
import org.json.JSONObject;

	/*================================<PERSONEL>================================*/

	/*================================<DECLARATION>================================*/
public class AI{
	
/*
 * //===========================================================================================//
 * //										MAIN												//
 * //===========================================================================================//
 * */
	/*================================<FONCTION>================================*/
	
	/*================================<MAIN>================================*/

	public static void main(String[] args){
		
		/*--------------------------------<DECLARATION>--------------------------------*/
		// lecteur JSON ID MPD
		/*Conexion au serveur id+mdp*/
		Config ia = new Config("config/ia.json");
		TCPClient tcpClient = new TCPClient();
        int challengeID = 2;
        JSONObject info;
        tcpClient.connect(ia.getString("login"), ia.getString("password"));
		
		/*--------------------------------<INITIALISATION>--------------------------------*/
		/*Demande de chalege au serveur et recuperation de l'ID*/
		JSONObject initialGameState = tcpClient.chooseChallenge(challengeID);
        info = initialGameState.getJSONObject("data");
		
		/*--------------------------------<PARTIE>--------------------------------*/
		while (!info.getJSONObject("data").getBoolean("fini"))
		{
			// reception de la case 
            info = tcpClient.receiveTurn();
			
			// Lancer Thread
			AI_Thread thrd = new AI_Thread(info.getInt("case"),tcpClient);
			info = tcpClient.receiveTurn();
			
        }
		
		/*--------------------------------<FIN>--------------------------------*/
		tcpClient.closeSocket();
	}
}








