package fr.fuzeblocks.homeplugin.database;

import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DbConnection {
    private static DbCredentials credentials;
    private static Connection connection;
    public DbConnection(DbCredentials credentials) {
        DbConnection.credentials = credentials;
        connect();
    }

    public static void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(credentials.toURI(),credentials.getUser(),credentials.getPass());
            Logger.getLogger("HomePlugin").warning("Successfully connected to DB.");
        } catch (SQLException | ClassNotFoundException e) {
           System.err.println("Error when connecting to DB ! Exiting...");
            Bukkit.getPluginManager().disablePlugin(HomePlugin.getPlugin(HomePlugin.class));

        }
    }

    public void close() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Connection getConnection()  {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    return connection;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        connect();
        return connection;
    }
}
