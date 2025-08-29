package fr.fuzeblocks.homeplugin.tpa;

import redis.clients.jedis.JedisPooled;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class RedisTpaRequestStore implements TpaRequestStore {

    private final JedisPooled jedis;
    private static final String FIELD_TPA_TARGET = "tpa_target";

    public RedisTpaRequestStore(JedisPooled jedis) {
        this.jedis = jedis;
    }

    @Override
    public void addTpaRequest(UUID sender, UUID target) {
        jedis.hset(sender.toString(), FIELD_TPA_TARGET, target.toString());
    }

    @Override
    public boolean hasTpaRequest(UUID sender, UUID target) {
        String stored = jedis.hget(sender.toString(), FIELD_TPA_TARGET);
        return target.toString().equals(stored);
    }

    @Override
    public void removeTpaRequest(UUID sender, UUID target) {
        String stored = jedis.hget(sender.toString(), FIELD_TPA_TARGET);
        if (target.toString().equals(stored)) {
            jedis.hdel(sender.toString(), FIELD_TPA_TARGET);
        }
    }

    @Override
    public UUID getTargetWithSender(UUID sender) {
        String targetUuid = jedis.hget(sender.toString(), FIELD_TPA_TARGET);
        if (targetUuid == null) return null;
        try {
            return UUID.fromString(targetUuid);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public boolean hasIncomingTpa(UUID target) {
        Set<String> keys = jedis.keys("*");
        for (String key : keys) {
            String stored = jedis.hget(key, FIELD_TPA_TARGET);
            if (target.toString().equals(stored)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public UUID getSenderForTarget(UUID target) {
        Set<String> keys = jedis.keys("*");
        for (String key : keys) {
            String stored = jedis.hget(key, FIELD_TPA_TARGET);
            if (target.toString().equals(stored)) {
                try {
                    return UUID.fromString(key);
                } catch (IllegalArgumentException ignored) {}
            }
        }
        return null;
    }

    @Override
    public UUID getTpaTarget(UUID senderId) {
        String targetUuid = jedis.hget(senderId.toString(), FIELD_TPA_TARGET);
        if (targetUuid == null) return null;
        try {
            return UUID.fromString(targetUuid);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    public Set<UUID> getAllTpaSenders() {
        Set<String> keys = jedis.keys("*");
        return keys.stream()
                .map(key -> {
                    try {
                        return UUID.fromString(key);
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .filter(uuid -> uuid != null)
                .collect(Collectors.toSet());
    }


}
