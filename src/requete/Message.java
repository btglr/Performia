/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requete;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import utils.ProtocolType;

/**
 *
 * @author Noizet Mathieu
 */
public class Message {
    // Compteur d'instance qui sert à attribuer un ID à un message
    private static final AtomicInteger count = new AtomicInteger(0);
    private final int id;
    private int code;
    private ProtocolType protocolType;
    private JSONObject data;

    // Dans le cas d'une réponse, contient l'id de la requête originelle
    private int destination;
    
    public Message(int code, JSONObject json, ProtocolType protocolType) {
        this.code = code;
        this.data = json;
        this.protocolType = protocolType;
        this.id = count.incrementAndGet();
        this.destination = -1;
    }
    
    public Message(int code) {
        this(code, new JSONObject(), null);
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
        this.id = count.incrementAndGet();
        this.destination = -1;
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
        Message req = new Message(this.code, new JSONObject(this.data), this.protocolType);
        return req;
    }

    @Override
    public String toString() {
        String details = "";
        details += "------- Requete -------";
        details += "\nCode requete : " + this.code + "\n";
        details += "Data = " + this.data + "\n";
        details += "Protocole = " + this.protocolType + "\n";
        details += "--------------------------";                
        return details;
    }
    
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("code", this.code);
        } catch (JSONException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            json.put("data", this.data.toString());
        } catch (JSONException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            json.put("protocolType", ProtocolType.getValue(this.protocolType));
        } catch (JSONException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }

        return json;
    }
    
    public static Message fromJSON(JSONObject json) {
        Message req = null;
        try {
            req =  new Message(json.getInt("code"), new JSONObject(json.getString("data")), ProtocolType.getProtocolType(json.getInt("protocolType")));
        } catch (JSONException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
        return req;
    }

    public ProtocolType getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(ProtocolType protocolType) {
        this.protocolType = protocolType;
    }

    public int getId() {
        return id;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }
}
