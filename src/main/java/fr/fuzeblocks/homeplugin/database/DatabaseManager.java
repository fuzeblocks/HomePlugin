package fr.fuzeblocks.homeplugin.database;

import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.configuration.ConfigurationSection;

/**
 * The type Database manager.
 */
public class DatabaseManager {
    private static DatabaseConnection connection;

    private final String key = "Config.Connector.";

    /**
     * Instantiates a new Database manager.
     *
     * @param instance the instance
     */
    public DatabaseManager(HomePlugin instance) {
        System.out.println(key);
        ConfigurationSection config = instance.getConfig();
        connection = new DatabaseConnection(new DatabaseCredentials(config.getString(key + "HOST"), config.getString(key + "USERNAME"), config.getString(key + "PASSWORD"), config.getString(key + "DATABASE"), config.getInt(key + "PORT")));
    }

    /**
     * Close.
     */
    public void close() {
        connection.close();
    }

    /**
     * Gets connection.
     *
     * @return the connection
     */
    public DatabaseConnection getConnection() {
        return connection;
    }
}
