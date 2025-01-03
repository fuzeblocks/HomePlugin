package fr.fuzeblocks.homeplugin.home;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.home.sql.HomeSQLManager;
import fr.fuzeblocks.homeplugin.home.yml.HomeYMLManager;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import fr.fuzeblocks.homeplugin.sync.type.SyncMethod;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class HomeManager {

        private HomeYMLManager homeYMLManager = HomePlugin.getHomeYMLManager();
        private HomeSQLManager homeSQLManager = HomePlugin.getHomeSQLManager();
        private static HomeManager instance = null;

    private HomeManager() {
    }

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
        private boolean isYAML() {
            return HomePlugin.getRegistrationType().equals(SyncMethod.YAML);
        }
    }



