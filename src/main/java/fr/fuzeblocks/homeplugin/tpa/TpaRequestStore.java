package fr.fuzeblocks.homeplugin.tpa;

import java.util.Set;
import java.util.UUID;

public interface TpaRequestStore {
    void addTpaRequest(UUID sender, UUID target);
    UUID getTpaRequest(UUID sender);
    boolean hasTpaRequest(UUID sender);
    void removeTpaRequest(UUID sender);
    Set<UUID> getAllSenders();
}

