/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author Noizet Mathieu
 */
public class ServeurTCP implements Runnable{
    private int port ;
    private String address;
    private ServerSocket serverSocket;
    
    public ServeurTCP(int port){
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(ServeurTCP.class.getName()).log(Level.SEVERE, null, ex);
            serverSocket = null;
        }
        this.port = port;
    }
    
    /* 
    public ServeurTCP(String configFile)
    */
    public void run(){
        
        boolean quitter = false;
        /* Creation de la socket */

        /*Attente connexion Client*/
       Socket socketClient = null;
       while(quitter != true){
           try {
               socketClient = serverSocket.accept();
           } catch (IOException ex) {
               Logger.getLogger(ServeurTCP.class.getName()).log(Level.SEVERE, null, ex);
           }

           /* Redirection de la socket vers un thread */
       }
       try{
           /*Fermeture socket*/
           serverSocket.close();
       } catch (IOException ex) {
           Logger.getLogger(ServeurTCP.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
}
