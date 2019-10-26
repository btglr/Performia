package requete;

/**
 * Classe Singleton gérant les réponses
 */
public class ResponseQueue extends MessageQueue {
    private static ResponseQueue instance = new ResponseQueue();

    private ResponseQueue() {
    }

    public static ResponseQueue getInstance() {
        return instance;
    }

    public boolean addResponse(Message req) {
        return addMessage(req);
    }

    public synchronized void addManager(RequeteManager manager) {
        super.addManager(manager);
    }
}
