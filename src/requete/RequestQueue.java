package requete;

public class RequestQueue extends MessageQueue {
    private static final Object lock = new Object();
    private static volatile RequestQueue instance = null;

    private RequestQueue() {
    }

    public static RequestQueue getInstance() {
        RequestQueue r = instance;

        if (r == null) {
            synchronized (lock) {
                r = instance;
                if (r == null) {
                    r = new RequestQueue();
                    instance = r;
                }
            }
        }

        return r;
    }

    public boolean addRequest(Message req) {
        return addMessage(req);
    }

    public synchronized void addManager(RequeteManager manager) {
        super.addManager(manager);
    }
}
