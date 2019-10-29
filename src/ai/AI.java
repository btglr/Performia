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

import org.json.JSONObject;

/*================================<DECLARATION>================================*/
public class AI {

    /*
     * //===========================================================================================//
     * //										MAIN												//
     * //===========================================================================================//
     * */
    /*================================<FONCTION>================================*/
    static int choisit_Alea(int grille[]) {
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
        JSONObject ia;
        /*Conexion au serveur id+mdp*/
        TCPClient connexion = new TCPClient(ia.getString("ID"), ia.getString("mdp"));
        int choix, iD;
        int grille[];
        boolean partieEnCours = true;
        int numeroChalenge = 0;
        JSONObject info;
        JSONObject reponse = new JSONObject();

        /*--------------------------------<INITIALISATION>--------------------------------*/
        /*Demande de chalege au serveur et recuperation de l'ID*/
        iD = connexion.demandeChallenge(numeroChalenge);

        /*--------------------------------<PARTIE>--------------------------------*/
        while (partieEnCours == true) {
            info = connexion.retrieveData();
            partieEnCours = info.getBool("fini");
            grille = (int[]) info.get("grille");

            if (iD == info.getInt("id_player") && partieEnCours) {
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








