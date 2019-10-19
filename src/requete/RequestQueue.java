package requete;

public class RequestQueue extends MessageQueue {
    private static RequestQueue instance = null;

    private RequestQueue() {
    }

    public static RequestQueue getInstance() {
        if (instance == null)
            instance = new RequestQueue();

        return instance;
    }

    public boolean addRequest(Requete req) {
        return addMessage(req);
    }
}
