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
        synchronized (lock) {
            boolean result = addMessage(req);

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
