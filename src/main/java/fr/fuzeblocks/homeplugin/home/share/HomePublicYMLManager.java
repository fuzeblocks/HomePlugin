package fr.fuzeblocks.homeplugin.home.share;

import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.home.offline.yml.HomeOfflineYMLManager;
import fr.fuzeblocks.homeplugin.home.yml.HomeYMLManager;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class HomePublicYMLManager implements PublicHome {


    private final File file;
    private final YamlConfiguration yaml;
    private final HomeYMLManager homeYMLManager;
    private final HomeOfflineYMLManager offlineYMLManager;


    public HomePublicYMLManager(File file) {
        this.file = file;
        this.yaml = YamlConfiguration.loadConfiguration(file);
        this.homeYMLManager = new HomeYMLManager(file);
        this.offlineYMLManager = new HomeOfflineYMLManager(file);
    }

    @Override
    public boolean addHome(Player player, String name) {
        return homeYMLManager.addHome(player, name);
    }

    @Override
    public boolean setHome(Player player, String name, Location location) {
        return homeYMLManager.setHome(player, name, location);
    }

    @Override
    public boolean renameHome(Player player, String oldHomeName, String newHomeName) {
        return homeYMLManager.renameHome(player, oldHomeName, newHomeName);
    }

    @Override
    public boolean relocateHome(Player player, String homeName, Location newLocation) {
        return homeYMLManager.relocateHome(player, homeName, newLocation);
    }

    @Override
    public List<Location> getHomesLocation(Player player) {
        return homeYMLManager.getHomesLocation(player);
    }

    @Override
    public int getHomeNumber(Player player) {
        return homeYMLManager.getHomeNumber(player);
    }

    @Override
    public List<String> getHomesName(Player player) {
        return homeYMLManager.getHomesName(player);
    }

    @Override
    public Location getHomeLocation(Player player, String homeName) {
        return homeYMLManager.getHomeLocation(player, homeName);
    }

    @Override
    public boolean deleteHome(Player player, String homeName) {
        return homeYMLManager.deleteHome(player, homeName);
    }

    @Override
    public boolean exist(Player player, String homeName) {
        return homeYMLManager.exist(player, homeName);
    }

    @Override
    public boolean isStatus(Player player) {
        return homeYMLManager.isStatus(player);
    }

    @Override
    public boolean setHome(UUID uuid, String name, Location location) {
        return offlineYMLManager.setHome(uuid, name, location);
    }


    @Override
    public boolean renameHome(UUID uuid, String oldHomeName, String newHomeName) {
        return offlineYMLManager.renameHome(uuid, oldHomeName, newHomeName);
    }

    @Override
    public boolean relocateHome(UUID uuid, String homeName, Location newLocation) {
        return offlineYMLManager.relocateHome(uuid, homeName, newLocation);
    }

    @Override
    public List<Location> getHomesLocation(UUID uuid) {
        return offlineYMLManager.getHomesLocation(uuid);
    }

    @Override
    public int getHomeNumber(UUID uuid) {
        return offlineYMLManager.getHomeNumber(uuid);
    }

    @Override
    public List<String> getHomesName(UUID uuid) {
        return offlineYMLManager.getHomesName(uuid);
    }

    @Override
    public Location getHomeLocation(UUID uuid, String homeName) {
        return offlineYMLManager.getHomeLocation(uuid, homeName);
    }

    @Override
    public boolean deleteHome(UUID uuid, String homeName) {
        return offlineYMLManager.deleteHome(uuid, homeName);
    }

    @Override
    public boolean exist(UUID uuid, String homeName) {
        return offlineYMLManager.exist(uuid, homeName);
    }

    @Override
    public CacheManager getCacheManager() {
        return homeYMLManager.getCacheManager();
    }

    @Override
    public void addPlayerToBlackList(Player owner, Player target, String homeName) {
        addPlayerToBlackList_(owner.getUniqueId(), target.getUniqueId(), homeName);
    }

    @Override
    public void addPlayerToBlackList(UUID ownerUUID, UUID targetUUID, String homeName) {
        addPlayerToBlackList_(ownerUUID, targetUUID, homeName);
    }

    @Override
    public void removePlayerFromBlackList(Player owner, Player target, String homeName) {
        removePlayerToBlackList_(owner.getUniqueId(), target.getUniqueId(), homeName);
    }

    @Override
    public void removePlayerFromBlackList(UUID ownerUUID, UUID targetUUID, String homeName) {
        removePlayerToBlackList_(ownerUUID, targetUUID, homeName);
    }

    @Override
    public boolean isPlayerInBlackList(Player owner, Player target, String homeName) {
        return isPlayerInBlackList_(owner.getUniqueId(), target.getUniqueId(), homeName);
    }

    @Override
    public boolean isPlayerInBlackList(UUID ownerUUID, UUID targetUUID, String homeName) {
        return isPlayerInBlackList_(ownerUUID, targetUUID, homeName);
    }

    @Override
    public List<UUID> getBlackList(UUID ownerUUID, String homeName) {
        return getBlackList_(ownerUUID, homeName);
    }

    @Override
    public List<UUID> getBlackList(Player owner, String homeName) {
        return getBlackList_(owner.getUniqueId(), homeName);
    }

    private void addPlayerToBlackList_(UUID ownerUUID, UUID targetUUID, String homeName) {
        String key = ownerUUID.toString() + ".Home." + homeName + ".BlackList." + targetUUID.toString();
        yaml.set(key, true);
        try {
            yaml.save(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void removePlayerToBlackList_(UUID ownerUUID, UUID targetUUID, String homeName) {
            String key = ownerUUID.toString() + ".Home." + homeName + ".BlackList." + targetUUID.toString();
            yaml.set(key, null);
            try {
                yaml.save(file);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        private boolean isPlayerInBlackList_(UUID ownerUUID, UUID targetUUID, String homeName) {
            String key = ownerUUID.toString() + ".Home." + homeName + ".BlackList." + targetUUID.toString();
            return yaml.contains(key) && yaml.getBoolean(key);
        }
      private List<UUID> getBlackList_(UUID ownerUUID, String homeName) {

        String key = ownerUUID.toString() + ".Home." + homeName + ".BlackList";

        List<UUID> blackList = new java.util.ArrayList<>();

        if (!yaml.isConfigurationSection(key)) {
            return blackList;
        }

        for (String uuidString : yaml.getConfigurationSection(key).getKeys(false)) {
            try {
                blackList.add(UUID.fromString(uuidString));
            } catch (IllegalArgumentException ignored) {
            }
        }

        return blackList;
    }

}
