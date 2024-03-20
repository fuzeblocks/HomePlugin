package fr.fuzeblocks.homeplugin.database;

import fr.fuzeblocks.homeplugin.home.HomeGetter;
import fr.fuzeblocks.homeplugin.home.sql.HomeManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
    private Connection connection;

    public CreateTable(Connection connection) {
        this.connection = connection;
        createHome();
        createSpawn();
    }

    public void createHome() {
        String request = "CREATE TABLE IF NOT EXISTS HomePlugin (player_uuid VARCHAR(36) NOT NULL, HOME_NAME VARCHAR(36), X INT(11), Y INT(11), Z INT(11), PITCH INT(11), YAW INT(11), WORLD VARCHAR(36), PRIMARY KEY(player_uuid, HOME_NAME))";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createSpawn() {
        String request = "CREATE TABLE IF NOT EXISTS SpawnPlugin (X INT(11), Y INT(11), Z INT(11), YAW INT(11), PITCH INT(11), WORLD VARCHAR(255))";
        try {
             PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        }
    }
