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
public class Requete {
    private int code;
    private JSONObject data;
    
    public Requete(int code, JSONObject json){
        this.code = code;
        this.data = json;
    }
    
    public Requete(int code)
    {
        this.code = code;
    }
      
    public Requete(int code, String data){
        this.code = code;
        try {
            this.data = new JSONObject(data);
        } catch (JSONException ex) {
            Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getCode() {
        return code;
    }

    
    public JSONObject getData() {
        return data;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Requete req = new Requete(this.code, new JSONObject(this.data));
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
    
    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        try {
            json.put("code",this.code);
        } catch (JSONException ex) {
            Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            json.put("data",this.data.toString());
        } catch (JSONException ex) {
            Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
        }
        return json;
    }
    
    public static Requete fromJSON(JSONObject json){
        Requete req = null;
        try {
            req =  new Requete(json.getInt("code"),new JSONObject(json.getString("data")));
        } catch (JSONException ex) {
            Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
        }
        return req;
    }
    
}
