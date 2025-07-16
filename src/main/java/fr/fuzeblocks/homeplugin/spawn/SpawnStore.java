package fr.fuzeblocks.homeplugin.spawn;

import org.bukkit.Location;

public interface SpawnStore {
    void setSpawn(Location location);
    Location getSpawn();
    void clearSpawn();
}
