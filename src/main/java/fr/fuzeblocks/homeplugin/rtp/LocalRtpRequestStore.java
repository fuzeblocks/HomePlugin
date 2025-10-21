package fr.fuzeblocks.homeplugin.rtp;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Local rtp request store.
 */
public class LocalRtpRequestStore implements RtpRequestStore {
    private final Map<UUID, Long> rtpCooldownMap = new ConcurrentHashMap<>();

    @Override
    public void addRtpRequest(UUID playerId, Long timestamp) {
        rtpCooldownMap.put(playerId,timestamp);
    }

    @Override
    public Long getRtpRequest(UUID playerId) {
       return rtpCooldownMap.get(playerId);
    }

    @Override
    public void removeRtpRequest(UUID playerId) {
        rtpCooldownMap.remove(playerId);
    }

    @Override
    public boolean hasRtpRequest(UUID playerId) {
        return rtpCooldownMap.containsKey(playerId);
    }

    @Override
    public Map<UUID, Long> getAllRtpRequests() {
        return rtpCooldownMap;
    }
}
