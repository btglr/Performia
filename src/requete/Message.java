/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requete;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Noizet Mathieu
 */
public class Message {
    private int code;
    private JSONObject data;
    
    public Message(int code, JSONObject json) {
        this.code = code;
        this.data = json;
    }
    
    public Message(int code) {
        this(code, new JSONObject());
    }

    public Message() {
        this(-1);
    }
      
    public Message(int code, String data) {
        this.code = code;
        try {
            this.data = new JSONObject(data);
        } catch (JSONException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public JSONObject getData() {
        return data;
    }

    public void addData(String key, Object data) {
        if (this.data != null) {
            this.data.put(key, data);
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Message req = new Message(this.code, new JSONObject(this.data));
        return req;
    }

    @Override
    public String toString() {
        String details = "";
        details += "------- Requete -------";
        details += "\nCode requete : " + this.code + "\n";
        details += "Data = " + this.data + "\n";
        details += "--------------------------";                
        return details;
    }
    
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("code",this.code);
        } catch (JSONException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            json.put("data",this.data.toString());
        } catch (JSONException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
        return json;
    }
    
    public static Message fromJSON(JSONObject json) {
        Message req = null;
        try {
            req =  new Message(json.getInt("code"),new JSONObject(json.getString("data")));
        } catch (JSONException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
        return req;
    }
    
}
