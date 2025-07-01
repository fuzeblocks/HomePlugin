package fr.fuzeblocks.homeplugin.home;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.sync.SyncMethod;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface Home {


    public boolean addHome(UUID uuid, Location location ,String name);
    public List<Location> getHomesLocation(UUID uuid);

    public int getHomeNumber(UUID uuid);

    public List<String> getHomesName(UUID uuid);

    public CacheManager getCacheManager();

    public Location getHomeLocation(UUID uuid, String homeName);

    public boolean deleteHome(UUID uuid, String homeName);

    public boolean isStatus(Player player);

    public boolean exist(UUID uuid, String homeName);

    public boolean renameHome(UUID uuid, String oldName, String newName);

    default boolean isYAML() {
        return HomePlugin.getRegistrationType().equals(SyncMethod.YAML);
    }
}

