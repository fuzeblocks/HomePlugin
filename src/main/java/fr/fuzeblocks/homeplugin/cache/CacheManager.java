package fr.fuzeblocks.homeplugin.cache;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CacheManager {
    private  HashMap<Player,HashMap<String, Location>> playerHomes = new HashMap<>();
    public void addHomeInCache(Player player,String homeName,Location location) {
        if (playerHomes.containsKey(player)) {
            playerHomes.get(player).put(homeName,location);
            return;
        }
        HashMap<String,Location> homes = new HashMap<>();
            homes.put(homeName,location);
            playerHomes.put(player,homes);
    }
    public  HashMap<String, Location> getHomesInCache(Player player) {
        return playerHomes.get(player);
    }
}
