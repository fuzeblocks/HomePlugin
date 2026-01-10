package fr.fuzeblocks. homeplugin.home;

import fr.fuzeblocks. homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache. CacheManager;
import fr. fuzeblocks.homeplugin.status.StatusManager;
import org. bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * The type Home manager.
 */
public class HomeManager implements Home {

    private static HomeManager instance = null;
    private final Home homeImplementation;

    private HomeManager() {
        if (isYAML()) {
            this.homeImplementation = HomePlugin.getHomeYMLManager();
        } else {
            this.homeImplementation = HomePlugin.getHomeSQLManager();
        }
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static HomeManager getInstance() {
        if (instance == null) {
            instance = new HomeManager();
        }
        return instance;
    }

    @Override
    public boolean addHome(Player player, String name) {
        return homeImplementation.addHome(player, name);
    }

    @Override
    public boolean setHome(Player player, String name, Location location) {
        return homeImplementation.setHome(player, name, location);
    }

    @Override
    public boolean renameHome(Player player, String oldHomeName, String newHomeName) {
        return homeImplementation.renameHome(player, oldHomeName, newHomeName);
    }

    @Override
    public boolean relocateHome(Player player, String homeName, Location newLocation) {
        return homeImplementation.relocateHome(player, homeName, newLocation);
    }

    @Override
    public List<Location> getHomesLocation(Player player) {
        return homeImplementation.getHomesLocation(player);
    }

    @Override
    public int getHomeNumber(Player player) {
        return homeImplementation.getHomeNumber(player);
    }

    @Override
    public List<String> getHomesName(Player player) {
        return homeImplementation.getHomesName(player);
    }

    @Override
    public CacheManager getCacheManager() {
        return HomePlugin.getCacheManager();
    }

    @Override
    public Location getHomeLocation(Player player, String homeName) {
        return homeImplementation.getHomeLocation(player, homeName);
    }

    @Override
    public boolean deleteHome(Player player, String homeName) {
        return homeImplementation.deleteHome(player, homeName);
    }

    @Override
    public boolean isStatus(Player player) {
        return StatusManager.getPlayerStatus(player);
    }

    @Override
    public boolean exist(Player player, String homeName) {
        return homeImplementation.exist(player, homeName);
    }
}