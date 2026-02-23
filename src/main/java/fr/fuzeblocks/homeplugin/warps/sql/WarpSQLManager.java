package fr.fuzeblocks.homeplugin.warps.sql;

import fr.fuzeblocks.homeplugin.database.DatabaseConnection;
import fr.fuzeblocks.homeplugin.warps.Warp;
import fr.fuzeblocks.homeplugin.warps.WarpData;
import org.bukkit.Location;
import org.bukkit.Material;

import java.sql.*;
import java.util.*;

/**
 * The type Warp sql manager.
 */
public class WarpSQLManager implements Warp {

    private final Connection connection = DatabaseConnection.getConnection();


    // ---------- Helpers ----------

    private boolean saveWarpInternal(WarpData warpData) {
        if (warpData == null || warpData.getName() == null || warpData.getName().isEmpty()) return false;

        String sql =
                "INSERT INTO Warps (WARP_NAME, DATA) VALUES (?, ?) " +
                        "ON DUPLICATE KEY UPDATE DATA = VALUES(DATA)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, warpData.getName());
            ps.setString(2, warpData.serialize());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private WarpData loadWarpInternal(String name) {
        if (name == null || name.isEmpty()) return null;

        String sql = "SELECT DATA FROM Warps WHERE WARP_NAME = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                String serialized = rs.getString("DATA");
                if (!WarpData.isValidSerializedData(serialized)) return null;

                return WarpData.deserialize(serialized);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean deleteWarpInternal(String name) {
        if (name == null || name.isEmpty()) return false;

        String sql = "DELETE FROM Warps WHERE WARP_NAME = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------- Creation ----------

    @Override
    public boolean addWarp(String name,
                           String creatorName,
                           UUID creatorUUID,
                           Material icon,
                           List<String> lores,
                           boolean isPublic,
                           Set<UUID> allowedPlayers,
                           double cost,
                           String permission,
                           Timestamp expirationDate,
                           Timestamp creationDate,
                           Location location) {

        WarpData warpData = new WarpData(
                name,
                creatorName,
                creatorUUID,
                icon,
                lores,
                isPublic,
                allowedPlayers,
                cost,
                permission,
                expirationDate,
                creationDate,
                location
        );
        return saveWarpInternal(warpData);
    }

    @Override
    public boolean addWarp(String serializedData) {
        if (!WarpData.isValidSerializedData(serializedData)) return false;
        WarpData data = WarpData.deserialize(serializedData);
        return saveWarpInternal(data);
    }

    @Override
    public boolean addWarp(WarpData warpData) {
        return saveWarpInternal(warpData);
    }

    // ---------- Deletion ----------

    @Override
    public boolean deleteWarp(String name) {
        return deleteWarpInternal(name);
    }

    @Override
    public boolean deleteWarp(WarpData warpData) {
        if (warpData == null) return false;
        return deleteWarpInternal(warpData.getName());
    }

    // ---------- Rename ----------

    @Override
    public boolean renameWarp(String oldName, String newName) {
        if (oldName == null || newName == null || oldName.isEmpty() || newName.isEmpty()) return false;

        WarpData data = loadWarpInternal(oldName);
        if (data == null) return false;

        // Créer un nouveau WarpData avec le nom mis à jour
        WarpData renamed = new WarpData(
                newName,
                data.getCreatorName(),
                data.getCreatorUUID(),
                data.getIcon(),
                data.getLores(),
                data.isPublic(),
                data.getAllowedPlayers(),
                data.getCost(),
                data.getPermission(),
                data.getExpirationDate(),
                data.getCreationDate(),
                data.getLocation()
        );

        // Supprimer l'ancien et sauver le nouveau
        if (!deleteWarpInternal(oldName)) return false;
        return saveWarpInternal(renamed);
    }

    @Override
    public boolean renameWarp(WarpData warpData, String newName) {
        if (warpData == null) return false;
        return renameWarp(warpData.getName(), newName);
    }

    // ---------- Relocate ----------

    @Override
    public boolean relocateWarp(String name, Location newLocation) {
        if (newLocation == null) return false;
        WarpData data = loadWarpInternal(name);
        if (data == null) return false;

        WarpData updated = new WarpData(
                data.getName(),
                data.getCreatorName(),
                data.getCreatorUUID(),
                data.getIcon(),
                data.getLores(),
                data.isPublic(),
                data.getAllowedPlayers(),
                data.getCost(),
                data.getPermission(),
                data.getExpirationDate(),
                data.getCreationDate(),
                newLocation
        );

        return saveWarpInternal(updated);
    }

    @Override
    public boolean relocateWarp(WarpData warpData, Location newLocation) {
        if (warpData == null) return false;
        return relocateWarp(warpData.getName(), newLocation);
    }

    // ---------- Icon ----------

    @Override
    public boolean setWarpIcon(String name, Material newIcon) {
        if (newIcon == null) return false;
        WarpData data = loadWarpInternal(name);
        if (data == null) return false;

        WarpData updated = new WarpData(
                data.getName(),
                data.getCreatorName(),
                data.getCreatorUUID(),
                newIcon,
                data.getLores(),
                data.isPublic(),
                data.getAllowedPlayers(),
                data.getCost(),
                data.getPermission(),
                data.getExpirationDate(),
                data.getCreationDate(),
                data.getLocation()
        );

        return saveWarpInternal(updated);
    }

    @Override
    public boolean setWarpIcon(WarpData warpData, Material newIcon) {
        if (warpData == null) return false;
        return setWarpIcon(warpData.getName(), newIcon);
    }

    // ---------- Lores ----------

    @Override
    public boolean setWarpLores(String serializedData, List<String> newLores) {
        if (!WarpData.isValidSerializedData(serializedData)) return false;
        WarpData data = WarpData.deserialize(serializedData);
        return setWarpLores(data, newLores);
    }

    @Override
    public boolean setWarpLores(WarpData warpData, List<String> newLores) {
        if (warpData == null) return false;

        WarpData updated = new WarpData(
                warpData.getName(),
                warpData.getCreatorName(),
                warpData.getCreatorUUID(),
                warpData.getIcon(),
                newLores,
                warpData.isPublic(),
                warpData.getAllowedPlayers(),
                warpData.getCost(),
                warpData.getPermission(),
                warpData.getExpirationDate(),
                warpData.getCreationDate(),
                warpData.getLocation()
        );

        return saveWarpInternal(updated);
    }

    // ---------- Public ----------

    @Override
    public boolean setWarpPublic(String name, boolean isPublic) {
        WarpData data = loadWarpInternal(name);
        if (data == null) return false;

        WarpData updated = new WarpData(
                data.getName(),
                data.getCreatorName(),
                data.getCreatorUUID(),
                data.getIcon(),
                data.getLores(),
                isPublic,
                data.getAllowedPlayers(),
                data.getCost(),
                data.getPermission(),
                data.getExpirationDate(),
                data.getCreationDate(),
                data.getLocation()
        );

        return saveWarpInternal(updated);
    }

    @Override
    public boolean setWarpPublic(WarpData warpData, boolean isPublic) {
        if (warpData == null) return false;
        return setWarpPublic(warpData.getName(), isPublic);
    }

    // ---------- Allowed players ----------

    @Override
    public boolean setWarpAllowedPlayers(String name, Set<UUID> allowedPlayers) {
        WarpData data = loadWarpInternal(name);
        if (data == null) return false;

        WarpData updated = new WarpData(
                data.getName(),
                data.getCreatorName(),
                data.getCreatorUUID(),
                data.getIcon(),
                data.getLores(),
                data.isPublic(),
                allowedPlayers,
                data.getCost(),
                data.getPermission(),
                data.getExpirationDate(),
                data.getCreationDate(),
                data.getLocation()
        );

        return saveWarpInternal(updated);
    }

    @Override
    public boolean setWarpAllowedPlayers(WarpData warpData, Set<UUID> allowedPlayers) {
        if (warpData == null) return false;
        return setWarpAllowedPlayers(warpData.getName(), allowedPlayers);
    }

    // ---------- Cost ----------

    @Override
    public boolean setWarpCost(String name, double cost) {
        WarpData data = loadWarpInternal(name);
        if (data == null) return false;

        WarpData updated = new WarpData(
                data.getName(),
                data.getCreatorName(),
                data.getCreatorUUID(),
                data.getIcon(),
                data.getLores(),
                data.isPublic(),
                data.getAllowedPlayers(),
                cost,
                data.getPermission(),
                data.getExpirationDate(),
                data.getCreationDate(),
                data.getLocation()
        );

        return saveWarpInternal(updated);
    }

    @Override
    public boolean setWarpCost(WarpData warpData, double cost) {
        if (warpData == null) return false;
        return setWarpCost(warpData.getName(), cost);
    }

    // ---------- Permission ----------

    @Override
    public boolean setWarpPermission(String name, String permission) {
        WarpData data = loadWarpInternal(name);
        if (data == null) return false;

        WarpData updated = new WarpData(
                data.getName(),
                data.getCreatorName(),
                data.getCreatorUUID(),
                data.getIcon(),
                data.getLores(),
                data.isPublic(),
                data.getAllowedPlayers(),
                data.getCost(),
                permission,
                data.getExpirationDate(),
                data.getCreationDate(),
                data.getLocation()
        );

        return saveWarpInternal(updated);
    }

    @Override
    public boolean setWarpPermission(WarpData warpData, String permission) {
        if (warpData == null) return false;
        return setWarpPermission(warpData.getName(), permission);
    }

    // ---------- Expiration date ----------

    @Override
    public boolean setWarpExpirationDate(String name, Timestamp expirationDate) {
        WarpData data = loadWarpInternal(name);
        if (data == null) return false;

        WarpData updated = new WarpData(
                data.getName(),
                data.getCreatorName(),
                data.getCreatorUUID(),
                data.getIcon(),
                data.getLores(),
                data.isPublic(),
                data.getAllowedPlayers(),
                data.getCost(),
                data.getPermission(),
                expirationDate,
                data.getCreationDate(),
                data.getLocation()
        );

        return saveWarpInternal(updated);
    }

    @Override
    public boolean setWarpExpirationDate(WarpData warpData, Timestamp expirationDate) {
        if (warpData == null) return false;
        return setWarpExpirationDate(warpData.getName(), expirationDate);
    }

    // ---------- Queries ----------

    @Override
    public boolean warpExists(String name) {
        if (name == null || name.isEmpty()) return false;

        String sql = "SELECT 1 FROM Warps WHERE WARP_NAME = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean warpExists(WarpData warpData) {
        if (warpData == null) return false;
        return warpExists(warpData.getName());
    }

    @Override
    public WarpData getWarp(String name) {
        return loadWarpInternal(name);
    }

    @Override
    public Map<String, WarpData> getAllWarps() {
        Map<String, WarpData> result = new HashMap<>();
        String sql = "SELECT WARP_NAME, DATA FROM Warps";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("WARP_NAME");
                String serialized = rs.getString("DATA");

                if (!WarpData.isValidSerializedData(serialized)) continue;

                WarpData data = WarpData.deserialize(serialized);
                if (data != null) {
                    result.put(name, data);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Collections.unmodifiableMap(result);
    }

    @Override
    public Set<String> getWarpNames() {
        Set<String> names = new HashSet<>();
        String sql = "SELECT WARP_NAME FROM Warps";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                names.add(rs.getString("WARP_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Collections.unmodifiableSet(names);
    }
    @Override
    public boolean isExpired(String name) {
        WarpData data = loadWarpInternal(name);
        if (data == null) return false;

        Timestamp expiration = data.getExpirationDate();
        if (expiration == null) return false;

        return expiration.before(new Timestamp(System.currentTimeMillis()));
    }
    @Override
    public boolean isExpired(WarpData warpData) {
        if (warpData == null) return false;
        return isExpired(warpData.getName());
    }
}