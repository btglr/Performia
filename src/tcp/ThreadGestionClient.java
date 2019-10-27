/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcp;

import challenge.Participant;
import org.json.JSONException;
import org.json.JSONObject;
import requete.Message;
import requete.MessageManager;
import requete.RequestQueue;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException ex) {
            Logger.getLogger(ThreadGestionClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        /* Traitement a faire sur une socket cliente*/
        boolean deconnecter = false, connecter = false;
        MessageManager manage = MessageManager.getInstance();
        RequestQueue file = RequestQueue.getInstance();
        String message = "";
        Message req = null;

        while (!deconnecter && connecter) {
            try {
                message = input.readLine();
            } catch (IOException ex) {
                Logger.getLogger(ThreadGestionClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!message.equals("")) {
                try {
                    req = Message.fromJSON(new JSONObject(message));
                } catch (JSONException ex) {
                    Logger.getLogger(ThreadGestionClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (!file.addRequest(req)) {
                    System.err.println("Erreur lors de l'ajout de la requete dans la file");
                }
                if (req.getCode() == 1) {
                    try {
                        id = manage.connexion(req);
                    } catch (SQLException ex) {
                        Logger.getLogger(ThreadGestionClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (!connecter) {
                        Message rep = new Message(1001);
                        output.write(rep.toJSON().toString());
                        //id = a recupérer dans la bdd 
                    }
                }
                if (req.getCode() == 7)
                    deconnecter = true;
            }
        }
        if (connecter) {
            Performia.participants.add(new Participant(id, input, output));
        }
        while (!deconnecter) {
            try {
                message = input.readLine();
            } catch (IOException ex) {
                Logger.getLogger(ThreadGestionClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!message.equals("")) {
                try {
                    req = Message.fromJSON(new JSONObject(message));
                    req.getData().put("id_user", id);
                } catch (JSONException ex) {
                    Logger.getLogger(ThreadGestionClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (!file.addRequest(req)) {
                    System.err.println("Erreur lors de l'ajout de la requete dans la file");
                }
                if (req.getCode() == 7)
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
