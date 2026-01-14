package fr.fuzeblocks.homeplugin.warps;

import org.bukkit.Location;
import org.bukkit.Material;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * The interface Warp.
 */
public interface Warp {

    /**
     * Add warp boolean.
     *
     * @param name           the name
     * @param creatorName    the creator name
     * @param creatorUUID    the creator uuid
     * @param icon           the icon
     * @param lores          the lores
     * @param isPublic       the is public
     * @param allowedPlayers the allowed players
     * @param cost           the cost
     * @param permission     the permission
     * @param expirationDate the expiration date
     * @param creationDate   the creation date
     * @param location       the location
     * @return the boolean
     */
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
     *
     * @param serializedData the serialized data
     * @return the boolean
     */
    boolean addWarp(String serializedData);

    /**
     * Add warp boolean.
     *
     * @param warpData the warp data
     * @return the boolean
     */
    boolean addWarp(WarpData warpData);

    /**
     * Delete warp boolean.
     *
     * @param name the name
     * @return the boolean
     */
// Deletion
    boolean deleteWarp(String name);

    /**
     * Delete warp boolean.
     *
     * @param warpData the warp data
     * @return the boolean
     */
    boolean deleteWarp(WarpData warpData);

    /**
     * Rename warp boolean.
     *
     * @param oldName the old name
     * @param newName the new name
     * @return the boolean
     */
// Updates
    boolean renameWarp(String oldName, String newName);

    /**
     * Rename warp boolean.
     *
     * @param warpData the warp data
     * @param newName  the new name
     * @return the boolean
     */
    boolean renameWarp(WarpData warpData, String newName);

    /**
     * Relocate warp boolean.
     *
     * @param name        the name
     * @param newLocation the new location
     * @return the boolean
     */
    boolean relocateWarp(String name, Location newLocation);

    /**
     * Relocate warp boolean.
     *
     * @param warpData    the warp data
     * @param newLocation the new location
     * @return the boolean
     */
    boolean relocateWarp(WarpData warpData, Location newLocation);

    /**
     * Sets warp icon.
     *
     * @param name    the name
     * @param newIcon the new icon
     * @return the warp icon
     */
    boolean setWarpIcon(String name, Material newIcon);

    /**
     * Sets warp icon.
     *
     * @param warpData the warp data
     * @param newIcon  the new icon
     * @return the warp icon
     */
    boolean setWarpIcon(WarpData warpData, Material newIcon);

    /**
     * Sets warp lores.
     *
     * @param serializedData the serialized data
     * @param newLores       the new lores
     * @return the warp lores
     */
    boolean setWarpLores(String serializedData, List<String> newLores);

    /**
     * Sets warp lores.
     *
     * @param warpData the warp data
     * @param newLores the new lores
     * @return the warp lores
     */
    boolean setWarpLores(WarpData warpData, List<String> newLores);

    /**
     * Sets warp public.
     *
     * @param name     the name
     * @param isPublic the is public
     * @return the warp public
     */
    boolean setWarpPublic(String name, boolean isPublic);

    /**
     * Sets warp public.
     *
     * @param warpData the warp data
     * @param isPublic the is public
     * @return the warp public
     */
    boolean setWarpPublic(WarpData warpData, boolean isPublic);

    /**
     * Sets warp allowed players.
     *
     * @param name           the name
     * @param allowedPlayers the allowed players
     * @return the warp allowed players
     */
    boolean setWarpAllowedPlayers(String name, Set<UUID> allowedPlayers);

    /**
     * Sets warp allowed players.
     *
     * @param warpData       the warp data
     * @param allowedPlayers the allowed players
     * @return the warp allowed players
     */
    boolean setWarpAllowedPlayers(WarpData warpData, Set<UUID> allowedPlayers);

    /**
     * Sets warp cost.
     *
     * @param name the name
     * @param cost the cost
     * @return the warp cost
     */
    boolean setWarpCost(String name, double cost);

    /**
     * Sets warp cost.
     *
     * @param warpData the warp data
     * @param cost     the cost
     * @return the warp cost
     */
    boolean setWarpCost(WarpData warpData, double cost);

    /**
     * Sets warp permission.
     *
     * @param name       the name
     * @param permission the permission
     * @return the warp permission
     */
    boolean setWarpPermission(String name, String permission);

    /**
     * Sets warp permission.
     *
     * @param warpData   the warp data
     * @param permission the permission
     * @return the warp permission
     */
    boolean setWarpPermission(WarpData warpData, String permission);

    /**
     * Sets warp expiration date.
     *
     * @param name           the name
     * @param expirationDate the expiration date
     * @return the warp expiration date
     */
    boolean setWarpExpirationDate(String name, Timestamp expirationDate);

    /**
     * Sets warp expiration date.
     *
     * @param warpData       the warp data
     * @param expirationDate the expiration date
     * @return the warp expiration date
     */
    boolean setWarpExpirationDate(WarpData warpData, Timestamp expirationDate);

    /**
     * Warp exists boolean.
     *
     * @param name the name
     * @return the boolean
     */
// Queries
    boolean warpExists(String name);

    /**
     * Warp exists boolean.
     *
     * @param warpData the warp data
     * @return the boolean
     */
    boolean warpExists(WarpData warpData);

    /**
     * Get full warp data object by name, or null if not found.
     *
     * @param name the name
     * @return the warp
     */
    WarpData getWarp(String name);

    /**
     * Get a map of all warps by name.
     *
     * @return the all warps
     */
    Map<String, WarpData> getAllWarps();

    /**
     * Convenience: list only the names of all warps.
     *
     * @return the warp names
     */
    Set<String> getWarpNames();
}