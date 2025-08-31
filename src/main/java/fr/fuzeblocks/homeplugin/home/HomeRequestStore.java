package fr.fuzeblocks.homeplugin.home;

import org.bukkit.Location;

import java.util.Map;
import java.util.UUID;

public interface HomeRequestStore {
    void addHome(UUID playerId, String homeName, Location location);
    void removeHome(UUID playerId, String homeName);
    Map<String, Location> getHomes(UUID playerId);
    void clearHomes(UUID playerId);
    void clearAllHomes();
}
