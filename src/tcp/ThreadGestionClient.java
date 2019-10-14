/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcp;

import requete.Requete;
import requete.FileRequete;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import requete.RequeteManager;

import data.Participant;
/**
 *
 * @author Noizet Mathieu
 */
public class ThreadGestionClient extends Thread {

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private int id = -1;

    public ThreadGestionClient(Socket socketClient) {
        socket = socketClient;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(ThreadGestionClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            output = new PrintWriter( new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException ex) {
            Logger.getLogger(ThreadGestionClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        /* Traitement a faire sur une socket cliente*/
        boolean deconnecter = false, connecter = false;
        FileRequete file = new FileRequete();
        String message = "";
        Requete req = null;
        RequeteManager manage = new RequeteManager();
        while(!deconnecter && connecter){
            try {
                message = input.readLine();
            } catch (IOException ex) {
                Logger.getLogger(ThreadGestionClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            if( !message.equals("")){
                try {
                    req = Requete.fromJSON(new JSONObject(message));
                } catch (JSONException ex) {
                    Logger.getLogger(ThreadGestionClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                if( !file.addRequete(req)){
                    System.err.println("Erreur lors de l'ajout de la requete dans la file");
                }
                if(req.getCode() == 1){
                    try {
                        id = manage.connexion(req);
                    } catch (SQLException ex) {
                        Logger.getLogger(ThreadGestionClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if( connecter == false){
                        Requete rep = new Requete(1001);
                        output.write(rep.toJSON().toString());
                        //id = a recupérer dans la bdd 
                    }
                }
                if( req.getCode() == 7)
                    deconnecter = true;
            }
        }
        if( connecter == true ) {
            Performia.participants.add(new Participant(id,input,output));
        }
        while (!deconnecter) {
            try {
                message = input.readLine();
            } catch (IOException ex) {
                Logger.getLogger(ThreadGestionClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            if( !message.equals("")){
                try {
                    req = Requete.fromJSON(new JSONObject(message));
                    req.getData().put("id_user",id );
                } catch (JSONException ex) {
                    Logger.getLogger(ThreadGestionClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                if( !file.addRequete(req)){
                    System.err.println("Erreur lors de l'ajout de la requete dans la file");
                }
                if( req.getCode() == 7)
                    deconnecter = true;
            }
        }
        
        try {
            socket.close();
            input.close();
            output.close();
        } catch (IOException ex) {
            Logger.getLogger(ThreadGestionClient.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
}