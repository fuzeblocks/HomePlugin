package fr.fuzeblocks.homeplugin.home;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.home.sql.HomeSQLManager;
import fr.fuzeblocks.homeplugin.home.yml.HomeYMLManager;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import fr.fuzeblocks.homeplugin.sync.SyncMethod;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * The type Home manager.
 */
public class HomeManager implements Home {

        private final HomeYMLManager homeYMLManager = HomePlugin.getHomeYMLManager();
        private final HomeSQLManager homeSQLManager = HomePlugin.getHomeSQLManager();
        private static HomeManager instance = null;

    private HomeManager() {
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


    public boolean addHome(Player player, String name) {
            if (isYAML()) {
                return homeYMLManager.addHome(player,name);
            } else {
                return homeSQLManager.addHome(player,name);
            }
        }

    @Override
    public boolean setHome(Player player, String name, Location location) {
        if (isYAML()) {
            return homeYMLManager.setHome(player, name, location);
        } else {
            return homeSQLManager.setHome(player, name, location);
        }
    }

    public List<Location> getHomesLocation(Player player) {
            if (isYAML()) {
                return homeYMLManager.getHomesLocation(player);
            } else {
                return homeSQLManager.getHomesLocation(player);
            }
        }

        public int getHomeNumber(Player player) {
            if (isYAML()) {
                return homeYMLManager.getHomeNumber(player);
            } else {
                return homeSQLManager.getHomeNumber(player);
            }
        }

        public List<String> getHomesName(Player player) {
           if (isYAML()) {
               return homeYMLManager.getHomesName(player);
           } else {
               return homeSQLManager.getHomesName(player);
           }
        }

        public CacheManager getCacheManager() {
            return HomePlugin.getCacheManager();
        }

        public Location getHomeLocation(Player player, String homeName) {
           if (isYAML()) {
               return homeYMLManager.getHomeLocation(player,homeName);
           } else {
               return homeSQLManager.getHomeLocation(player,homeName);
           }
        }

        public boolean deleteHome(Player player, String homeName) {
            if (isYAML()) {
                return homeYMLManager.deleteHome(player,homeName);
            } else {
                return homeSQLManager.deleteHome(player,homeName);
            }
        }

        public boolean isStatus(Player player) {
            return StatusManager.getPlayerStatus(player);
        }

        public boolean exist(Player player, String homeName) {
            if (isYAML()) {
                return homeYMLManager.exist(player,homeName);
            } else {
                return homeSQLManager.exist(player,homeName);
            }
        }
    }



