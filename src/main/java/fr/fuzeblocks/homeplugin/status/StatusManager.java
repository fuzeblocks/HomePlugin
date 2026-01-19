package fr.fuzeblocks.homeplugin.status;

import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Status manager.
 */
public class StatusManager {
    private static final ConcurrentHashMap<Player, Boolean> playerStatus = new ConcurrentHashMap<>();

    /**
     * Sets player status.
     *
     * @param player the player
     * @param status the status
     */
    public static void setPlayerStatus(Player player, boolean status) {
        playerStatus.put(player, status);
    }

    /**
     * Gets player status.
     *
     * @param player the player
     * @return the player status
     */
    public static boolean getPlayerStatus(Player player) {
        return playerStatus.getOrDefault(player, false);
    }

    /**
     * Removes player status.
     *
     * @param player the player
     */
    public static void removePlayerStatus(Player player) {
        playerStatus.remove(player);
    }

    /**
     * Clears all player statuses.
     * Useful for plugin reload or shutdown.
     */
    public static void clearAll() {
        playerStatus.clear();
    }

    /**
     * Checks if player has a status entry.
     *
     * @param player the player
     * @return true if player has a status entry
     */
    public static boolean hasStatus(Player player) {
        return playerStatus.containsKey(player);
    }
}