package ai;/* Dreugui && Laurie
 * //========================================<>=================================================//
 * //========================================<>=================================================//
 * //===									 AI												====//
 * //========================================<>=================================================//
 * //========================================<>=================================================//
 * 	IA puissance 4 V1.2 Aleatoire
 * //===========================================================================================//
 * //										IMPORT												//
 * //===========================================================================================//
 * */
/*================================<DEFINE>================================*/

/*================================<GENERAL>================================*/

import data.Config;
import org.json.JSONObject;

/*================================<DECLARATION>================================*/
public class AI {

    /*
     * //===========================================================================================//
     * //										MAIN												//
     * //===========================================================================================//
     * */
    /*================================<FONCTION>================================*/
    private static int choisit_Alea(int grille[]) {
        int res;

        res = (int) (Math.random() * 7);
        while (grille[res] != 0) {
            res = (int) (Math.random() * 7);
            System.out.println("Res = " + res);
        }

        return res;
    }

    /*================================<MAIN>================================*/

    public static void main(String[] args) {

        /*--------------------------------<DECLARATION>--------------------------------*/
        // lecteur JSON ID MPD
        Config ia = new Config("config/ia.json");
        /*Conexion au serveur id+mdp*/
        TCPClient connexion = new TCPClient(ia.getString("ID"), ia.getString("mdp"));
        int choix, id;
        int grille[];
        boolean partieEnCours = true;
        int numeroChalenge = 0;
        JSONObject info;
        JSONObject reponse = new JSONObject();

        /*--------------------------------<INITIALISATION>--------------------------------*/
        /*Demande de chalege au serveur et recuperation de l'ID*/
        id = connexion.demandeChallenge(numeroChalenge).getInt("id_utilisateur");

        /*--------------------------------<PARTIE>--------------------------------*/
        while (partieEnCours) {
            info = connexion.retrieveData().toJSON();
            partieEnCours = info.getBoolean("fini");
            grille = (int[]) info.get("grille");

            if (id == info.getInt("id_player") && partieEnCours) {
                //choisit
                choix = choisit_Alea(grille);
                reponse.put("column", choix);
                //envoie
                connexion.jouerTour(reponse);
            }
        }

        /*--------------------------------<FIN>--------------------------------*/
		connexion.CloseSocket();
    }
}








