package fr.fuzeblocks.homeplugin.home.sql;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.database.DatabaseConnection;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HomeSQLManager {
    private final Connection connection = DatabaseConnection.getConnection();


    public boolean addHome(Player player, String name) {
        Location location = player.getLocation();
        return addHome(player.getUniqueId().toString(), name, location);
    }

    public List<Location> getHomesLocation(Player player) {
        List<Location> homes = new ArrayList<>();
        List<String> homeNames = getHomesName(player);
        for (String homeName : homeNames) {
            Location homeLocation = getHomeLocation(player, homeName);
            if (homeLocation != null) {
                homes.add(homeLocation);
            }
        }
        return homes;
    }

    public int getHomeNumber(Player player) {
        String request = "SELECT COUNT(*) FROM HomePlugin WHERE player_uuid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, player.getUniqueId().toString());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<String> getHomesName(Player player) {
        List<String> homeNames = new ArrayList<>();
        String request = "SELECT HOME_NAME FROM HomePlugin WHERE player_uuid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, player.getUniqueId().toString());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    homeNames.add(resultSet.getString("HOME_NAME"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return homeNames;
    }

    private boolean addHome(String playerUUID, String name, Location location) {
        String request = "INSERT INTO HomePlugin (player_uuid, HOME_NAME, X, Y, Z, PITCH, YAW, WORLD) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, playerUUID);
            preparedStatement.setString(2, name);
            preparedStatement.setDouble(3, location.getX());
            preparedStatement.setDouble(4, location.getY());
            preparedStatement.setDouble(5, location.getZ());
            preparedStatement.setFloat(6, location.getPitch());
            preparedStatement.setFloat(7, location.getYaw());
            preparedStatement.setString(8, location.getWorld().getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public CacheManager getCacheManager() {
        return HomePlugin.getCacheManager();
    }

    public Location getHomeLocation(Player player, String homeName) {
        String request = "SELECT * FROM HomePlugin WHERE player_uuid = ? AND HOME_NAME = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, homeName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String worldName = resultSet.getString("WORLD");
                    double x = resultSet.getDouble("X");
                    double y = resultSet.getDouble("Y");
                    double z = resultSet.getDouble("Z");
                    float yaw = resultSet.getFloat("YAW");
                    float pitch = resultSet.getFloat("PITCH");
                    return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteHome(Player player, String homeName) {
        String request = "DELETE FROM HomePlugin WHERE player_uuid = ? AND HOME_NAME = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, homeName);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean isStatus(Player player) {
        return StatusManager.getPlayerStatus(player);
    }

    public boolean exist(Player player, String homeName) {
        String sql = "SELECT * FROM HomePlugin WHERE player_uuid = ? AND HOME_NAME = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, player.getUniqueId().toString());
            pstmt.setString(2, homeName);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }
}
