package data;





import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import org.json.JSONObject;

public class Config {

    private JSONObject objet;

    public Config(String filename) {
        // Ouverture du fichier
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            System.err.println("Fichier '" + filename + "' introuvable");
            System.exit(-1);
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
            System.exit(-1);
        }

        // Création d'un objet JSON
        objet = new JSONObject(json);

    }

    public String getString(String key) {
        return objet.getString(key);
    }

    public int getInt(String key) {
        return objet.getInt(key);
    }

}
