package requete;

/**
 * Classe Singleton gérant les réponses
 */
public class ResponseQueue extends MessageQueue {
    private static ResponseQueue instance = null;

    private ResponseQueue() {
    }

    public static ResponseQueue getInstance() {
        if (instance == null)
            instance = new ResponseQueue();

        return instance;
    }

    public boolean addResponse(Requete req) {
        return addMessage(req);
    }
}
