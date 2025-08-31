package fr.fuzeblocks.homeplugin.spawn;

import org.bukkit.Location;

public interface SpawnRequestStore {
    void setSpawn(Location location);
    Location getSpawn();
    void clearSpawn();
}
