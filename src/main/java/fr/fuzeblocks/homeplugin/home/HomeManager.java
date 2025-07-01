package fr.fuzeblocks.homeplugin.home;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.home.sql.HomeSQLManager;
import fr.fuzeblocks.homeplugin.home.yml.HomeYMLManager;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class HomeManager implements Home {

        private final HomeYMLManager homeYMLManager = HomePlugin.getHomeYMLManager();
        private final HomeSQLManager homeSQLManager = HomePlugin.getHomeSQLManager();
        private static HomeManager instance = null;

    private HomeManager() {
    }

    public static HomeManager getInstance() {
        if (instance == null) {
            instance = new HomeManager();
        }
        return instance;
    }


    public boolean addHome(UUID uuid, Location location ,String name) {
            if (isYAML()) {
                return homeYMLManager.addHome(uuid,location,name);
            } else {
                return homeSQLManager.addHome(uuid,location,name);
            }
        }

        public List<Location> getHomesLocation(UUID uuid) {
            if (isYAML()) {
                return homeYMLManager.getHomesLocation(uuid);
            } else {
                return homeSQLManager.getHomesLocation(uuid);
            }
        }

        public int getHomeNumber(UUID uuid) {
            if (isYAML()) {
                return homeYMLManager.getHomeNumber(uuid);
            } else {
                return homeSQLManager.getHomeNumber(uuid);
            }
        }

        public List<String> getHomesName(UUID uuid) {
           if (isYAML()) {
               return homeYMLManager.getHomesName(uuid);
           } else {
               return homeSQLManager.getHomesName(uuid);
           }
        }

        public CacheManager getCacheManager() {
            return HomePlugin.getCacheManager();
        }

        public Location getHomeLocation(UUID uuid, String homeName) {
           if (isYAML()) {
               return homeYMLManager.getHomeLocation(uuid,homeName);
           } else {
               return homeSQLManager.getHomeLocation(uuid,homeName);
           }
        }

        public boolean deleteHome(UUID uuid, String homeName) {
            if (isYAML()) {
                return homeYMLManager.deleteHome(uuid,homeName);
            } else {
                return homeSQLManager.deleteHome(uuid,homeName);
            }
        }

        public boolean isStatus(Player player) {
            return StatusManager.getPlayerStatus(player);
        }

        public boolean exist(UUID uuid, String homeName) {
            if (isYAML()) {
                return homeYMLManager.exist(uuid,homeName);
            } else {
                return homeSQLManager.exist(uuid,homeName);
            }
        }


        public boolean renameHome(UUID uuid, String oldName, String newName) {
            if (isYAML()) {
               return homeYMLManager.renameHome(uuid, oldName, newName);
            } else {
                return homeSQLManager.renameHome(uuid, oldName, newName);
            }
        }
}



