package fr.fuzeblocks.homeplugin.database;

import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.configuration.ConfigurationSection;

public class DatabaseManager {
    private static DatabaseConnection connection;

    private final String key = "Config.Connector.";

    public DatabaseManager(HomePlugin instance) {
        System.out.println(key);
        ConfigurationSection config = instance.getConfig();
        connection = new DatabaseConnection(new DatabaseCredentials(config.getString(key + "HOST"), config.getString(key + "USERNAME"), config.getString(key + "PASSWORD"), config.getString(key + "DATABASE"), config.getInt(key + "PORT")));
    }

    public void close() {
        connection.close();
    }

    public DatabaseConnection getConnection() {
        return connection;
    }
}
