package fr.fuzeblocks.homeplugin.spawn;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.spawn.sql.SpawnSQLManager;
import fr.fuzeblocks.homeplugin.spawn.yml.SpawnYMLManager;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;


/**
 * The type Spawn manager.
 */
public class SpawnManager implements Spawn {

    private static SpawnManager instance = null;
    private final SpawnYMLManager spawnYMLManager = HomePlugin.getSpawnYMLManager();
    private final SpawnSQLManager spawnSQLManager = HomePlugin.getSpawnSQLManager();

    private SpawnManager() {
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

    public boolean setSpawn(Location location) {
        if (isYAML()) {
            return spawnYMLManager.setSpawn(location);
        } else {
            return spawnSQLManager.setSpawn(location);
        }
    }

    public Location getSpawn(World world) {
        if (isYAML()) {
            return spawnYMLManager.getSpawn(world);
        } else {
            return spawnSQLManager.getSpawn(world);
        }
    }

    public boolean hasSpawn(World world) {
        if (isYAML()) {
            return spawnYMLManager.hasSpawn(world);
        } else {
            return spawnSQLManager.hasSpawn(world);
        }
    }

    public boolean removeSpawn(World world) {
        if (isYAML()) {
            return spawnYMLManager.removeSpawn(world);
        } else {
            return spawnSQLManager.removeSpawn(world);
        }
    }

    public boolean isStatus(Player player) {
        return StatusManager.getPlayerStatus(player);
    }

}
