
package ai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import data.Config;
import org.json.JSONObject;
import requete.Message;
import utils.MessageCode;
import utils.ProtocolType;

public class TCPClient {
    private static final Logger logger = Logger.getLogger(TCPClient.class.getName());

    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private int user_id;

    public TCPClient(String login, String mdp) {
        try {
            Config config = new Config("config/config.json");

            socket = new Socket("localhost", config.getInt("port_tcp"));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            JSONObject jo = new JSONObject();
            jo.put("login", login);
            jo.put("password", mdp);

            Message connexion = new Message(MessageCode.CONNECTION.getCode(), jo);
            sendData(connexion);
            Message responseConnexion = retrieveData();

            if (responseConnexion.getCode() != MessageCode.CONNECTION_OK.getCode()) {
                System.err.println("Connection failed");
                System.exit(-1);
            }

            if (responseConnexion.getData().has("id_utilisateur")) {
                user_id = responseConnexion.getData().getInt("id_utilisateur");
            }
        } catch (UnknownHostException e) {
            System.err.println("Erreur sur l'hôte : " + e);
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("Création de la socket impossible : " + e);
            System.exit(-1);
        }
    }

    private void sendData(Message req) {
        out.println(req.toJSON());
    }

    public Message retrieveData() {
        Message response = null;
        try {
            String res = in.readLine();

            if (res != null) {
                JSONObject obj = new JSONObject(res);
                response = Message.fromJSON(obj);
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        return response;
    }

    public JSONObject demandeChallenge(int numeroChallenge) {
        JSONObject jo = new JSONObject();
        jo.put("id_utilisateur", user_id);
        jo.put("numero_challenge", numeroChallenge);

        Message connexion = new Message(MessageCode.CHOOSE_CHALLENGE.getCode(), jo);
        sendData(connexion);
        Message responseConnexion = retrieveData();
        if (responseConnexion.getCode() != MessageCode.INITIAL_CHALLENGE_STATE.getCode()) {
            System.err.println("Demande Challenge failed");
            System.exit(-1);
        }
        return responseConnexion.getData();
    }

    public JSONObject jouerTour(JSONObject action) {
        Message connexion = new Message(4, action);
        sendData(connexion);
        Message responseConnexion = retrieveData();
        if (responseConnexion.getCode() != MessageCode.ACTION_OK.getCode()) {
            System.err.println("Demande Challenge failed");
            System.exit(-1);
        }
        return responseConnexion.getData();
    }

    public void CloseSocket() {
        try {
            Message deconnexion = new Message(7);
            sendData(deconnexion);
            this.socket.close();
            this.out.close();
            this.in.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
}
