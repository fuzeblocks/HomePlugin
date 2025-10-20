package fr.fuzeblocks.homeplugin.spawn;

import org.bukkit.Location;

/**
 * The interface Spawn request store.
 */
public interface SpawnRequestStore {
    /**
     * Gets spawn.
     *
     * @return the spawn
     */
    Location getSpawn();

    /**
     * Sets spawn.
     *
     * @param location the location
     */
    void setSpawn(Location location);

    /**
     * Clear spawn.
     */
    void clearSpawn();
}
