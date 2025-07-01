package fr.fuzeblocks.homeplugin.home;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class PublicHomeManager implements Home {

    private final YamlConfiguration yaml;
    private final File file;

    public PublicHomeManager(File file) {
        this.file = file;
        this.yaml = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public boolean addHome(UUID uuid, Location location, String name) {
        String path = uuid + ".Home." + name;
        if (yaml.contains(path)) return false;

        yaml.set(path + ".world", location.getWorld().getName());
        yaml.set(path + ".x", location.getX());
        yaml.set(path + ".y", location.getY());
        yaml.set(path + ".z", location.getZ());
        yaml.set(path + ".yaw", location.getYaw());
        yaml.set(path + ".pitch", location.getPitch());
        yaml.set(path + ".name", name);
        yaml.set(path + ".description", "");
        yaml.set(path + ".isPublic", true);
        yaml.set(path + ".isActive", true);

        return saveFile();
    }

    @Override
    public List<Location> getHomesLocation(UUID uuid) {
        return yaml.getConfigurationSection(uuid + ".Home").getKeys(false).stream()
                .map(name -> getHomeLocation(uuid, name))
                .filter(loc -> loc != null)
                .toList();
    }

    @Override
    public int getHomeNumber(UUID uuid) {
        return yaml.getConfigurationSection(uuid + ".Home").getKeys(false).size();
    }

    @Override
    public List<String> getHomesName(UUID uuid) {
        return List.copyOf(yaml.getConfigurationSection(uuid + ".Home").getKeys(false));
    }

    @Override
    public CacheManager getCacheManager() {
        return HomePlugin.getCacheManager();
    }

    @Override
    public Location getHomeLocation(UUID uuid, String homeName) {
        String path = uuid + ".Home." + homeName;
        if (!yaml.contains(path + ".world")) return null;

        World world = HomePlugin.getPlugin(HomePlugin.class).getServer().getWorld(yaml.getString(path + ".world"));
        if (world == null) throw new NullPointerException("World not found: " + yaml.getString(path + ".world"));

        return new Location(world, yaml.getDouble(path + ".x"), yaml.getDouble(path + ".y"),
                yaml.getDouble(path + ".z"), (float) yaml.getDouble(path + ".yaw"), (float) yaml.getDouble(path + ".pitch"));
    }

    @Override
    public boolean deleteHome(UUID uuid, String homeName) {
        yaml.set(uuid + ".Home." + homeName, null);
        return saveFile();
    }

    @Override
    public boolean isStatus(Player player) {
        return yaml.getConfigurationSection(player.getUniqueId() + ".Home").getKeys(false).stream()
                .anyMatch(name -> yaml.getBoolean(player.getUniqueId() + ".Home." + name + ".isPublic", false));
    }

    @Override
    public boolean exist(UUID uuid, String homeName) {
        return yaml.contains(uuid + ".Home." + homeName);
    }

    @Override
    public boolean renameHome(UUID uuid, String oldName, String newName) {
        String oldPath = uuid + ".Home." + oldName;
        String newPath = uuid + ".Home." + newName;
        if (!yaml.contains(oldPath) || yaml.contains(newPath)) return false;

        yaml.set(newPath, yaml.get(oldPath));
        yaml.set(oldPath, null);
        yaml.set(newPath + ".name", newName);

        return saveFile();
    }

    public boolean setPublic(UUID uuid, String homeName, boolean isPublic) {
        yaml.set(uuid + ".Home." + homeName + ".isPublic", isPublic);
        return saveFile();
    }

    public boolean setActive(UUID uuid, String homeName, boolean isActive) {
        yaml.set(uuid + ".Home." + homeName + ".isActive", isActive);
        return saveFile();
    }

    public boolean setDescription(UUID uuid, String homeName, String description) {
        yaml.set(uuid + ".Home." + homeName + ".description", description);
        return saveFile();
    }

    public boolean addAllowedPlayer(UUID uuid, String homeName, UUID playerToAdd) {
        List<String> list = yaml.getStringList(uuid + ".Home." + homeName + ".allowedPlayers");
        if (!list.contains(playerToAdd.toString())) list.add(playerToAdd.toString());
        yaml.set(uuid + ".Home." + homeName + ".allowedPlayers", list);
        return saveFile();
    }

    public boolean removeAllowedPlayer(UUID uuid, String homeName, UUID playerToRemove) {
        List<String> list = yaml.getStringList(uuid + ".Home." + homeName + ".allowedPlayers");
        if (!list.remove(playerToRemove.toString())) return false;
        yaml.set(uuid + ".Home." + homeName + ".allowedPlayers", list);
        return saveFile();
    }

    public boolean addDeniedPlayer(UUID uuid, String homeName, UUID playerToAdd) {
        List<String> list = yaml.getStringList(uuid + ".Home." + homeName + ".deniedPlayers");
        if (!list.contains(playerToAdd.toString())) list.add(playerToAdd.toString());
        yaml.set(uuid + ".Home." + homeName + ".deniedPlayers", list);
        return saveFile();
    }

    public boolean removeDeniedPlayer(UUID uuid, String homeName, UUID playerToRemove) {
        List<String> list = yaml.getStringList(uuid + ".Home." + homeName + ".deniedPlayers");
        if (!list.remove(playerToRemove.toString())) return false;
        yaml.set(uuid + ".Home." + homeName + ".deniedPlayers", list);
        return saveFile();
    }

    public boolean isPlayerAllowed(UUID uuid, String homeName, UUID playerToCheck) {
        List<String> allowed = yaml.getStringList(uuid + ".Home." + homeName + ".allowedPlayers");
        List<String> denied = yaml.getStringList(uuid + ".Home." + homeName + ".deniedPlayers");
        return allowed.contains(playerToCheck.toString()) && !denied.contains(playerToCheck.toString());
    }

    private boolean saveFile() {
        try {
            yaml.save(file);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}