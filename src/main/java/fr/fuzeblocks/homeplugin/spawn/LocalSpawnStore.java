package fr.fuzeblocks.homeplugin.spawn;

import org.bukkit.Location;

/**
 * The type Local spawn store.
 */
public class LocalSpawnStore implements SpawnRequestStore {
    private Location spawnLocation;

    @Override
    public void setSpawn(Location location) {
        this.spawnLocation = location;
    }

    @Override
    public Location getSpawn() {
        return spawnLocation;
    }

    @Override
    public void clearSpawn() {
        spawnLocation = null;
    }
}
