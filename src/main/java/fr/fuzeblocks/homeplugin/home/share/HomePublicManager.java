package fr.fuzeblocks.homeplugin.home.share;

import fr.fuzeblocks.homeplugin.cache.CacheManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class HomePublicManager implements PublicHome {

    @Override
    public boolean addHome(Player player, String name) {
        return false;
    }

    @Override
    public boolean setHome(Player player, String name, Location location) {
        return false;
    }

    @Override
    public boolean renameHome(Player player, String oldHomeName, String newHomeName) {
        return false;
    }

    @Override
    public boolean relocateHome(Player player, String homeName, Location newLocation) {
        return false;
    }

    @Override
    public List<Location> getHomesLocation(Player player) {
        return List.of();
    }

    @Override
    public int getHomeNumber(Player player) {
        return 0;
    }

    @Override
    public List<String> getHomesName(Player player) {
        return List.of();
    }

    @Override
    public Location getHomeLocation(Player player, String homeName) {
        return null;
    }

    @Override
    public boolean deleteHome(Player player, String homeName) {
        return false;
    }

    @Override
    public boolean exist(Player player, String homeName) {
        return false;
    }

    @Override
    public boolean isStatus(Player player) {
        return false;
    }

    @Override
    public boolean setHome(UUID uuid, String name, Location location) {
        return false;
    }

    @Override
    public boolean renameHome(UUID uuid, String oldHomeName, String newHomeName) {
        return false;
    }

    @Override
    public boolean relocateHome(UUID uuid, String homeName, Location newLocation) {
        return false;
    }

    @Override
    public List<Location> getHomesLocation(UUID uuid) {
        return List.of();
    }

    @Override
    public int getHomeNumber(UUID uuid) {
        return 0;
    }

    @Override
    public List<String> getHomesName(UUID uuid) {
        return List.of();
    }

    @Override
    public Location getHomeLocation(UUID uuid, String homeName) {
        return null;
    }

    @Override
    public boolean deleteHome(UUID uuid, String homeName) {
        return false;
    }

    @Override
    public boolean exist(UUID uuid, String homeName) {
        return false;
    }

    @Override
    public CacheManager getCacheManager() {
        return null;
    }

    @Override
    public void addPlayerToBlackList(Player owner, Player target, String homeName) {

    }

    @Override
    public void addPlayerToBlackList(UUID ownerUUID, UUID targetUUID, String homeName) {

    }

    @Override
    public void removePlayerFromBlackList(Player owner, Player target, String homeName) {

    }

    @Override
    public void removePlayerFromBlackList(UUID ownerUUID, UUID targetUUID, String homeName) {

    }

    @Override
    public boolean isPlayerInBlackList(Player owner, Player target, String homeName) {
        return false;
    }

    @Override
    public boolean isPlayerInBlackList(UUID ownerUUID, UUID targetUUID, String homeName) {
        return false;
    }

    @Override
    public List<UUID> getBlackList(UUID ownerUUID, String homeName) {
        return List.of();
    }

    @Override
    public List<UUID> getBlackList(Player owner, String homeName) {
        return List.of();
    }
}
