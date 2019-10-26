package requete;

public class RequestQueue extends MessageQueue {
    private static RequestQueue instance = new RequestQueue();

    private RequestQueue() {
    }

    public static RequestQueue getInstance() {
        return instance;
    }

    public boolean addRequest(Message req) {
        return addMessage(req);
    }

    public synchronized void addManager(RequeteManager manager) {
        super.addManager(manager);
    }
}
