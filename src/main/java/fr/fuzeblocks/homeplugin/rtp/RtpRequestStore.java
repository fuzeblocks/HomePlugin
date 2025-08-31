package fr.fuzeblocks.homeplugin.rtp;

import java.util.Map;
import java.util.UUID;

public interface RtpRequestStore {
    void addRtpRequest(UUID playerId, Long timestamp);
    Long getRtpRequest(UUID playerId);
    void removeRtpRequest(UUID playerId);
    boolean hasRtpRequest(UUID playerId);
    Map<UUID, Long> getAllRtpRequests();
}
