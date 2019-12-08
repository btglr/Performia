package ai;

import static java.lang.Thread.sleep;

import data.Config;
import org.json.JSONArray;
import org.json.JSONObject;
import requete.Message;

public class AIClick {
    public static void main(String[] args) {
        // Login + password JSON
        Config ia = new Config("config/ia.json");
        TCPClient tcpClient = new TCPClient();
        int choice = 0;
        boolean[] grid;
        boolean ongoingChallenge = true;
        // Le challenge est le deuxième
        int challengeID = 2;
        JSONObject info;

        tcpClient.connect(ia.getString("login"), ia.getString("password"));

        /*Demande de challenge au serveur et recuperation de l'ID*/
        JSONObject initialGameState = tcpClient.chooseChallenge(challengeID);
        info = initialGameState.getJSONObject("data");

        while (ongoingChallenge) {
            JSONObject response = new JSONObject();

            ongoingChallenge = !info.getBoolean("fini");
            JSONArray gridArray = info.getJSONArray("grille");
            grid = new boolean[gridArray.length()];
            // On remplit la grille
            for (int i = 0; i < gridArray.length(); ++i) {
                grid[i] = gridArray.getBoolean(i);
                if(grid[i]) {
                    choice = i;
                }
            }

            if (tcpClient.getUserId() == info.getInt("id_player") && ongoingChallenge) {
                // Choose randomly (smart AI)

                response.put("user_id", tcpClient.getUserId());
                response.put("case", choice);

                try {
                    // Attente entre 0,2 et 2s
                    sleep((long) (200 + (Math.random() * 2000)));
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