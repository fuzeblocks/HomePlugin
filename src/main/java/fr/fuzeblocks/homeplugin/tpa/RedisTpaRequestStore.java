package fr.fuzeblocks.homeplugin.tpa;

import redis.clients.jedis.JedisPooled;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
    public UUID getTpaRequest(UUID sender) {
        String targetUuid = jedis.hget(sender.toString(), FIELD_TPA_TARGET);
        if (targetUuid == null) return null;
        try {
            return UUID.fromString(targetUuid);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public boolean hasTpaRequest(UUID sender) {
        return jedis.hexists(sender.toString(), FIELD_TPA_TARGET);
    }

    @Override
    public void removeTpaRequest(UUID sender) {
        jedis.hdel(sender.toString(), FIELD_TPA_TARGET);
    }

    @Override
    public Set<UUID> getAllSenders() {
        Set<UUID> senders = new HashSet<>();
        Set<String> keys = jedis.keys("*");
        for (String key : keys) {
            if (jedis.hexists(key, FIELD_TPA_TARGET)) {
                try {
                    senders.add(UUID.fromString(key));
                } catch (IllegalArgumentException ignored) {}
            }
        }
        return senders;
    }
}
