package fr.fuzeblocks.homeplugin.tpa;

import java.util.*;

public class LocalTpaRequestStore implements TpaRequestStore {
    private final Map<UUID, UUID> tpaRequests = new HashMap<>();

    @Override
    public void addTpaRequest(UUID sender, UUID target) {
        tpaRequests.put(sender, target);
    }

    @Override
    public UUID getTpaRequest(UUID sender) {
        return tpaRequests.get(sender);
    }

    @Override
    public boolean hasTpaRequest(UUID sender) {
        return tpaRequests.containsKey(sender);
    }

    @Override
    public void removeTpaRequest(UUID sender) {
        tpaRequests.remove(sender);
    }

    @Override
    public Set<UUID> getAllSenders() {
        return new HashSet<>(tpaRequests.keySet());
    }
}
