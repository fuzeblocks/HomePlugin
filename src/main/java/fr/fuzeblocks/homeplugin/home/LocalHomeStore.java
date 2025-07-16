package fr.fuzeblocks.homeplugin.home;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LocalHomeStore implements HomeStore {
    private final Map<UUID, Map<String, Location>> playerHomes = new HashMap<>();

    @Override
    public void addHome(UUID playerId, String homeName, Location location) {
        playerHomes.computeIfAbsent(playerId, k -> new HashMap<>()).put(homeName, location);
    }

    @Override
    public void removeHome(UUID playerId, String homeName) {
        Map<String, Location> homes = playerHomes.get(playerId);
        if (homes != null) {
            homes.remove(homeName);
            if (homes.isEmpty()) playerHomes.remove(playerId);
        }
    }

    @Override
    public Map<String, Location> getHomes(UUID playerId) {
        return playerHomes.getOrDefault(playerId, Map.of());
    }

    @Override
    public void clearHomes(UUID playerId) {
        playerHomes.remove(playerId);
    }

    @Override
    public void clearAllHomes() {
        for (UUID playerId : playerHomes.keySet()) {
            playerHomes.get(playerId).clear();
        }
    }
}
