package fr.fuzeblocks.homeplugin.core.tpa;

import java.util.Set;
import java.util.UUID;

/**
 * The interface Tpa request store.
 */
public interface TpaRequestStore {
    /**
     * Add tpa request.
     *
     * @param sender the sender
     * @param target the target
     */
    void addTpaRequest(UUID sender, UUID target);


    /**
     * Has tpa request boolean.
     *
     * @param sender the sender
     * @param target the target
     * @return the boolean
     */
    boolean hasTpaRequest(UUID sender, UUID target);

    /**
     * Remove tpa request.
     *
     * @param sender the sender
     * @param target the target
     */
    void removeTpaRequest(UUID sender, UUID target);

    /**
     * Gets target with sender.
     *
     * @param sender the sender
     * @return the target with sender
     */
    UUID getTargetWithSender(UUID sender);

    /**
     * Has incoming tpa boolean.
     *
     * @param target the target
     * @return the boolean
     */
    boolean hasIncomingTpa(UUID target);

    /**
     * Gets sender for target.
     *
     * @param target the target
     * @return the sender for target
     */
    UUID getSenderForTarget(UUID target);

    /**
     * Gets all tpa senders.
     *
     * @return the all tpa senders
     */
    Set<UUID> getAllTpaSenders();
}
