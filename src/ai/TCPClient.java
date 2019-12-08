
package ai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;
import requete.Message;
import utils.MessageCode;

public class TCPClient {
	private static final Logger logger = Logger.getLogger(TCPClient.class.getName());

	private Socket socket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;

	private int userId;
	private int roomId;

	public TCPClient(String host, int port) {
		try {
			socket = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (UnknownHostException e) {
			System.err.println("Erreur sur l'hôte : " + e);
			System.exit(-1);
		} catch (IOException e) {
			System.err.println("Création de la socket impossible : " + e);
			System.exit(-1);
		}
	}

	private static String encryptPassword(String password) {
		String sha1 = "";
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(password.getBytes("UTF-8"));
			sha1 = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return sha1;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	public boolean waitChallengeStart() {
		JSONObject jo = new JSONObject();
		jo.put("user_id", userId);
		jo.put("room_id", roomId);

		Message message = new Message(MessageCode.WAIT_CHALLENGE_START.getCode(), jo);
		sendData(message);

		Message response = retrieveData();

		return response.getCode() == MessageCode.CHALLENGE_CAN_START.getCode();
	}

	public void connect(String login, String password, int account_type) {
		JSONObject jo = new JSONObject();
		jo.put("login", login);
		jo.put("password", encryptPassword(password));
		jo.put("account_type", account_type);

		Message connexion = new Message(MessageCode.CONNECTION.getCode(), jo);
		sendData(connexion);
		Message responseConnexion = retrieveData();

		if (responseConnexion.getCode() != MessageCode.CONNECTION_OK.getCode()) {
			logger.info("Connection failed");
			System.exit(-1);
		}

        userId = responseConnexion.getData().getInt("user_id");
	}

    public int getUserId() {
        return userId;
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

	public JSONObject chooseChallenge(int numeroChallenge) {
		JSONObject jo = new JSONObject();
		jo.put("user_id", userId);
		jo.put("challenge_id", numeroChallenge);

		Message connexion = new Message(MessageCode.CHOOSE_CHALLENGE.getCode(), jo);
		sendData(connexion);

		Message responseConnexion = retrieveData();

		// Si la réponse n'est pas soit l'état du challenge initial, soit un message nous indiquant qu'il manque un joueur alors c'est une erreur et on quitte
		if (responseConnexion.getCode() != MessageCode.INITIAL_CHALLENGE_STATE.getCode() && responseConnexion.getCode() != MessageCode.ROOM_NOT_FULL.getCode()) {
			logger.info("Choosing a challenge failed");
			System.exit(-1);
		}

		if (responseConnexion.getData().has("room_id")) {
			roomId = responseConnexion.getData().getInt("room_id");
		}

		return responseConnexion.getData();
	}

	public JSONObject getChallengeState() {
        JSONObject jo = new JSONObject();
        jo.put("user_id", userId);
        jo.put("room_id", roomId);

        Message message = new Message(MessageCode.GET_CHALLENGE_STATE.getCode(), jo);
        sendData(message);

        Message response = retrieveData();
        if (response.getCode() != MessageCode.CHALLENGE_STATE.getCode()) {
            logger.info("Couldn't retrieve the challenge state");
            System.exit(-1);
        }

        return response.getData();
    }

    public void sendTurn(JSONObject action)
    {
        Message message = new Message(MessageCode.PLAY_TURN.getCode(), action);
        sendData(message);
    }

    public JSONObject receiveTurn()
    {
        Message response = retrieveData();

        if (response.getCode() != MessageCode.ACTION_OK.getCode()) {
            logger.info("Incorrect move");
            System.exit(-1);
        }
        return response.getData();
    }

    public JSONObject playTurn(JSONObject action) {
        sendTurn(action);

        return receiveTurn();
    }

	public void closeSocket() {
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

	public int getRoomId() {
		return roomId;
	}
}
