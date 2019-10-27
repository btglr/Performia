package requete;

import java.util.LinkedList;

/**
 * Classe abstraite de messages (requêtes, réponses)
 */
public abstract class MessageQueue {
    private LinkedList<Message> list = new LinkedList<Message>();
    private static RequeteManager manager = null;

    protected synchronized Message getMessage() {
        return list.poll();
    }

    /**
     * Ajoute un message à la file de messages
     * @param msg le message à ajouter
     */
    protected boolean addMessage(Message msg) {
        synchronized (RequeteManager.getLock()) {
            boolean result = list.add(msg);

            if (manager != null) {
                RequeteManager.getLock().notify();
            }

            return result;
        }
    }

    /**
     * Détermine si la file de messages est vide
     * @return true ou false
     */
    protected boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Supprime le RequestManager associé à l'instance
     * @return la référence du RequestManager
     */
    protected synchronized RequeteManager deleteManager() {
        RequeteManager man = manager;
        manager = null;
        return man;
    }

    /**
     * Ajoute un RequestManager
     * @param manager le RequestManager
     */
    protected synchronized void addManager(RequeteManager manager) {
        MessageQueue.manager = manager;
    }

    /**
     * Détermine si la classe possède un RequestManager
     * @return true ou false
     */
    protected boolean hasManager() {
        return manager != null;
    }
}
