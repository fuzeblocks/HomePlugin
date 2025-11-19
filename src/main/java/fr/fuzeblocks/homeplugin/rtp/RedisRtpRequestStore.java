package fr.fuzeblocks.homeplugin.rtp;

import redis.clients.jedis.JedisPooled;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The type Redis rtp request store.
 */
public class RedisRtpRequestStore implements RtpRequestStore {

    private static final String REDIS_KEY = "rtp_requests";
    private final JedisPooled jedis;

    /**
     * Instantiates a new Redis rtp request store.
     *
     * @param jedis the jedis
     */
    public RedisRtpRequestStore(JedisPooled jedis) {
        this.jedis = jedis;
    }

    @Override
    public void addRtpRequest(UUID playerId, Long timestamp) {
        jedis.hset(REDIS_KEY, playerId.toString(), String.valueOf(timestamp));
    }

    @Override
    public Long getRtpRequest(UUID playerId) {
        String stored = jedis.hget(REDIS_KEY, playerId.toString());
        if (stored == null) return null;
        try {
            return Long.parseLong(stored);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public void removeRtpRequest(UUID playerId) {
        jedis.hdel(REDIS_KEY, playerId.toString());
    }

    @Override
    public boolean hasRtpRequest(UUID playerId) {
        return jedis.hexists(REDIS_KEY, playerId.toString());
    }

    @Override
    public Map<UUID, Long> getAllRtpRequests() {
        Map<String, String> entries = jedis.hgetAll(REDIS_KEY);
        return entries.entrySet().stream()
                .map(entry -> {
                    try {
                        UUID uuid = UUID.fromString(entry.getKey());
                        Long timestamp = Long.parseLong(entry.getValue());
                        return Map.entry(uuid, timestamp);
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
