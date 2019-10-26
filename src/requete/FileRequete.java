/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requete;

import java.util.LinkedList;

/**
 *
 * @author Noizet Mathieu
 */
public class FileRequete {

    private static LinkedList<Message> liste = new LinkedList<Message>();
    private static RequeteManager manager;

    public FileRequete(RequeteManager manager) {
        FileRequete.manager = manager;
    }

    public FileRequete() {
    }

    public synchronized Message getRequete() {
        return liste.poll();
    }

    public synchronized boolean addRequete(Message req) {
            if (manager != null) {
                manager.notify();
            }
        return liste.add(req);
    }

    public boolean estVide() {
        return liste.isEmpty();
    }

    public synchronized RequeteManager deleteManager() {
        RequeteManager man = manager;
        manager = null;
        return man;
    }

    public synchronized void addManager(RequeteManager manager) {
        FileRequete.manager = manager;
    }

}
