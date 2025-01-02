package fr.fuzeblocks.homeplugin.cache;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.yml.HomeYMLManager;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import redis.clients.jedis.JedisPooled;


import java.util.HashMap;
import java.util.Map;

public class CacheManager {
    private static CacheManager instance;
    private final HashMap<Player, HashMap<String, Location>> playerHomes = new HashMap();
    private JedisPooled jedisPooled;
    private final boolean useRedis = HomePlugin.getConfigurationSection().getBoolean("Config.Connector.Redis.UseRedis");

    private CacheManager() {
        if (useRedis && jedisPooled == null) {
            jedisPooled = HomePlugin.getJedisPooled();
        }
    }

    public static synchronized CacheManager getInstance() {
        if (instance == null) {
            instance = new CacheManager();
        }
        return instance;
    }

    public void addHomeInCache(Player player, String homeName, Location location) {
        if (useRedis) {
            String serializedLocation = getStringFromLocation(location);
            jedisPooled.hset(player.getUniqueId().toString(), homeName, serializedLocation);
        } else {
            playerHomes.computeIfAbsent(player, k -> new HashMap<>()).put(homeName, location);
        }
    }

    public HashMap<String, Location> getHomesInCache(Player player) {
        if (useRedis) {
            Map<String, String> redisData = jedisPooled.hgetAll(player.getUniqueId().toString());
            return deserializeLocationMap(redisData);
        } else {
            return playerHomes.get(player);
        }
    }

    public boolean delHomeInCache(Player player, String homeName) {
        if (useRedis) {
            jedisPooled.hdel(player.getUniqueId().toString(), homeName);
            return true;
        } else {
            HashMap<String, Location> homes = playerHomes.get(player);
            if (homes != null) {
                homes.remove(homeName);
                return true;
            }
            return false;
        }
    }

    public void clear() {
        playerHomes.clear();
        if (useRedis) {
            jedisPooled.flushDB();
        }
    }

    public void clearPlayer(Player player) {
        if (useRedis) {
            jedisPooled.del(player.getUniqueId().toString());
        } else {
            HashMap<String, Location> homes = playerHomes.get(player);
            if (homes != null) {
                homes.clear();
            }
        }
    }

    public void addAllPlayerHomes(Player player) {
        HomeYMLManager homeYMLManager = HomePlugin.getHomeYMLManager();
        for (String homeName : homeYMLManager.getHomesName(player)) {
            Location homeLocation = homeYMLManager.getHomeLocation(player, homeName);
            addHomeInCache(player, homeName, homeLocation);
        }
    }

    private String getStringFromLocation(Location location) {
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.set("location", location);
        return yamlConfiguration.saveToString();
    }

    private Location getLocationFromString(String serializedLocation) {
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        try {
            yamlConfiguration.loadFromString(serializedLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (Location) yamlConfiguration.get("location");
    }

    private HashMap<String, Location> deserializeLocationMap(Map<String, String> map) {
        HashMap<String, Location> locationMap = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Location location = getLocationFromString(entry.getValue());
            locationMap.put(entry.getKey(), location);
        }
        return locationMap;
    }
}
