package fr.fuzeblocks.homeplugin.rtp;

import java.util.Map;
import java.util.UUID;

/**
 * The interface Rtp request store.
 */
public interface RtpRequestStore {
    /**
     * Add rtp request.
     *
     * @param playerId  the player id
     * @param timestamp the timestamp
     */
    void addRtpRequest(UUID playerId, Long timestamp);

    /**
     * Gets rtp request.
     *
     * @param playerId the player id
     * @return the rtp request
     */
    Long getRtpRequest(UUID playerId);

    /**
     * Remove rtp request.
     *
     * @param playerId the player id
     */
    void removeRtpRequest(UUID playerId);

    /**
     * Has rtp request boolean.
     *
     * @param playerId the player id
     * @return the boolean
     */
    boolean hasRtpRequest(UUID playerId);

    /**
     * Gets all rtp requests.
     *
     * @return the all rtp requests
     */
    Map<UUID, Long> getAllRtpRequests();
}
