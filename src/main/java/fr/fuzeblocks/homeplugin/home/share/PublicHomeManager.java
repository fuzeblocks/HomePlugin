package fr.fuzeblocks.homeplugin.home.share;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.home.Home;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class PublicHomeManager implements Home {

    private final HomeManager homeManager = HomeManager.getInstance();
    private final UUID uuid = UUID.nameUUIDFromBytes("public".getBytes());

    @Override
    public boolean addHome(UUID uuid,Location location ,String name) {
        return homeManager.addHome(uuid, location, name + "_public");
    }

    public boolean addHome(Location location, String homeName) {
        return addHome(uuid,location, homeName);
    }

    @Override
    public List<Location> getHomesLocation(UUID uuid) {
        return getHomesName(uuid).stream()
                .map(homeName -> homeManager.getHomeLocation(uuid, homeName))
                .toList();
    }


    public List<Location> getHomesLocation() {
        return getHomesName(uuid).stream()
                .map(homeName -> homeManager.getHomeLocation(uuid, homeName))
                .toList();
    }

    @Override
    public int getHomeNumber(UUID uuid) {
        return getHomesName(uuid).size();
    }

    public int getHomeNumber() {
        return getHomesName(uuid).size();
    }

    @Override
    public List<String> getHomesName(UUID uuid) {
      return homeManager.getHomesName(uuid).stream()
                .filter(name -> name.endsWith("_public"))
                .toList();
    }

    public List<String> getHomesName() {
        return homeManager.getHomesName(uuid).stream()
                .filter(name -> name.endsWith("_public"))
                .toList();
    }

    public CacheManager getCacheManager() {
        return HomePlugin.getCacheManager();
    }
    @Override
    public Location getHomeLocation(UUID uuid, String homeName) {
        return homeManager.getHomeLocation(uuid,homeName + "_public");
    }
    public Location getHomeLocation(String homeName) {
        return homeManager.getHomeLocation(uuid,homeName + "_public");
    }

    @Override
    public boolean deleteHome(UUID uuid, String homeName) {
        return homeManager.deleteHome(uuid,homeName + "_public");
    }

    public boolean deleteHome(String homeName) {
        return homeManager.deleteHome(uuid,homeName + "_public");
    }

    public boolean isStatus(Player player) {
        return StatusManager.getPlayerStatus(player);
    }

    @Override
    public boolean exist(UUID uuid, String homeName) {
        return homeManager.exist(uuid,homeName + "_public");
    }
    public boolean exist(String homeName) {
        return homeManager.exist(uuid,homeName + "_public");
    }

    @Override
    public boolean renameHome(UUID uuid, String oldName, String newName) {
        return homeManager.renameHome(uuid, oldName + "_public", newName + "_public");
    }
    public boolean renameHome(String oldName, String newName) {
        return homeManager.renameHome(uuid, oldName + "_public", newName + "_public");
    }
}
