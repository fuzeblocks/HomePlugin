package fr.fuzeblocks.homeplugin.home.offline;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

/**
 * The type Home offline manager.
 */
public class HomeOfflineManager implements OfflineHome {

    private static HomeOfflineManager instance = null;
    private final OfflineHome homeImplementation;

    private HomeOfflineManager() {
        if (isYAML()) {
            this.homeImplementation = HomePlugin.getHomeOfflineYMLManager();
        } else {
            this.homeImplementation = HomePlugin.getHomeOfflineSQLManager();
        }
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static HomeOfflineManager getInstance() {
        if (instance == null) {
            instance = new HomeOfflineManager();
        }
        return instance;
    }

    @Override
    public boolean setHome(UUID uuid, String name, Location location) {
        return homeImplementation.setHome(uuid, name, location);
    }

    @Override
    public boolean renameHome(UUID uuid, String oldHomeName, String newHomeName) {
        return homeImplementation.renameHome(uuid,oldHomeName,newHomeName);
    }

    @Override
    public boolean relocateHome(UUID uuid, String homeName, Location newLocation) {
        return homeImplementation.relocateHome(uuid,homeName,newLocation);
    }

    @Override
    public List<Location> getHomesLocation(UUID uuid) {
        return homeImplementation.getHomesLocation(uuid);
    }

    @Override
    public int getHomeNumber(UUID uuid) {
        return homeImplementation.getHomeNumber(uuid);
    }

    @Override
    public List<String> getHomesName(UUID uuid) {
        return homeImplementation.getHomesName(uuid);
    }

    @Override
    public CacheManager getCacheManager() {
        return homeImplementation.getCacheManager();
    }

    @Override
    public Location getHomeLocation(UUID uuid, String homeName) {
        return homeImplementation.getHomeLocation(uuid,homeName);
    }

    @Override
    public boolean deleteHome(UUID uuid, String homeName) {
        return homeImplementation.deleteHome(uuid,homeName);
    }

    @Override
    public boolean exist(UUID uuid, String homeName) {
        return homeImplementation.exist(uuid,homeName);
    }
}
