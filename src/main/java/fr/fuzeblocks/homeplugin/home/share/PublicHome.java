package fr.fuzeblocks.homeplugin.home.share;

import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.home.Home;
import fr.fuzeblocks.homeplugin.home.offline.OfflineHome;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface PublicHome extends Home, OfflineHome {

    // ----- Player -----

    @Override
    boolean addHome(Player player, String name);

    @Override
    boolean setHome(Player player, String name, Location location);

    @Override
    boolean renameHome(Player player, String oldHomeName, String newHomeName);

    @Override
    boolean relocateHome(Player player, String homeName, Location newLocation);

    @Override
    List<Location> getHomesLocation(Player player);

    @Override
    int getHomeNumber(Player player);

    @Override
    List<String> getHomesName(Player player);

    @Override
    Location getHomeLocation(Player player, String homeName);

    @Override
    boolean deleteHome(Player player, String homeName);

    @Override
    boolean exist(Player player, String homeName);

    @Override
    default boolean isYAML() {
        return Home.super.isYAML();
    }

    @Override
    boolean isStatus(Player player);

    // ----- UUID -----

    @Override
    boolean setHome(UUID uuid, String name, Location location);

    @Override
    boolean renameHome(UUID uuid, String oldHomeName, String newHomeName);

    @Override
    boolean relocateHome(UUID uuid, String homeName, Location newLocation);

    @Override
    List<Location> getHomesLocation(UUID uuid);

    @Override
    int getHomeNumber(UUID uuid);

    @Override
    List<String> getHomesName(UUID uuid);

    @Override
    Location getHomeLocation(UUID uuid, String homeName);

    @Override
    boolean deleteHome(UUID uuid, String homeName);

    @Override
    boolean exist(UUID uuid, String homeName);

    // ----- Cache -----

    @Override
    CacheManager getCacheManager();

    // ----- Blacklist -----

    void addPlayerToBlackList(Player owner, Player target, String homeName);

    void addPlayerToBlackList(UUID ownerUUID, UUID targetUUID, String homeName);

    void removePlayerFromBlackList(Player owner, Player target, String homeName);

    void removePlayerFromBlackList(UUID ownerUUID, UUID targetUUID, String homeName);

    boolean isPlayerInBlackList(Player owner, Player target, String homeName);

    boolean isPlayerInBlackList(UUID ownerUUID, UUID targetUUID, String homeName);

    List<UUID> getBlackList(UUID ownerUUID, String homeName);

    List<UUID> getBlackList(Player owner, String homeName);
}
