package fr.fuzeblocks.homeplugin.spawn;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.sync.SyncMethod;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * The interface Spawn.
 */
public interface Spawn {
    /**
     * Sets spawn.
     *
     * @param location the location
     * @return the spawn
     */
    boolean setSpawn(Location location);

    /**
     * Gets spawn.
     *
     * @param world the world
     * @return the spawn
     */
    Location getSpawn(World world);

    /**
     * Has spawn boolean.
     *
     * @param world the world
     * @return the boolean
     */
    boolean hasSpawn(World world);

    /**
     * Remove spawn boolean.
     *
     * @param world the world
     * @return the boolean
     */
    boolean removeSpawn(World world);

    /**
     * Is status boolean.
     *
     * @param player the player
     * @return the boolean
     */
    boolean isStatus(Player player);

    /**
     * Is yaml boolean.
     *
     * @return the boolean
     */
    default boolean isYAML() {
        return HomePlugin.getRegistrationType().equals(SyncMethod.YAML);
    }
}
