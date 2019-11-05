package challenge;

import org.json.JSONObject;

public class Participant {
    private int id;

    public Participant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public JSONObject toJson() {
        return new JSONObject().put("id", id);
    }

    public static Participant fromJson(JSONObject jsonObject) {
        return new Participant(jsonObject.getInt("id"));
    }
}