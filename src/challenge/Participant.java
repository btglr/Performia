package challenge;

import org.json.JSONObject;

public class Participant {
    private int id;
    private int timeAverageByTurn;
    private int countPlayTurn;
    private int lastTime;

    public Participant(int id, int countPlayTurn, int timeAverageByTurn, int lastTime) {
        this.id = id;
        this.countPlayTurn = countPlayTurn;
        this.timeAverageByTurn = timeAverageByTurn;
        this.lastTime = lastTime;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public JSONObject toJson() {
        return new JSONObject().put("id", id).put("countPlayTurn", countPlayTurn).put("timeAverageByTurn", timeAverageByTurn).put("lastTime", lastTime);
    }

    public static Participant fromJson(JSONObject jsonObject) {
        return new Participant(jsonObject.getInt("id"), jsonObject.getInt("countPlayTurn"), jsonObject.getInt("timeAverageByTurn"), jsonObject.getInt("lastTime" ));
    }

    // On commence à compter à partir de quand c'est son tour
    public void canPlay() {
        // condition pour éviter l'actualisation h24
        if(lastTime == 0) {
            lastTime = (int) System.currentTimeMillis() / 1000;
        }
    }

    // Son tour est joué, ainsi, on met à jour les dates
    public void turnPlayed() {
        int average = this.timeAverageByTurn*this.countPlayTurn;
        this.countPlayTurn++;
        this.timeAverageByTurn = (average + ((int)System.currentTimeMillis()/1000) - lastTime)/countPlayTurn;
        //on le mets à 0 pour la prochaine vérif
        this.lastTime = 0;
    }

}