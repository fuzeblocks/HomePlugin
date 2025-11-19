package fr.fuzeblocks.homeplugin.spawn;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import redis.clients.jedis.JedisPooled;

/**
 * The type Redis spawn store.
 */
public class RedisSpawnStore implements SpawnRequestStore {

    private static final String SPAWN_KEY = "global_spawn";
    private final JedisPooled jedis;

    /**
     * Instantiates a new Redis spawn store.
     *
     * @param jedis the jedis
     */
    public RedisSpawnStore(JedisPooled jedis) {
        this.jedis = jedis;
    }

    @Override
    public Location getSpawn() {
        String data = jedis.get(SPAWN_KEY);
        if (data == null) return null;
        YamlConfiguration yaml = new YamlConfiguration();
        try {
            yaml.loadFromString(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return (Location) yaml.get("location");
    }

    @Override
    public void setSpawn(Location location) {
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("location", location);
        String data = yaml.saveToString();
        jedis.set(SPAWN_KEY, data);
    }

    @Override
    public void clearSpawn() {
        jedis.del(SPAWN_KEY);
    }
}
