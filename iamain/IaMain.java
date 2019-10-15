
package iamain;

import TCPClient.TCPClient;
import org.json.JSONException;
import org.json.JSONObject;
import requete.Requete;

public class IaMain {
    private static IA ia;
    private static TCPClient client;
    
    public static void main(String[] args) {
        ia = new IA();
        client = new TCPClient(5001);
        connexion();
        Requete responseConnexion = client.retrieveData();
        if(responseConnexion.getCode() != 1000)
        {
            System.exit(-1);
        }
        
        //choose challenge
        
        while(true) // jeu
        {
            
        }
        
        //deconnexion();
        
    }
    
    public static void connexion()
    {
        JSONObject jo = new JSONObject();
        jo.append("login", ia.getLogin());
        jo.append("password", ia.getPassword());
        Requete connexion = new Requete(1,jo);
        client.sendData(connexion);
    }
    
    public static void deconnexion()
    {
        Requete deconnexion = new Requete(7);
        client.sendData(deconnexion);
    }
}
