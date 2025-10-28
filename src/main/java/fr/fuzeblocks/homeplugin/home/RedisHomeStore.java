package fr.fuzeblocks.homeplugin.home;

import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import redis.clients.jedis.JedisPooled;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Redis implementation of HomeRequestStore for storing player homes.
 */
public class RedisHomeStore implements HomeRequestStore {

    private final JedisPooled jedis;
    private final Logger logger;
    private static final String KEY_PREFIX = "homes:";

    /**
     * Instantiates a new Redis home store.
     *
     * @param jedis The Jedis pooled connection
     */
    public RedisHomeStore(JedisPooled jedis) {
        this.jedis = jedis;
        this.logger = Logger.getLogger(RedisHomeStore.class.getName());
    }

    @Override
    public void addHome(UUID playerId, String homeName, Location location) {
        if (playerId == null || homeName == null || location == null) {
            logger.warning("Cannot add home: null parameter provided");
            return;
        }

        String key = getKey(playerId);
        String serialized = serializeLocation(location);

        if (serialized != null) {
            jedis.hset(key, homeName, serialized);
        }
    }

    @Override
    public void removeHome(UUID playerId, String homeName) {
        if (playerId == null || homeName == null) {
            logger.warning("Cannot remove home: null parameter provided");
            return;
        }

        jedis.hdel(getKey(playerId), homeName);
    }

    @Override
    public void relocateHome(UUID playerId, String homeName, Location newLocation) {
        if (playerId == null || homeName == null || newLocation == null) {
            logger.warning("Cannot relocate home: null parameter provided");
            return;
        }

        String key = getKey(playerId);

        if (!jedis.hexists(key, homeName)) {
            logger.warning("Cannot relocate home '" + homeName + "' for player " + playerId + ": home does not exist");
            return;
        }

        String serialized = serializeLocation(newLocation);
        if (serialized != null) {
            jedis.hset(key, homeName, serialized);
        }
    }

    @Override
    public Map<String, Location> getHomes(UUID playerId) {
        if (playerId == null) {
            logger.warning("Cannot get homes: playerId is null");
            return new HashMap<>();
        }

        Map<String, String> data = jedis.hgetAll(getKey(playerId));

        if (data == null || data.isEmpty()) {
            return new HashMap<>();
        }

        return deserializeLocationMap(data);
    }

    @Override
    public void clearHomes(UUID playerId) {
        if (playerId == null) {
            logger.warning("Cannot clear homes: playerId is null");
            return;
        }

        jedis.del(getKey(playerId));
    }

    @Override
    public void clearAllHomes() {
        try {
            Set<String> keys = jedis.keys(KEY_PREFIX + "*");

            if (keys != null && !keys.isEmpty()) {
                String[] keyArray = keys.toArray(new String[0]);
                jedis.del(keyArray);
            }
        } catch (Exception e) {
            logger.severe("Error clearing all homes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gets the Redis key for a player's homes.
     *
     * @param playerId The player's UUID
     * @return The Redis key
     */
    private String getKey(UUID playerId) {
        return KEY_PREFIX + playerId.toString();
    }

    /**
     * Serializes a Location to a YAML string.
     *
     * @param location The location to serialize
     * @return The serialized string, or null if serialization fails
     */
    private String serializeLocation(Location location) {
        if (location == null) {
            return null;
        }

        if (location.getWorld() == null) {
            logger.warning("Cannot serialize location: world is null");
            return null;
        }

        try {
            YamlConfiguration yaml = new YamlConfiguration();
            yaml.set("location", location);
            return yaml.saveToString();
        } catch (Exception e) {
            logger.severe("Error serializing location: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Deserializes a YAML string to a Location.
     *
     * @param data The YAML string
     * @return The deserialized Location, or null if deserialization fails
     */
    private Location deserializeLocation(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }

        try {
            YamlConfiguration yaml = new YamlConfiguration();
            yaml.loadFromString(data);
            Object obj = yaml.get("location");

            if (obj instanceof Location) {
                return (Location) obj;
            } else {
                logger.warning("Deserialized object is not a Location");
                return null;
            }
        } catch (InvalidConfigurationException e) {
            logger.severe("Invalid YAML configuration while deserializing location: " + e.getMessage());
            return null;
        } catch (Exception e) {
            logger.severe("Error deserializing location: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Deserializes a map of YAML strings to Locations.
     *
     * @param data The map of serialized locations
     * @return The map of deserialized locations
     */
    private Map<String, Location> deserializeLocationMap(Map<String, String> data) {
        Map<String, Location> map = new HashMap<>();

        if (data == null) {
            return map;
        }

        data.forEach((key, value) -> {
            Location location = deserializeLocation(value);
            if (location != null) {
                map.put(key, location);
            } else {
                logger.warning("Failed to deserialize location for home: " + key);
            }
        });

        return map;
    }
}