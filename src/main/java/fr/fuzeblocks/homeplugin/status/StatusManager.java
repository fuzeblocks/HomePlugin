package fr.fuzeblocks.homeplugin.status;

import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

public class StatusManager {
    private static final ConcurrentHashMap<Player,Boolean> player_status = new ConcurrentHashMap<>();

    public static void setPlayerStatus(Player player, boolean status) {
        if (player_status.containsKey(player)) {
            player_status.remove(player);
            player_status.put(player, status);
        } else {
            player_status.put(player, status);
        }
    }

    public static boolean getPlayerStatus(Player player) {
        if (player_status.containsKey(player)) {
            return  player_status.get(player);
        }
        return false;
    }
}
