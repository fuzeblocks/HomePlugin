package fr.fuzeblocks.homeplugin.warps;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * The type Warp manager.
 */
public class WarpManager implements Warp {

    private static WarpManager instance = null;
    private final Warp warpImplementation;

    private WarpManager() {
        if (isYAML()) {
            this.warpImplementation = HomePlugin.getWarpYMLManager();
        } else {
            this.warpImplementation = HomePlugin.getWarpSQLManager();
        }
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static WarpManager getInstance() {
        if (instance == null) {
            instance = new WarpManager();
        }
        return instance;
    }

    // --- Helpers de config (comme pour HomeManager) ---

    private boolean isYAML() {
        // Adapte la clé de config à ton plugin
        return HomePlugin.getConfigurationSection().getBoolean("Config.Connector.Storage.UseYAML");
    }

    public CacheManager getCacheManager() {
        return HomePlugin.getCacheManager();
    }

    public boolean isStatus(Player player) {
        return StatusManager.getPlayerStatus(player);
    }

    // --- Implémentation de l'interface Warp (délégation) ---

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
        return warpImplementation.addWarp(name, creatorName, creatorUUID, icon, lores, isPublic,
                allowedPlayers, cost, permission, expirationDate, creationDate, location);
    }

    @Override
    public boolean addWarp(String serializedData) {
        return warpImplementation.addWarp(serializedData);
    }

    @Override
    public boolean addWarp(WarpData warpData) {
        return warpImplementation.addWarp(warpData);
    }

    @Override
    public boolean deleteWarp(String name) {
        return warpImplementation.deleteWarp(name);
    }

    @Override
    public boolean deleteWarp(WarpData warpData) {
        return warpImplementation.deleteWarp(warpData);
    }

    @Override
    public boolean renameWarp(String oldName, String newName) {
        return warpImplementation.renameWarp(oldName, newName);
    }

    @Override
    public boolean renameWarp(WarpData warpData, String newName) {
        return warpImplementation.renameWarp(warpData, newName);
    }

    @Override
    public boolean relocateWarp(String name, Location newLocation) {
        return warpImplementation.relocateWarp(name, newLocation);
    }

    @Override
    public boolean relocateWarp(WarpData warpData, Location newLocation) {
        return warpImplementation.relocateWarp(warpData, newLocation);
    }

    @Override
    public boolean setWarpIcon(String name, Material newIcon) {
        return warpImplementation.setWarpIcon(name, newIcon);
    }

    @Override
    public boolean setWarpIcon(WarpData warpData, Material newIcon) {
        return warpImplementation.setWarpIcon(warpData, newIcon);
    }

    @Override
    public boolean setWarpLores(String serializedData, List<String> newLores) {
        return warpImplementation.setWarpLores(serializedData, newLores);
    }

    @Override
    public boolean setWarpLores(WarpData warpData, List<String> newLores) {
        return warpImplementation.setWarpLores(warpData, newLores);
    }

    @Override
    public boolean setWarpPublic(String name, boolean isPublic) {
        return warpImplementation.setWarpPublic(name, isPublic);
    }

    @Override
    public boolean setWarpPublic(WarpData warpData, boolean isPublic) {
        return warpImplementation.setWarpPublic(warpData, isPublic);
    }

    @Override
    public boolean setWarpAllowedPlayers(String name, Set<UUID> allowedPlayers) {
        return warpImplementation.setWarpAllowedPlayers(name, allowedPlayers);
    }

    @Override
    public boolean setWarpAllowedPlayers(WarpData warpData, Set<UUID> allowedPlayers) {
        return warpImplementation.setWarpAllowedPlayers(warpData, allowedPlayers);
    }

    @Override
    public boolean setWarpCost(String name, double cost) {
        return warpImplementation.setWarpCost(name, cost);
    }

    @Override
    public boolean setWarpCost(WarpData warpData, double cost) {
        return warpImplementation.setWarpCost(warpData, cost);
    }

    @Override
    public boolean setWarpPermission(String name, String permission) {
        return warpImplementation.setWarpPermission(name, permission);
    }

    @Override
    public boolean setWarpPermission(WarpData warpData, String permission) {
        return warpImplementation.setWarpPermission(warpData, permission);
    }

    @Override
    public boolean setWarpExpirationDate(String name, Timestamp expirationDate) {
        return warpImplementation.setWarpExpirationDate(name, expirationDate);
    }

    @Override
    public boolean setWarpExpirationDate(WarpData warpData, Timestamp expirationDate) {
        return warpImplementation.setWarpExpirationDate(warpData, expirationDate);
    }

    @Override
    public boolean warpExists(String name) {
        return warpImplementation.warpExists(name);
    }

    @Override
    public boolean warpExists(WarpData warpData) {
        return warpImplementation.warpExists(warpData);
    }

    @Override
    public WarpData getWarp(String name) {
        return warpImplementation.getWarp(name);
    }

    @Override
    public Map<String, WarpData> getAllWarps() {
        return warpImplementation.getAllWarps();
    }

    @Override
    public Set<String> getWarpNames() {
        return warpImplementation.getWarpNames();
    }
}