package fr.fuzeblocks.homeplugin.cache;

import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class CacheManager {
    public CacheManager(HomePlugin instance) {
        new BukkitRunnable() {
            @Override
            public void run() {
              clear();
              System.out.println("Le cache a été vidé !");
            }
        }.runTaskTimer(instance, 0, 20 * 60 * 30);
    }


    private HashMap<Player,HashMap<String, Location>> playerHomes = new HashMap<>();
    public void addHomeInCache(Player player,String homeName,Location location) {
        if (playerHomes.containsKey(player)) {
            playerHomes.get(player).put(homeName,location);
            return;
        }
        HashMap<String,Location> homes = new HashMap<>();
            homes.put(homeName,location);
            playerHomes.put(player,homes);
    }
    public HashMap<String, Location> getHomesInCache(Player player) {
        return playerHomes.get(player);
    }
    public boolean delHomeInCache(Player player,String homeName) {
        if (playerHomes.containsKey(player)) {
            playerHomes.get(player).remove(homeName);
            return true;
        }
        return false;
    }
    public void clear() {
        playerHomes.clear();
    }
    public void clearPlayer(Player player) {
        if (playerHomes.containsKey(player)) {
            playerHomes.get(player).clear();
        }
    }
}
