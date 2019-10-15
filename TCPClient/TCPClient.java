 
package TCPClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import requete.Requete;

public class TCPClient
{
    private int port;
    private Socket socket = null;


    public TCPClient(JSONObject json) 
    {
        try 
        {
             socket = new Socket("localhost", port);
             socket.setSoTimeout(120000); //max timeOut for input is 2 min
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
        this.port = port;
    }

    //later need to be in thread
    public void sendData(Requete req) 
    {
        try 
        {
            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
            os.writeObject(req);
        }
        catch (IOException e) 
        {
            System.exit(-1);
        }
    }
    
    //later need to be in thread
    public Requete retrieveData()
    {
        Requete response = null;
        try {
            ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
            response = (Requete) is.readObject();
        } catch (IOException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
    public void CloseSocket()
    {
        try {
            this.socket.close();
        } catch (IOException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
