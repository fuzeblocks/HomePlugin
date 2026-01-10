package fr.fuzeblocks.homeplugin.home.offline;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.sync.SyncMethod;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

/**
 * The interface OfflineHome.
 */
public interface OfflineHome {

    /**
     * Sets home.
     *
     * @param uuid     the UUID of the player
     * @param name     the name
     * @param location the location
     * @return the home
     */
    boolean setHome(UUID uuid, String name, Location location);

    /**
     * Rename home boolean.
     *
     * @param uuid        the UUID of the player
     * @param oldHomeName the old home name
     * @param newHomeName the new home name
     * @return the boolean
     */
    boolean renameHome(UUID uuid, String oldHomeName, String newHomeName);

    /**
     * Relocate home boolean.
     *
     * @param uuid        the UUID of the player
     * @param homeName    the home name
     * @param newLocation the new location
     * @return the boolean
     */
    boolean relocateHome(UUID uuid, String homeName, Location newLocation);

    /**
     * Gets homes location.
     *
     * @param uuid the UUID of the player
     * @return the homes location
     */
    List<Location> getHomesLocation(UUID uuid);

    /**
     * Gets home number.
     *
     * @param uuid the UUID of the player
     * @return the home number
     */
    int getHomeNumber(UUID uuid);

    /**
     * Gets homes name.
     *
     * @param uuid the UUID of the player
     * @return the homes name
     */
    List<String> getHomesName(UUID uuid);

    /**
     * Gets cache manager.
     *
     * @return the cache manager
     */
    CacheManager getCacheManager();

    /**
     * Gets home location.
     *
     * @param uuid     the UUID of the player
     * @param homeName the home name
     * @return the home location
     */
    Location getHomeLocation(UUID uuid, String homeName);

    /**
     * Delete home boolean.
     *
     * @param uuid     the UUID of the player
     * @param homeName the home name
     * @return the boolean
     */
    boolean deleteHome(UUID uuid, String homeName);

    /**
     * Exist boolean.
     *
     * @param uuid     the UUID of the player
     * @param homeName the home name
     * @return the boolean
     */
    boolean exist(UUID uuid, String homeName);

    /**
     * Is yaml boolean.
     *
     * @return the boolean
     */
    default boolean isYAML() {
        return HomePlugin.getRegistrationType().equals(SyncMethod.YAML);
    }
}
