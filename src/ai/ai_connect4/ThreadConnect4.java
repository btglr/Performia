package ai.ai_connect4;

import ai.TCPClient;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.MessageCode;

import java.net.Socket;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class ThreadConnect4 implements Runnable {
    private static final Logger logger = Logger.getLogger(ThreadConnect4.class.getName());

    private String login;
    private String password;
    private String javaServerHost;
    private int javaServerPort;
    private int account_type;
    private String lastMessage = null;
    private final String waitTurnMessage = "Waiting for my turn";
    
    private ArrayList<Integer> threeAligned;
    private ArrayList<Integer> twoAligned;
    private ArrayList<Integer> oneAligned;

    public ThreadConnect4(Socket socketClient, String login, String password, String javaServerHost, int javaServerPort, int account_type) {
        this.login = login;
        this.password = password;
        this.javaServerHost = javaServerHost;
        this.javaServerPort = javaServerPort;
        this.account_type = account_type;
    }

    private int randomlyChoose(JSONArray grille) {
        int res;

        res = (int) (Math.random() * 7);
        while (grille.getInt(res) != 0) {
            res = (int) (Math.random() * 7);
            System.out.println("Res = " + res);
        }

        return res;
    }
	
	//ajouter i (colonne à choisir) à un des tableaux
	private void ajouterTab(int compteur,int i) {
		if(compteur==3) {
			threeAligned.add(i);
		}else if(compteur==2) {
			twoAligned.add(i);
		}else if(compteur==1) {
			oneAligned.add(i);
		}
	}
	
	private int semiRandomChoice(int grille[]) {
		int firstEmpty,j,res,compteur=0;
		threeAligned = new ArrayList<Integer>();
		twoAligned = new ArrayList<Integer>();
		oneAligned = new ArrayList<Integer>();
		
		//parcours des colonnes pour connaitre les differentes possibilites
		for(int i=0; i<6; i++) {
			firstEmpty = i;
			
			//reucuperation de la premiere case vide de la colonne
			while(firstEmpty+7<grille.length && grille[firstEmpty+7]==0) {
				firstEmpty+=7;
			}

			//test sur les cases en dessous
			j = firstEmpty+7;
			while(j<grille.length) {
				compteur++;
				j+=7;
			}
			ajouterTab(compteur,i);
			compteur=0;
			
			//test sur les cases a gauche 
			j = firstEmpty-1;
			while(j>=firstEmpty-3 && j%7 == firstEmpty%7 && grille[j]!=0) {
				compteur++;
				j--;
			}
			ajouterTab(compteur,i);
			compteur=0;
			
			//test sur les cases a droite 
			j = firstEmpty+1;
			while(j<=firstEmpty+3 && j%7 == firstEmpty%7 && grille[j]!=0) {
				compteur++;
				j++;
			}
			ajouterTab(compteur,i);
			compteur=0;
			
			//test sur les diagonales
			//haut gauche
			j=firstEmpty-7;
			while(j-1 > 0 && grille[j-1]!=0) {
				compteur++;
				j=j-7;
			}
			ajouterTab(compteur,i);
			compteur=0;
			//haut droite
			j=firstEmpty-7;
			while(j+1 > 0 && grille[j+1]!=0) {
				compteur++;
				j=j-7;
			}
			ajouterTab(compteur,i);
			compteur=0;
			//bas gauche
			j=firstEmpty+7;
			while(j-1 < grille.length && grille[j-1]!=0) {
				compteur++;
				j=j+7;
			}
			ajouterTab(compteur,i);
			compteur=0;
			//bas droite
			j=firstEmpty+7;
			while(j+1 < grille.length && grille[j+1]!=0) {
				compteur++;
				j=j+7;
			}
			ajouterTab(compteur,i);
			compteur=0;
		}
		
		//tirage aleatoire parmi les possibilites
		if(!threeAligned.isEmpty()) {
			res = threeAligned.get((int)(Math.random() * threeAligned.size()));
		} else if(!twoAligned.isEmpty()) {
			res = twoAligned.get((int)(Math.random() * twoAligned.size()));
		} else if(!oneAligned.isEmpty()) {
			res = oneAligned.get((int)(Math.random() * oneAligned.size()));
		} else {
			res = (int)(Math.random() * 7);
		}
		
		return res;
	}

    @Override
    public void run() {
        TCPClient tcpClient = new TCPClient(this.javaServerHost, this.javaServerPort);
        int choice;
        boolean ongoingChallenge = true;
        int challengeID = 1;
        JSONObject info;
        tcpClient.connect(this.login, this.password, this.account_type);

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

            if (tcpClient.getUserId() == info.getInt("id_player") && ongoingChallenge) {
                // Choose randomly (smart AI)
                choice = semiRandomChoice(gridArray);
                response.put("user_id", tcpClient.getUserId());
                response.put("room_id", tcpClient.getRoomId());
                response.put("colonne", choice);
                try {
                    // Attente entre 1 et 5s
                    sleep((long) (1000 + (Math.random() * 4000)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                logger.info("Playing my move in column " + choice);
                lastMessage = null;

                // Play turn
                info = tcpClient.playTurn(response).getJSONObject("data");
            } else {
                if (lastMessage == null) {
                    lastMessage = waitTurnMessage;
                    logger.info(waitTurnMessage);
                }

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Ask for the state of the challenge to check if the other player has made a move
                info = tcpClient.getChallengeState().getJSONObject("data");
            }
        }

        logger.info((info.getInt("id_player") != tcpClient.getUserId()) ? "I have won": "I have lost");
        logger.info("Game is now over, disconnecting");

        tcpClient.closeSocket();
    }
}
