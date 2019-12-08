package ai.ai_reflex;

import ai.TCPClient;
import data.Config;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.MessageCode;

import java.net.Socket;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class ThreadReflex implements Runnable {
    private static final Logger logger = Logger.getLogger(ThreadReflex.class.getName());

    private String login;
    private String password;
    private String javaServerHost;
    private int javaServerPort;
    private int account_type;
    private String lastMessage = null;

    public ThreadReflex(Socket socketClient, String login, String password, String javaServerHost, int javaServerPort, int account_type) {
        this.login = login;
        this.password = password;
        this.javaServerHost = javaServerHost;
        this.javaServerPort = javaServerPort;
        this.account_type = account_type;
    }

    @Override
    public void run() {
        // Login + password JSON
        TCPClient tcpClient = new TCPClient(this.javaServerHost, this.javaServerPort);
        int choice = 0;
        boolean[] grid;
        boolean ongoingChallenge = true;
        // Le challenge est le deuxième
        int challengeID = 2;
        JSONObject info;

        tcpClient.connect(this.login, this.password, this.account_type);

        /*Demande de challenge au serveur et recuperation de l'ID*/
        JSONObject initialGameState = tcpClient.chooseChallenge(challengeID);

        if (initialGameState.getInt("code") == MessageCode.ROOM_NOT_FULL.getCode()) {
            // Il manque un joueur, on attend qu'il arrive
            logger.info("Waiting for opponent...");

            boolean canStart;
            do {
                try {
                    // Attente de 0.5s
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                canStart = tcpClient.waitChallengeStart();
            } while (!canStart);

            info = tcpClient.getChallengeState().getJSONObject("data");
        }

        else {
            info = initialGameState.getJSONObject("data");
        }

        logger.info("Challenge can start");

        while (ongoingChallenge) {
            JSONObject response = new JSONObject();

            ongoingChallenge = !info.getBoolean("fini");
            JSONArray gridArray = info.getJSONArray("grille");
            grid = new boolean[gridArray.length()];

            // On remplit la grille
            for (int i = 0; i < gridArray.length(); ++i) {
                grid[i] = gridArray.getBoolean(i);
                if (grid[i]) {
                    choice = i;
                }
            }

            if (tcpClient.getUserId() == info.getInt("id_player") && ongoingChallenge) {
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
            } else {
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
