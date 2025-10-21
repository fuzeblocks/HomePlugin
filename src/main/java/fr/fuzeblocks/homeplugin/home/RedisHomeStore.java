package fr.fuzeblocks.homeplugin.home;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import redis.clients.jedis.JedisPooled;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The type Redis home store.
 */
public class RedisHomeStore implements HomeRequestStore {

    private final JedisPooled jedis;

    /**
     * Instantiates a new Redis home store.
     *
     * @param jedis the jedis
     */
    public RedisHomeStore(JedisPooled jedis) {
        this.jedis = jedis;
    }

    @Override
    public void addHome(UUID playerId, String homeName, Location location) {
        jedis.hset(playerId.toString(), homeName, serializeLocation(location));
    }

    @Override
    public void removeHome(UUID playerId, String homeName) {
        jedis.hdel(playerId.toString(), homeName);
    }

    @Override
    public Map<String, Location> getHomes(UUID playerId) {
        Map<String, String> data = jedis.hgetAll(playerId.toString());
        return deserializeLocationMap(data);
    }

    @Override
    public void clearHomes(UUID playerId) {
        jedis.del(playerId.toString());
    }

    @Override
    public void clearAllHomes() {
        jedis.keys("homes:*").forEach(jedis::del);
    }


    private String serializeLocation(Location location) {
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("location", location);
        return yaml.saveToString();
    }

    private Location deserializeLocation(String data) {
        YamlConfiguration yaml = new YamlConfiguration();
        try {
            yaml.loadFromString(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (Location) yaml.get("location");
    }

    private Map<String, Location> deserializeLocationMap(Map<String, String> data) {
        Map<String, Location> map = new HashMap<>();
        data.forEach((key, value) -> map.put(key, deserializeLocation(value)));
        return map;
    }
}
