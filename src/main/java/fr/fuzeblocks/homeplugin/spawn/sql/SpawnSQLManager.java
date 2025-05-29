package fr.fuzeblocks.homeplugin.spawn.sql;

import fr.fuzeblocks.homeplugin.database.DatabaseConnection;
import fr.fuzeblocks.homeplugin.spawn.Spawn;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SpawnSQLManager implements Spawn {
    private final Connection connection = DatabaseConnection.getConnection();

    public boolean setSpawn(Location location) {
        String request = "INSERT INTO SpawnPlugin (X, Y, Z, YAW, PITCH, WORLD) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.setDouble(1, location.getX());
            preparedStatement.setDouble(2, location.getY());
            preparedStatement.setDouble(3, location.getZ());
            preparedStatement.setFloat(4, location.getYaw());
            preparedStatement.setFloat(5, location.getPitch());
            preparedStatement.setString(6, location.getWorld().getName());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Location getSpawn(World world) {
        String request = "SELECT * FROM `SpawnPlugin` WHERE `WORLD` = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, world.getName());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String worldName = resultSet.getString("WORLD");
                double x = resultSet.getDouble("X");
                double y = resultSet.getDouble("Y");
                double z = resultSet.getDouble("Z");
                float yaw = resultSet.getFloat("YAW");
                float pitch = resultSet.getFloat("PITCH");
                return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean hasSpawn(World world) {
        return getSpawn(world) != null;
    }

    public boolean removeSpawn(World world) {
        String request = "DELETE FROM `SpawnPlugin` WHERE `WORLD` = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, world.getName());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isStatus(Player player) {
        return StatusManager.getPlayerStatus(player);
    }
}
