package fr.fuzeblocks.homeplugin.back;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Back manager.
 */
public class BackManager {

    private static BackManager instance = null;
    private final Map<UUID, Location> lastLocations = new ConcurrentHashMap<>();


    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static BackManager getInstance() {
        if (instance == null) {
            instance = new BackManager();
        }
        return instance;
    }

    /**
     * Sets last location.
     *
     * @param player   the player
     * @param location the location
     */
    public void setLastLocation(Player player, Location location) {
        if (player == null || location == null) return;
        lastLocations.put(player.getUniqueId(), location.clone());
    }

    /**
     * Gets last location.
     *
     * @param player the player
     * @return the last location
     */
    public Optional<Location> getLastLocation(Player player) {
        if (player == null) return Optional.empty();
        return Optional.ofNullable(lastLocations.get(player.getUniqueId()));
    }

    /**
     * Has last location boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public boolean hasLastLocation(Player player) {
        if (player == null) return false;
        return lastLocations.containsKey(player.getUniqueId());
    }

    /**
     * Clear.
     *
     * @param player the player
     */
    public void clear(Player player) {
        if (player == null) return;
        lastLocations.remove(player.getUniqueId());
    }
}