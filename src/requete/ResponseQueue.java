package requete;

/**
 * Classe Singleton gérant les réponses
 */
public class ResponseQueue extends MessageQueue {
    private static final Object lock = new Object();
    private static volatile ResponseQueue instance = null;

    private ResponseQueue() {
    }

    public static ResponseQueue getInstance() {
        ResponseQueue r = instance;

        if (r == null) {
            synchronized (lock) {
                r = instance;
                if (r == null) {
                    r = new ResponseQueue();
                    instance = r;
                }
            }
        }

        return r;
    }

    public boolean addResponse(Message req) {
        return addMessage(req);
    }

    public synchronized void addManager(MessageManager manager) {
        super.addManager(manager);
    }
}
