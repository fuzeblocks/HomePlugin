package fr.fuzeblocks.homeplugin.home;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.sync.SyncMethod;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public interface Home {

    public boolean addHome(Player player, String name);
    public boolean setHome(Player player, String name,Location location);
    public List<Location> getHomesLocation(Player player);

    public int getHomeNumber(Player player);

    public List<String> getHomesName(Player player);

    public CacheManager getCacheManager();

    public Location getHomeLocation(Player player, String homeName);

    public boolean deleteHome(Player player, String homeName);

    public boolean isStatus(Player player);

    public boolean exist(Player player, String homeName);
    default boolean isYAML() {
        return HomePlugin.getRegistrationType().equals(SyncMethod.YAML);
    }
}

