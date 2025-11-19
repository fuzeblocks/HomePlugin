package fr.fuzeblocks.homeplugin.home;

import org.bukkit.Location;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Local in-memory implementation of HomeRequestStore for storing player homes.
 */
public class LocalHomeStore implements HomeRequestStore {

    private final Map<UUID, Map<String, Location>> playerHomes = new ConcurrentHashMap<>();
    private final Logger logger = Logger.getLogger(LocalHomeStore.class.getName());

    @Override
    public void addHome(UUID playerId, String homeName, Location location) {
        if (playerId == null || homeName == null || location == null) {
            logger.warning("Cannot add home: null parameter provided");
            return;
        }

        if (location.getWorld() == null) {
            logger.warning("Cannot add home: location world is null");
            return;
        }

        playerHomes.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>()).put(homeName, location.clone());
    }

    @Override
    public void removeHome(UUID playerId, String homeName) {
        if (playerId == null || homeName == null) {
            logger.warning("Cannot remove home: null parameter provided");
            return;
        }

        Map<String, Location> homes = playerHomes.get(playerId);
        if (homes != null) {
            homes.remove(homeName);
            if (homes.isEmpty()) {
                playerHomes.remove(playerId);
            }
        }
    }

    @Override
    public void relocateHome(UUID playerId, String homeName, Location newLocation) {
        if (playerId == null || homeName == null || newLocation == null) {
            logger.warning("Cannot relocate home: null parameter provided");
            return;
        }

        if (newLocation.getWorld() == null) {
            logger.warning("Cannot relocate home: location world is null");
            return;
        }

        Map<String, Location> homes = playerHomes.get(playerId);
        if (homes == null || !homes.containsKey(homeName)) {
            logger.warning("Cannot relocate home '" + homeName + "' for player " + playerId + ": home does not exist");
            return;
        }

        homes.put(homeName, newLocation.clone());
    }

    @Override
    public Map<String, Location> getHomes(UUID playerId) {
        if (playerId == null) {
            logger.warning("Cannot get homes: playerId is null");
            return Collections.emptyMap();
        }

        Map<String, Location> homes = playerHomes.get(playerId);
        if (homes == null || homes.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Location> clonedHomes = new HashMap<>();
        homes.forEach((name, location) -> {
            if (location != null) {
                clonedHomes.put(name, location.clone());
            }
        });

        return Collections.unmodifiableMap(clonedHomes);
    }

    @Override
    public void clearHomes(UUID playerId) {
        if (playerId == null) {
            logger.warning("Cannot clear homes: playerId is null");
            return;
        }

        playerHomes.remove(playerId);
    }

    @Override
    public void clearAllHomes() {
        playerHomes.clear();
    }
}