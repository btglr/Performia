/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package requete;

import challenge.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import data.DBManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.MessageCode;

import static utils.AccountType.AI;
import static utils.AccountType.USER;
import static utils.MessageCode.*;
import static utils.MessageCode.getRequest;

/**
 * @author Noizet Mathieu & Dupont Kévin
 */
public class MessageManager implements Runnable {
	private static final Logger logger = Logger.getLogger(MessageManager.class.getName());

	private RequestQueue requestQueue;
	private ResponseQueue responseQueue;
	private static final Object lock = new Object();
	private static volatile MessageManager instance = null;

	private ArrayList<Participant> participants = new ArrayList<>();
	private ArrayList<Salle> rooms = new ArrayList<>();

	private MessageManager(RequestQueue requestQueue, ResponseQueue responseQueue) {
		this.requestQueue = requestQueue;
		this.responseQueue = responseQueue;
	}

	public static MessageManager getInstance() {
		MessageManager r = instance;

		if (r == null) {
			synchronized (lock) {
				r = instance;
				if (r == null) {
					r = new MessageManager(RequestQueue.getInstance(), ResponseQueue.getInstance());
					instance = r;
				}
			}
		}

		return r;
	}

	public void run() {
		Message req;

		while (true) {
			// Attente passive d'une requête
			while (requestQueue.isEmpty()) {
				synchronized (RequestQueue.getLock()) {
					logger.info("Going to sleep as I don't have any messages to process");

					try {
						RequestQueue.getLock().wait();
					} catch (InterruptedException ex) {
						logger.log(Level.SEVERE, null, ex);
					}
				}
			}

			// Attente terminée, on a été notify
			req = requestQueue.getMessage();

			// On utilise l'ID de la requête reçue pour définir qui devra recevoir la réponse
			int sourceId = req.getId();

			MessageCode code = getRequest(req.getCode());
			Message response = new Message();
			JSONObject jsonObject;
			JSONArray jsonArray;

			if (code == CONNECTION || code == REGISTER) {
				int id = -1;
				int accountType = -1;

				if (code == REGISTER) {
					try {
						id = inscription(req);
					} catch (SQLException e) {
						logger.log(Level.SEVERE, null, e);
					}

					if (id == -1) {
						response.setCode(REGISTRATION_ERROR.getCode());
						response.addData("error_message", "Registration was not successful");

						logger.info("Registration was not successful");
					}

					else {
						logger.info("User successfully registered");
					}
				}

				else {
					// Si la requête de connexion vient d'une IA on l'inscrit si elle ne l'est pas déjà
					if (req.getData().has("account_type") && req.getData().getInt("account_type") == AI.getValue()) {
						boolean existe = true;
						try {
							logger.info("Checking that the AI account doesn't already exist");
							existe = verifierUtilisateur(req);
						} catch (SQLException e) {
							logger.log(Level.SEVERE, null, e);
						}

						if (!existe) {
							logger.info("AI account doesn't exist, creating");
							try {
								id = inscription(req);
							} catch (SQLException e) {
								logger.log(Level.SEVERE, null, e);
							}
						}

						accountType = req.getData().getInt("account_type");
					}

					try {
						logger.info("Trying to connect");
						id = connexion(req);
					} catch (SQLException e) {
						logger.log(Level.SEVERE, null, e);
					}

					if (id == -1) {
						response.setCode(CONNECTION_ERROR.getCode());
						response.addData("error_message", "Connection was not successful");

						logger.info("Connection was not successful");
					}

					else {
						logger.info("User successfully connected");
					}
				}

				if (id != -1) {
					response.setCode(CONNECTION_OK.getCode());
					response.addData("user_id", id);

					try {
						accountType = (accountType == -1) ? determinerTypeCompte(id) : accountType;
					} catch (SQLException e) {
						logger.log(Level.SEVERE, null, e);
					}

					response.addData("account_type", accountType);
				}
			}

			else {
				if (req.getData().has("user_id")) {
					Participant p = getParticipantByID(req.getData().getInt("user_id"));

					if (p == null) {
						response.setCode(UNKNOWN_USER.getCode());
					}

					else {
						switch (code) {
							// Choix d'un challenge
							case CHOOSE_CHALLENGE:
								jsonObject = choisirChallenge(req);

								if (jsonObject.has("code")) {
									if (jsonObject.getInt("code") == WRONG_CHALLENGE.getCode()) {
										response.setCode(WRONG_CHALLENGE.getCode());
									}
									else if (jsonObject.getInt("code") == ROOM_NOT_FULL.getCode()) {
										response.setCode(ROOM_NOT_FULL.getCode());
										response.addData("room_id", jsonObject.getInt("room_id"));
									}
									else {
										response.setCode(UNKNOWN.getCode());
									}
								}

								else {
									response.setCode(INITIAL_CHALLENGE_STATE.getCode());
									int roomId = jsonObject.getInt("room_id");
									jsonObject.remove("room_id");

									response.addData("data", jsonObject);
									response.addData("room_id", roomId);
								}

								break;

							// Jouer un tour
							case PLAY_TURN:
								jsonObject = jouerTour(req);

								if (jsonObject == null) {
									response.setCode(ACTION_NOT_OK.getCode());
								}

								else {
									response.setCode(ACTION_OK.getCode());
									response.addData("data", jsonObject);
								}

								break;

							// Demande d'actualisation de l'état du jeu
							case GET_CHALLENGE_STATE:
								jsonObject = actualisation(req);

								if (jsonObject == null) {
									response.setCode(USER_NOT_PLAYING.getCode());
								}

								else {
									response.setCode(CHALLENGE_STATE.getCode());
									response.addData("data", jsonObject);
								}

								break;

							// Attente du début du challenge
							case WAIT_CHALLENGE_START:
								boolean canStart = checkCanChallengeStart(req);

								response.setCode(canStart ? CHALLENGE_CAN_START.getCode() : CHALLENGE_CANNOT_START.getCode());

								break;

							// Demande de la liste des challenges
							case GET_LIST_CHALLENGE:
								jsonArray = getListChallenge();

								if (jsonArray == null) {
									response.setCode(ACTION_NOT_OK.getCode());
								}

								else {
									response.setCode(LIST_CHALLENGE.getCode());
									response.addData("data", jsonArray);
								}

								break;

							// Demande des détails d'un challenge
							case GET_CHALLENGE_DETAILS:
								jsonObject = getChallengeDetails(req);

								if (jsonObject == null) {
									response.setCode(ACTION_NOT_OK.getCode());
								}

								else if (!jsonObject.has("challenge_name")) {
									response.setCode(WRONG_CHALLENGE.getCode());
								}

								else {
									response.setCode(CHALLENGE_DETAILS.getCode());
									response.addData("data", jsonObject);
								}

								break;

							case GET_AI_LIST:
								try {
									jsonArray = getAITypes();

									if (jsonArray == null) {
										response.setCode(ACTION_NOT_OK.getCode());
									}

									else {
										response.setCode(AI_TYPES.getCode());
										response.addData("data", jsonArray);
									}

								} catch (SQLException e) {
									logger.log(Level.SEVERE, null, e);
								}
								break;
						}
					}
				}

				else {
					response.setCode(MISSING_PARAMETERS.getCode());
				}
			}

			response.setDestination(sourceId);

			if (responseQueue.addResponse(response)) {
				logger.info("Response was added to the ResponseQueue");
			}
		}
	}

	private JSONArray getAITypes() throws SQLException {
		PreparedStatement query;
		ResultSet resultat;
		JSONArray ar = new JSONArray();

		DBManager db = DBManager.getInstance();

		Connection dbConnection;

		try {
			dbConnection = db.getConnection();
		} catch (JSONException e) {
			System.err.println("An exception occurred while creating the connection to the database. Please check that the configuration file exists.");
			return null;
		}

		query = dbConnection.prepareStatement("SELECT * FROM ai");
		resultat = query.executeQuery();

		while (true) {
			try {
				if (!resultat.next()) break;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			JSONObject ob = new JSONObject();

			try {
				ob.put("ai_id", resultat.getInt("id"));
				ob.put("ai_name", resultat.getString("name"));
				ob.put("ai_host", resultat.getString("host"));
				ob.put("ai_port", resultat.getInt("port"));
				ar.put(ob);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return ar;
	}

	private int determinerTypeCompte(int id) throws SQLException {
		ResultSet resultat;
		int accountType = -1;

		DBManager db = DBManager.getInstance();

		Connection dbConnection;
		try {
			dbConnection = db.getConnection();
		} catch (JSONException e) {
			System.err.println("An exception occurred while creating the connection to the database. Please check that the configuration file exists.");
			return -1;
		}

		PreparedStatement query = dbConnection.prepareStatement("SELECT type FROM user WHERE id=?");
		query.setInt(1, id);
		resultat = query.executeQuery();

		if (resultat.next()) {
			accountType = resultat.getInt(1);
		}

		return accountType;
	}

	private boolean checkCanChallengeStart(Message request) {
		if (request.getData().has("room_id")) {
			int room_id = request.getData().getInt("room_id");
			Salle s = getRoomByID(room_id);

			return s != null && s.estPleine();
		}

		return false;
	}

	private boolean verifierUtilisateur(Message requete) throws SQLException {
		String login, password;
		ResultSet resultat;
		boolean b = false;

		try {
			login = requete.getData().getString("login");
			password = requete.getData().getString("password");
		} catch (JSONException e) {
			logger.log(Level.SEVERE, null, e);
			return false;
		}

		DBManager db = DBManager.getInstance();

		Connection dbConnection;
		try {
			dbConnection = db.getConnection();
		} catch (JSONException e) {
			System.err.println("An exception occurred while creating the connection to the database. Please check that the configuration file exists.");
			return false;
		}

		PreparedStatement query = dbConnection.prepareStatement("SELECT COUNT(*) FROM user WHERE username=? AND password=?");
		query.setString(1, login);
		query.setString(2, password);
		resultat = query.executeQuery();

		if (resultat.next()) {
			b = resultat.getInt(1) == 1;
		}

		return b;
	}

	/**
	 * Connecte une entité avec un couple login/password
	 * @param requete la requête contenant les informations de connexion
	 * @return l'id de l'utilisateur si celui-ci existe, -1 pour toute autre raison
	 * @throws SQLException si la connexion n'a pas pu être effectuée à la base de données
	 */
	private int connexion(Message requete) throws SQLException {
		String login, password;
		int id = -1;
		ResultSet resultat;

		try {
			login = requete.getData().getString("login");
			password = requete.getData().getString("password");
		} catch (JSONException e) {
			logger.log(Level.SEVERE, null, e);
			return -1;
		}

		DBManager db = DBManager.getInstance();

		Connection dbConnection;
		try {
			dbConnection = db.getConnection();
		} catch (JSONException e) {
			System.err.println("An exception occurred while creating the connection to the database. Please check that the configuration file exists.");
			return -1;
		}

		PreparedStatement query = dbConnection.prepareStatement("SELECT id FROM user WHERE username=? AND  password=?");
		query.setString(1, login);
		// Password envoyé en SHA1 par l'interface/l'IA !
		query.setString(2, password);
		resultat = query.executeQuery();

		if (resultat.next()) {
			id = resultat.getInt(1);

			participants.add(new Participant(id));
		}

		if (resultat.next()) {
			id = -1; // Erreur plusieurs même login/mdp
		}

		return id;
	}

	/**
	 * Inscrit une entité avec un couple login/password, un genre et une date de naissance
	 * @param requete la requête contenant les informations d'inscription
	 * @return l'id de l'utilisateur si celui-ci a été correctement créé, sinon -1
	 * @throws SQLException si la connexion n'a pas pu être effectuée ou si l'ajout a échoué
	 */
	private int inscription(Message requete) throws SQLException {
		String login, password;
		int gender, accountType;
		LocalDate birthdate;

		int id;
		int affectedRows;

		try {
			login = requete.getData().getString("login");
			password = requete.getData().getString("password");

			// Si aucun genre n'a été renseigné alors on le met à "autre"
			gender = (requete.getData().has("gender")) ? requete.getData().getInt("gender") : 3;

			// Si aucune date anniversaire n'a été renseignée alors on prend la date du jour
			birthdate = (requete.getData().has("birthdate")) ? (LocalDate) requete.getData().get("birthdate") : LocalDate.now();

			// Si aucun argument n'a été passé à propos du type de compte alors par défaut, c'est un utilisateur lambda
			accountType = (requete.getData().has("account_type")) ? requete.getData().getInt("account_type") : USER.getValue();
		} catch (JSONException e) {
			logger.log(Level.SEVERE, null, e);
			return -1;
		}

		DBManager db = DBManager.getInstance();

		Connection dbConnection;
		try {
			dbConnection = db.getConnection();
		} catch (JSONException e) {
			System.err.println("An exception occurred while creating the connection to the database. Please check that the configuration file exists.");
			return -1;
		}

		PreparedStatement query = dbConnection.prepareStatement("INSERT INTO user (username, password, birthdate, gender, type) VALUES(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		query.setString(1, login);
		query.setString(2, password);
		query.setDate(3, java.sql.Date.valueOf(birthdate));
		query.setInt(4, gender);
		query.setInt(5, accountType);
		affectedRows = query.executeUpdate();

		if (affectedRows == 0) {
			throw new SQLException("An exception occurred while creating the user: user already exists");
		}

		try (ResultSet generatedKeys = query.getGeneratedKeys()) {
			if (generatedKeys.next()) {
				id = generatedKeys.getInt(1);
			}
			else {
				throw new SQLException("An exception occurred while creating the user");
			}
		}

		return id;
	}

	private JSONObject actualisation(Message requete) {
		int idUser = requete.getData().getInt("user_id");
		int idRoom = requete.getData().getInt("room_id");

		Participant p = getParticipantByID(idUser);

		if (p != null) {
			Salle s = getRoomByID(idRoom);

			if (s == null) {
				logger.info("Given room ID doesn't exist or is incorrect");
				return null;
			}

			// Si la salle contient bien l'id du joueur qui a fait la requête
			if (s.getJoueurs().contains(idUser)) {
				if (s.getChallenge().estFini()) {
					s.fermer();
				}

				return s.getChallenge().toJson();
			}

			else {
				logger.info("User is not in this game room");
			}
		}

		else {
			logger.info("User is not currently playing");
		}

		return null;
	}

	private JSONObject choisirChallenge(Message requete) {
		int idUser = requete.getData().getInt("user_id");
		int challengeId = requete.getData().getInt("challenge_id");

		// Ne peut être null ici, vérification antérieure
		Participant p = getParticipantByID(idUser);
		Salle s = findAvailableRoom();

		if (s == null) {
			switch (challengeId) {
				case 1:
					s = new Salle(new Connect4(p), 2);
					break;

				case 2:
					s = new Salle(new Reflex(p), 4);
					break;

				default:
					return new JSONObject().put("code", WRONG_CHALLENGE.getCode());
			}

			rooms.add(s);
		}

		else {
			s.getChallenge().addPlayer(p);
		}

		s.addJoueur(idUser);

		if (s.estPleine()) {
			return s.getChallenge().toJson().put("room_id", s.getId());
		}

		else {
			return new JSONObject().put("code", ROOM_NOT_FULL.getCode()).put("room_id", s.getId());
		}
	}

	private JSONObject jouerTour(Message requete) {
		int idUser = requete.getData().getInt("user_id");
		int idRoom = requete.getData().getInt("room_id");
		Participant p = getParticipantByID(idUser);
		Salle s = getRoomByID(idRoom);

		if (p == null || s == null) {
		    logger.info("No player found with the provided id");
			return null;
		}

		// Si le joueur qui essaye de jouer un tour n'est pas celui qui est présent dans la salle de challenge
		if (!s.getJoueurs().contains(idUser)) {
			return null;
		}

		if(!s.getChallenge().estFini() && p.getId() == s.getChallenge().getCurrentPlayerId()) {
			if (s.getChallenge().jouerCoup(requete.getData())) {
				// Coup ok ici
				return s.getChallenge().toJson();
			}

			else {
				// Coup pas ok
				return null;
			}
		}

		if (s.getChallenge().estFini()) {
			s.fermer();
		}

		return s.getChallenge().toJson();
	}

	private Participant getParticipantByID(int id) {
		return participants.stream().filter(participant -> (participant.getId()==id)).findFirst().orElse(null);
	}

/*	private Salle getRoomByID(int user_id) {
		return rooms.stream()
				.filter(salle -> (Arrays.stream(salle.getJoueurs().toArray()))
				.filter(id -> id==user_id).findFirst().isPresent()).findFirst().orElse(null);
	}*/

	private Salle getRoomByID(int room_id) {
		if (room_id >= 0 && room_id < rooms.size()) {
			return rooms.get(room_id);
		}

		return null;
	}

	private Salle findAvailableRoom() {
		Salle s = null;

		for (Salle tmp : rooms) {
			if (tmp != null && !tmp.estPleine() && !tmp.estFermee()) {
				s = tmp;
			}
		}

		return s;
	}

	public JSONArray getListChallenge() {
		PreparedStatement query;
		ResultSet resultat = null;
		JSONArray ar = new JSONArray();

		DBManager db = DBManager.getInstance();

		Connection dbConnection = null;

		try {
			dbConnection = db.getConnection();
		} catch (SQLException e) {
			System.err.println("An exception occurred while creating the connection to the database. Please check that the database is online.");
		} catch (JSONException e) {
			System.err.println("An exception occurred while creating the connection to the database. Please check that the configuration file exists.");
		}

		try {
			query = dbConnection.prepareStatement("SELECT * FROM challenge");
			resultat = query.executeQuery();
		} catch (SQLException e){
			System.err.println("An exception occurred while creating the connection to the database. Please check that the database is online.");
		}

		while (true){
			try {
				if (!resultat.next()) break;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			JSONObject ob = new JSONObject();

			try {
				ob.put("challenge_id", resultat.getInt("challenge_id"));
				ob.put("challenge_name", resultat.getString("challenge_name"));
				ob.put("challenge_description", resultat.getString("challenge_description"));
				ar.put(ob);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return ar;
	}

	public JSONObject getChallengeDetails(Message requete) {
		int challengeId = requete.getData().getInt("challenge_id");
		PreparedStatement query;
		ResultSet resultat;
		JSONObject ob = null;
		DBManager db = DBManager.getInstance();

		Connection dbConnection;
		try {
			dbConnection = db.getConnection();
			query = dbConnection.prepareStatement("SELECT * FROM challenge WHERE challenge_id = ?");
			query.setInt(1, challengeId);
			resultat = query.executeQuery();

			ob = new JSONObject();

			// Si le challenge ID était correct alors il y aura des résultats, sinon un JSONObject vide sera retourné
			// indiquant qu'aucun challenge ne correspondait
			if (resultat.next()) {
				ob.put("challenge_name", resultat.getString("challenge_name"));
				ob.put("challenge_description", resultat.getString("challenge_description"));
			}
		} catch (SQLException e) {
			System.err.println("An exception occurred while creating the connection to the database. Please check that the database is online.");
		} catch (JSONException e) {
			System.err.println("An exception occurred while creating the connection to the database. Please check that the configuration file exists.");
		}

		return ob;
	}
}
