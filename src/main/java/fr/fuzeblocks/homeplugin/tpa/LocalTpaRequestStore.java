package fr.fuzeblocks.homeplugin.tpa;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * The type Local tpa request store.
 */
public class LocalTpaRequestStore implements TpaRequestStore {
    private final Map<UUID, UUID> tpaRequests = new HashMap<>(); // sender -> target

    @Override
    public void addTpaRequest(UUID sender, UUID target) {
        tpaRequests.put(sender, target);
    }

    @Override
    public boolean hasTpaRequest(UUID sender, UUID target) {
        return target.equals(tpaRequests.get(sender));
    }

    @Override
    public void removeTpaRequest(UUID sender, UUID target) {
        if (target.equals(tpaRequests.get(sender))) {
            tpaRequests.remove(sender);
        }
    }

    @Override
    public UUID getTargetWithSender(UUID sender) {
        return tpaRequests.get(sender);
    }

    @Override
    public boolean hasIncomingTpa(UUID target) {
        return tpaRequests.containsValue(target);
    }

    @Override
    public UUID getSenderForTarget(UUID target) {
        return tpaRequests.entrySet().stream()
                .filter(entry -> entry.getValue().equals(target))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    @Override
    public UUID getTpaTarget(UUID senderId) {
        return tpaRequests.get(senderId); // tpaRequests : Map<UUID, UUID>
    }

    public Set<UUID> getAllTpaSenders() {
        return tpaRequests.keySet();
    }
}
