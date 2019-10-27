package data;





import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class Config {
    private JSONObject objet = null;

    public Config(String filename) {
        // Ouverture du fichier
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            System.err.println("Fichier '" + filename + "' introuvable");
            return;
        }

        // Récupération de la chaîne JSON depuis le fichier
        String json = "";
        Scanner scanner = new Scanner(fs);
        while (scanner.hasNext())
            json = json.concat(scanner.nextLine());
        scanner.close();
        json = json.replaceAll("[\t ]", "");

        // Fermeture du fichier
        try {
            fs.close();
        } catch (IOException e) {
            System.err.println("Erreur lors de la fermeture du fichier." + e);
            return;
        }

        // Création d'un objet JSON
        objet = new JSONObject(json);
    }

    public String getString(String key) {
        if (objet == null)
            throw new JSONException("JSONObject is not initialized");

        return objet.getString(key);
    }

    public int getInt(String key) {
        if (objet == null)
            throw new JSONException("JSONObject is not initialized");

        return objet.getInt(key);
    }

}
