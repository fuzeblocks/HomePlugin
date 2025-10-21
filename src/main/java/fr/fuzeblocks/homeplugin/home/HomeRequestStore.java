package fr.fuzeblocks.homeplugin.home;

import org.bukkit.Location;

import java.util.Map;
import java.util.UUID;

/**
 * The interface Home request store.
 */
public interface HomeRequestStore {
    /**
     * Add home.
     *
     * @param playerId the player id
     * @param homeName the home name
     * @param location the location
     */
    void addHome(UUID playerId, String homeName, Location location);

    /**
     * Remove home.
     *
     * @param playerId the player id
     * @param homeName the home name
     */
    void removeHome(UUID playerId, String homeName);

    /**
     * Gets homes.
     *
     * @param playerId the player id
     * @return the homes
     */
    Map<String, Location> getHomes(UUID playerId);

    /**
     * Clear homes.
     *
     * @param playerId the player id
     */
    void clearHomes(UUID playerId);

    /**
     * Clear all homes.
     */
    void clearAllHomes();
}
