package fr.fuzeblocks.homeplugin.home.offline.sql;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.database.DatabaseConnection;
import fr.fuzeblocks.homeplugin.home.offline.OfflineHome;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The type Home offline sql manager.
 */
public class HomeOfflineSQLManager implements OfflineHome {

    private final Connection connection = DatabaseConnection.getConnection();

    @Override
    public boolean setHome(UUID uuid, String name, Location location) {
        return addHome(uuid.toString(), name, location);
    }

    @Override
    public boolean renameHome(UUID uuid, String oldHomeName, String newHomeName) {
        Location homeLocation = getHomeLocation(uuid, oldHomeName);
        if (homeLocation != null) {
            if (deleteHome(uuid, oldHomeName)) {
                return addHome(uuid.toString(), newHomeName, homeLocation);
            }
        }
        return false;
    }

    @Override
    public boolean relocateHome(UUID uuid, String homeName, Location newLocation) {
        if (deleteHome(uuid, homeName)) {
            return addHome(uuid.toString(), homeName, newLocation);
        }
        return false;
    }

    @Override
    public List<Location> getHomesLocation(UUID uuid) {
        List<Location> homes = new ArrayList<>();
        List<String> homeNames = getHomesName(uuid);
        for (String homeName : homeNames) {
            Location homeLocation = getHomeLocation(uuid, homeName);
            if (homeLocation != null) {
                homes.add(homeLocation);
            }
        }
        return homes;
    }

    @Override
    public int getHomeNumber(UUID uuid) {
        String request = "SELECT COUNT(*) FROM HomePlugin WHERE player_uuid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, uuid.toString());
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

    @Override
    public List<String> getHomesName(UUID uuid) {
        List<String> homeNames = new ArrayList<>();
        String request = "SELECT HOME_NAME FROM HomePlugin WHERE player_uuid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, uuid.toString());
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

    private boolean addHome(String playerUUID, String name, @NotNull Location location) {
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

    @Override
    public CacheManager getCacheManager() {
        return HomePlugin.getCacheManager();
    }

    @Override
    public Location getHomeLocation(UUID uuid, String homeName) {
        String request = "SELECT * FROM HomePlugin WHERE player_uuid = ? AND HOME_NAME = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, uuid.toString());
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

    @Override
    public boolean deleteHome(UUID uuid, String homeName) {
        String request = "DELETE FROM HomePlugin WHERE player_uuid = ? AND HOME_NAME = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, homeName);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean exist(UUID uuid, String homeName) {
        String sql = "SELECT * FROM HomePlugin WHERE player_uuid = ? AND HOME_NAME = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, uuid.toString());
            pstmt.setString(2, homeName);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }
}
