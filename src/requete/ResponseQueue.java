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

    public boolean addResponse(Message response) {
        synchronized (lock) {
            if (response.getData() != null && !response.getData().has("code")) {
                response.getData().put("code", response.getCode());
            }

            boolean result = addMessage(response);

            lock.notify();

            return result;
        }
    }

    public synchronized void addManager(MessageManager manager) {
        super.addManager(manager);
    }

    public static Object getLock() {
        return lock;
    }
}
