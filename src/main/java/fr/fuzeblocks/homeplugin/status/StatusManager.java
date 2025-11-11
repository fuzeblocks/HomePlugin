package fr.fuzeblocks.homeplugin.status;

import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Status manager.
 */
public class StatusManager {
    private static final ConcurrentHashMap<Player, Boolean> player_status = new ConcurrentHashMap<>();

    /**
     * Sets player status.
     *
     * @param player the player
     * @param status the status
     */
    public static void setPlayerStatus(Player player, boolean status) {
        if (player_status.containsKey(player)) {
            player_status.remove(player);
            player_status.put(player, status);
        } else {
            player_status.put(player, status);
        }
    }

    /**
     * Gets player status.
     *
     * @param player the player
     * @return the player status
     */
    public static boolean getPlayerStatus(Player player) {
        if (player_status.containsKey(player)) {
            return player_status.get(player);
        }
        return false;
    }
}
