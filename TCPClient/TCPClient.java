
package TCPClient;

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
import org.json.JSONObject;
import requete.Requete;

public class TCPClient
{
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;

    public TCPClient(String login, String mdp) 
    {
        try 
        {
            socket = new Socket("localhost", 5001);
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            JSONObject jo = new JSONObject();
            jo.append("login", login);
            jo.append("password", mdp);
            Requete connexion = new Requete(1,jo);
            sendData(connexion);
            Requete responseConnexion = retrieveData();
            if(responseConnexion.getCode() != 1000)
            {
                System.err.println("Connexion failed");
                System.exit(-1);
            }
        } 
        catch(UnknownHostException e) 
        {
            System.err.println("Erreur sur l'hôte : " + e);
            System.exit(-1);
        } 
        catch(IOException e) 
        {
            System.err.println("Création de la socket impossible : " + e);
            System.exit(-1);
        }
    }

    private void sendData(Requete req) 
    {
        out.println(req.toJSON());
    }
    
    private Requete retrieveData()
    {
        Requete response = null;
        try {
            response = Requete.fromJSON(new JSONObject(in.readLine()));
        } catch (IOException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return response;
    }
    
    public JSONObject demandeChallenge(int numeroChallenge)
    {
            JSONObject jo = new JSONObject();
            jo.append("numero_challenge", numeroChallenge);
            Requete connexion = new Requete(2,jo);
            sendData(connexion);
            Requete responseConnexion = retrieveData();
            if(responseConnexion.getCode() != 3)
            {
                System.err.println("Demande Challenge failed");
                System.exit(-1);
            }
            return responseConnexion.getData();
    }
    
    public JSONObject jouerTour(JSONObject action)
    {
        Requete connexion = new Requete(4,action);
        sendData(connexion);
        Requete responseConnexion = retrieveData();
        if(responseConnexion.getCode() != 5)
        {
            System.err.println("Demande Challenge failed");
            System.exit(-1);
        }
        return responseConnexion.getData();
    }
    
    public void CloseSocket()
    {
        try {
            Requete deconnexion = new Requete(7);
            sendData(deconnexion);
            this.socket.close();
            this.out.close();
            this.in.close();
        } catch (IOException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
