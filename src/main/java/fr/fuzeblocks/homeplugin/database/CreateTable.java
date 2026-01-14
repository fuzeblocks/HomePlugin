package fr.fuzeblocks.homeplugin.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The type Create table.
 */
public class CreateTable {
    private final Connection connection;

    /**
     * Instantiates a new Create table.
     *
     * @param connection the connection
     */
    public CreateTable(Connection connection) {
        this.connection = connection;
        createHome();
        createSpawn();
        createWarps();
    }

    /**
     * Create home.
     */
    public void createHome() {
        String request = "CREATE TABLE IF NOT EXISTS HomePlugin (player_uuid VARCHAR(36) NOT NULL, HOME_NAME VARCHAR(36), X INT(11), Y INT(11), Z INT(11), PITCH INT(11), YAW INT(11), WORLD VARCHAR(36), PRIMARY KEY(player_uuid, HOME_NAME))";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create spawn.
     */
    public void createSpawn() {
        String request = "CREATE TABLE IF NOT EXISTS SpawnPlugin (X INT(11), Y INT(11), Z INT(11), YAW INT(11), PITCH INT(11), WORLD VARCHAR(255))";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createWarps() {
        String request =
                "CREATE TABLE IF NOT EXISTS Warps (" +
                        "  WARP_NAME VARCHAR(64) NOT NULL," +
                        "  DATA TEXT NOT NULL," +
                        "  PRIMARY KEY (WARP_NAME)" +
                        ")";

        try (PreparedStatement preparedStatement = connection.prepareStatement(request)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
