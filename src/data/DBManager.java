package data;

import org.json.JSONException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Kévin DUPONT on 13/10/2019.
 */

public class DBManager {

    private static Config cfg = new Config("database.json");
    private Connection connection;

    public Connection getConnection() throws SQLException {
        if (isConnected()) {
            return this.connection;
        } else {
            disconnect();
            connect();
            return this.connection;
        }
    }

    public void connect() throws SQLException, JSONException {
        String host = cfg.getString("host");
        int port = cfg.getInt("port");
        String user = cfg.getString("user");
        String password = cfg.getString("password");
        String database = cfg.getString("database");
        String sqlhost = "jdbc:mysql://" + host + ":" + port + "/" + database + "?verifyServerCertificate=false&useSSL=true";

        this.connection = DriverManager.getConnection(sqlhost, user, password);
    }

    public boolean isConnected() {
        try {
            return (this.connection != null) && (!this.connection.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void refreshConnection() {
        System.out.println("Refresh de la connection à la base de données.");
        try {
            if (isConnected()) {
                disconnect();
                connect();
            } else {
                connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}