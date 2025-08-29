package fr.fuzeblocks.homeplugin.tpa;

import java.util.Set;
import java.util.UUID;

public interface TpaRequestStore {
    void addTpaRequest(UUID sender, UUID target);
    UUID getTpaTarget(UUID senderId);
    boolean hasTpaRequest(UUID sender, UUID target);
    void removeTpaRequest(UUID sender, UUID target);
    UUID getTargetWithSender(UUID sender);
    boolean hasIncomingTpa(UUID target);
    UUID getSenderForTarget(UUID target);
    Set<UUID> getAllTpaSenders();
}
