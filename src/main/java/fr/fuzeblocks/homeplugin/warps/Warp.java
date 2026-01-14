package fr.fuzeblocks.homeplugin.warps;

import org.bukkit.Location;
import org.bukkit.Material;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface Warp {

    // Creation
    boolean addWarp(String name,
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
                    Location location);

    /**
     * Parse and add a warp from serialized data (e.g. from a file/DB).
     * The exact format is implementation-dependent.
     */
    boolean addWarp(String serializedData);

    boolean addWarp(WarpData warpData);

    // Deletion
    boolean deleteWarp(String name);
    boolean deleteWarp(WarpData warpData);

    // Updates
    boolean renameWarp(String oldName, String newName);
    boolean renameWarp(WarpData warpData, String newName);

    boolean relocateWarp(String name, Location newLocation);
    boolean relocateWarp(WarpData warpData, Location newLocation);

    boolean setWarpIcon(String name, Material newIcon);
    boolean setWarpIcon(WarpData warpData, Material newIcon);

    boolean setWarpLores(String serializedData, List<String> newLores);
    boolean setWarpLores(WarpData warpData, List<String> newLores);

    boolean setWarpPublic(String name, boolean isPublic);
    boolean setWarpPublic(WarpData warpData, boolean isPublic);

    boolean setWarpAllowedPlayers(String name, Set<UUID> allowedPlayers);
    boolean setWarpAllowedPlayers(WarpData warpData, Set<UUID> allowedPlayers);

    boolean setWarpCost(String name, double cost);
    boolean setWarpCost(WarpData warpData, double cost);

    boolean setWarpPermission(String name, String permission);
    boolean setWarpPermission(WarpData warpData, String permission);

    boolean setWarpExpirationDate(String name, Timestamp expirationDate);
    boolean setWarpExpirationDate(WarpData warpData, Timestamp expirationDate);

    // Queries
    boolean warpExists(String name);
    boolean warpExists(WarpData warpData);

    /**
     * Get full warp data object by name, or null if not found.
     */
    WarpData getWarp(String name);

    /**
     * Get a map of all warps by name.
     */
    Map<String, WarpData> getAllWarps();

    /**
     * Convenience: list only the names of all warps.
     */
    Set<String> getWarpNames();
}