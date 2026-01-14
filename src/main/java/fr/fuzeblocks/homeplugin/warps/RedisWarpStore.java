package fr.fuzeblocks.homeplugin.warps;

import redis.clients.jedis.JedisPooled;

import java.util.*;

/**
 * The type Redis warp store.
 */
public class RedisWarpStore implements WarpRequestStore {

    private final JedisPooled jedis;
    private static final String WARP_KEY_PREFIX = "warp:";
    private static final String WARP_NAMES_KEY = "warp:names";

    /**
     * Instantiates a new Redis warp store.
     *
     * @param jedis the jedis
     */
    public RedisWarpStore(JedisPooled jedis) {
        this.jedis = jedis;
    }

    private String keyOf(String name) {
        return WARP_KEY_PREFIX + name;
    }

    @Override
    public boolean saveWarp(WarpData warpData) {
        if (warpData == null || warpData.getName() == null || warpData.getName().isEmpty()) {
            return false;
        }
        String name = warpData.getName();
        String key = keyOf(name);
        String serialized = warpData.serialize();

        jedis.set(key, serialized);
        jedis.sadd(WARP_NAMES_KEY, name);
        return true;
    }

    @Override
    public boolean deleteWarp(String name) {
        if (name == null || name.isEmpty()) return false;

        String key = keyOf(name);
        Long removed = jedis.del(key);
        jedis.srem(WARP_NAMES_KEY, name);
        return removed != null && removed > 0;
    }

    @Override
    public WarpData loadWarp(String name) {
        if (name == null || name.isEmpty()) return null;

        String key = keyOf(name);
        String serialized = jedis.get(key);
        if (serialized == null) return null;
        if (!WarpData.isValidSerializedData(serialized)) return null;

        return WarpData.deserialize(serialized);
    }

    @Override
    public boolean warpExists(String name) {
        if (name == null || name.isEmpty()) return false;
        String key = keyOf(name);
        return jedis.exists(key);
    }

    @Override
    public Map<String, WarpData> loadAllWarps() {
        Set<String> names = jedis.smembers(WARP_NAMES_KEY);
        if (names == null || names.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, WarpData> result = new HashMap<>();
        for (String name : names) {
            String serialized = jedis.get(keyOf(name));
            if (serialized == null) continue;
            if (!WarpData.isValidSerializedData(serialized)) continue;

            WarpData data = WarpData.deserialize(serialized);
            if (data != null) {
                result.put(name, data);
            }
        }

        return Collections.unmodifiableMap(result);
    }

    @Override
    public Set<String> getWarpNames() {
        Set<String> names = jedis.smembers(WARP_NAMES_KEY);
        if (names == null) return Collections.emptySet();
        return Collections.unmodifiableSet(new HashSet<>(names));
    }
}