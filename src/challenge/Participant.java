package challenge;

public class Participant {
    private int id;
    private int sourceIdRequest;

    public Participant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSourceIdRequest() {
        return sourceIdRequest;
    }

    public void setSourceIdRequest(int sourceIdRequest) {
        this.sourceIdRequest = sourceIdRequest;
    }
}