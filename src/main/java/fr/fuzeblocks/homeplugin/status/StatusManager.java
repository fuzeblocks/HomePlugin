package fr.fuzeblocks.homeplugin.status;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class StatusManager {
    private static HashMap<Player,Status> player_status = new HashMap<>();
    public static void setPlayerStatus(Player player,Status status) {
        if (player_status.containsKey(player)) {
            player_status.remove(player);
            player_status.put(player,status);
        } else {
            player_status.put(player,status);
        }
    }

    public static Status getPlayerStatus(Player player) {
        if (player_status.containsKey(player)) {
            return player_status.get(player);
        }
        return null;
    }
}
