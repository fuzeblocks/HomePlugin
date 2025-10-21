package fr.fuzeblocks.homeplugin.home;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.sync.SyncMethod;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * The interface Home.
 */
public interface Home {

    /**
     * Add home boolean.
     *
     * @param player the player
     * @param name   the name
     * @return the boolean
     */
    boolean addHome(Player player, String name);

    /**
     * Sets home.
     *
     * @param player   the player
     * @param name     the name
     * @param location the location
     * @return the home
     */
    boolean setHome(Player player, String name, Location location);

    /**
     * Rename home boolean.
     *
     * @param player      the player
     * @param oldHomeName the old home name
     * @param newHomeName the new home name
     * @return the boolean
     */
    boolean renameHome(Player player, String oldHomeName, String newHomeName);

    /**
     * Relocate home boolean.
     *
     * @param player      the player
     * @param homeName    the home name
     * @param newLocation the new location
     * @return the boolean
     */
    boolean relocateHome(Player player, String homeName, Location newLocation);

    /**
     * Gets homes location.
     *
     * @param player the player
     * @return the homes location
     */
    List<Location> getHomesLocation(Player player);

    /**
     * Gets home number.
     *
     * @param player the player
     * @return the home number
     */
    int getHomeNumber(Player player);

    /**
     * Gets homes name.
     *
     * @param player the player
     * @return the homes name
     */
    List<String> getHomesName(Player player);

    /**
     * Gets cache manager.
     *
     * @return the cache manager
     */
    CacheManager getCacheManager();

    /**
     * Gets home location.
     *
     * @param player   the player
     * @param homeName the home name
     * @return the home location
     */
    Location getHomeLocation(Player player, String homeName);

    /**
     * Delete home boolean.
     *
     * @param player   the player
     * @param homeName the home name
     * @return the boolean
     */
    boolean deleteHome(Player player, String homeName);

    /**
     * Is status boolean.
     *
     * @param player the player
     * @return the boolean
     */
    boolean isStatus(Player player);

    /**
     * Exist boolean.
     *
     * @param player   the player
     * @param homeName the home name
     * @return the boolean
     */
    boolean exist(Player player, String homeName);

    /**
     * Is yaml boolean.
     *
     * @return the boolean
     */
    default boolean isYAML() {
        return HomePlugin.getRegistrationType().equals(SyncMethod.YAML);
    }
}

