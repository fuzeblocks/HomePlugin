package fr.fuzeblocks.homeplugin.cache;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.yml.HomeManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CacheManager {
    private static CacheManager instance;
    public static synchronized CacheManager getInstance() {
        if (instance == null) {
            instance = new CacheManager();
        }
        return instance;
    }
    private HashMap<Player,HashMap<String, Location>> playerHomes = new HashMap<>();
    public void addHomeInCache(Player player,String homeName,Location location) {
        if (playerHomes.containsKey(player)) {
            playerHomes.get(player).put(homeName,location);
            return;
        }
        playerHomes.computeIfAbsent(player, key -> {
            HashMap<String, Location> homeMap = new HashMap<>();
            homeMap.put(homeName,location);
            return homeMap;
        });
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
    public void addAllPlayerHomes(Player player) {
        HomeManager homeManager = HomePlugin.getHomeManager();
        for (String homeName :  homeManager.getHomesName(player)) {
            for (Location homeLocation : homeManager.getHomesLocation(player)) {
                addHomeInCache(player,homeName,homeLocation);
            }
        }
    }
    private CacheManager() {}

}
