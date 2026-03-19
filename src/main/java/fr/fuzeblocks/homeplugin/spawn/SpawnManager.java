package fr.fuzeblocks.homeplugin.spawn;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * The type Spawn manager.
 */
public class SpawnManager implements Spawn {

    private static SpawnManager instance = null;
    private final Spawn spawnImplementation;

    private SpawnManager() {
        if (isYAML()) {
            this.spawnImplementation = HomePlugin.getSpawnYMLManager();
        } else {
            this.spawnImplementation = HomePlugin.getSpawnSQLManager();
        }
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static SpawnManager getInstance() {
        if (instance == null) {
            instance = new SpawnManager();
        }
        return instance;
    }

    @Override
    public boolean setSpawn(Location location) {
        return spawnImplementation.setSpawn(location);
    }

    @Override
    public Location getSpawn(World world) {
        return spawnImplementation.getSpawn(world);
    }

    @Override
    public boolean hasSpawn(World world) {
        return spawnImplementation.hasSpawn(world);
    }

    @Override
    public boolean removeSpawn(World world) {
        return spawnImplementation.removeSpawn(world);
    }

    @Override
    public boolean isStatus(Player player) {
        return StatusManager.getPlayerStatus(player);
    }
}