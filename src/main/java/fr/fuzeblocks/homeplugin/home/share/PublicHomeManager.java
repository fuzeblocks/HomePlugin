package fr.fuzeblocks.homeplugin.home.share;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.home.Home;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class PublicHomeManager implements Home {

    private final HomeManager homeManager = HomeManager.getInstance();

    @Override
    public boolean addHome(Player owner, String name) {
        return homeManager.addHome(owner, name + "_public");
    }

    @Override
    public List<Location> getHomesLocation(Player player) {
        return getHomesName(player).stream()
                .map(homeName -> homeManager.getHomeLocation(player, homeName))
                .toList();
    }

    @Override
    public int getHomeNumber(Player player) {
        return getHomesName(player).size();
    }

    @Override
    public List<String> getHomesName(Player player) {
      return homeManager.getHomesName(player).stream()
                .filter(name -> name.endsWith("_public"))
                .toList();
    }

    public CacheManager getCacheManager() {
        return HomePlugin.getCacheManager();
    }
    @Override
    public Location getHomeLocation(Player player, String homeName) {
        return homeManager.getHomeLocation(player,homeName + "_public");
    }

    @Override
    public boolean deleteHome(Player player, String homeName) {
        return homeManager.deleteHome(player,homeName + "_public");
    }
    public boolean isStatus(Player player) {
        return StatusManager.getPlayerStatus(player);
    }

    @Override
    public boolean exist(Player player, String homeName) {
        return homeManager.exist(player,homeName + "_public");
    }

    @Override
    public boolean renameHome(Player player, String oldName, String newName) {
        return homeManager.renameHome(player, oldName + "_public", newName + "_public");
    }
}
