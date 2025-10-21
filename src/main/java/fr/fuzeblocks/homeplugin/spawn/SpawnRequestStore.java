package fr.fuzeblocks.homeplugin.spawn;

import org.bukkit.Location;

/**
 * The interface Spawn request store.
 */
public interface SpawnRequestStore {
    /**
     * Sets spawn.
     *
     * @param location the location
     */
    void setSpawn(Location location);

    /**
     * Gets spawn.
     *
     * @return the spawn
     */
    Location getSpawn();

    /**
     * Clear spawn.
     */
    void clearSpawn();
}
