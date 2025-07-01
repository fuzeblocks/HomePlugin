package fr.fuzeblocks.homeplugin.home.yml;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.home.Home;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class HomeYMLManager implements Home {
    private final File file;
    private final YamlConfiguration yaml;

    public HomeYMLManager(File file) {
        this.file = file;
        this.yaml = YamlConfiguration.loadConfiguration(file);
    }

    public boolean addHome(UUID uuid, Location location ,String name) {
        String key = uuid + ".Home";
        if (!yaml.contains(key + "." + name)) {
            yaml.set(key + "." + name + ".X", location.getX());
            yaml.set(key + "." + name + ".Y", location.getY());
            yaml.set(key + "." + name + ".Z", location.getZ());
            yaml.set(key + "." + name + ".PITCH", location.getPitch());
            yaml.set(key + "." + name + ".YAW", location.getYaw());
            yaml.set(key + "." + name + ".World", location.getWorld().getName());
            try {
                yaml.save(file);
                return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return false;
        }
    }

    public List<Location> getHomesLocation(UUID uuid) {
        List<Location> homes = new ArrayList<>();
        if (getHomeNumber(uuid) > 0) {
            String key = uuid + ".Home.";
            Set<String> homeNames = yaml.getConfigurationSection(key).getKeys(false);
            for (String homeName : homeNames) {
                Location homeLocation = getHomeLocation(uuid, homeName);
                if (homeLocation != null) {
                    homes.add(homeLocation);
                }
            }
        }
        return homes;
    }

    public int getHomeNumber(UUID uuid) {
        String key = uuid + ".Home.";
        try {
            ConfigurationSection configurationSection = yaml.getConfigurationSection(key);

            if (configurationSection == null) {
                return 0;
            }
            Set<String> home = configurationSection.getKeys(false);
            return home.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public List<String> getHomesName(UUID uuid) {
        List<String> home_names = new ArrayList<>();
        ConfigurationSection homesSection = yaml.getConfigurationSection(uuid + ".Home");
        if (homesSection != null) {
            Set<String> homeNames = homesSection.getKeys(false);
            home_names.addAll(homeNames);
        }
        return home_names;
    }

    public CacheManager getCacheManager() {
        return HomePlugin.getCacheManager();
    }

    public Location getHomeLocation(UUID uuid, String homeName) {
        String key = uuid + ".Home." + homeName + ".";
        if (yaml.contains(key)) {
            return new Location(Bukkit.getWorld(yaml.getString(key + "World")), yaml.getDouble(key + "X"), yaml.getDouble(key + "Y"), yaml.getDouble(key + "Z"), (float) yaml.getDouble(key + "YAW"), (float) yaml.getDouble(key + "PITCH"));
        }
        return null;
    }

    public boolean deleteHome(UUID uuid, String homeName) {
        String key = uuid + ".Home." + homeName;
        if (yaml.contains(key)) {
            yaml.set(key, null);
            try {
                yaml.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean isStatus(Player player) {
        return StatusManager.getPlayerStatus(player);
    }

    public boolean exist(UUID uuid, String homeName) {
        String key = uuid + ".Home." + homeName;
        return yaml.contains(key);
    }
    public boolean renameHome(UUID uuid, String oldName, String newName) {
        if (exist(uuid, oldName) && !exist(uuid, newName)) {
            Location location = getHomeLocation(uuid, oldName);
            if (location != null) {
                deleteHome(uuid, oldName);
                yaml.set(uuid + ".Home." + newName + ".X", location.getX());
                yaml.set(uuid + ".Home." + newName + ".Y", location.getY());
                yaml.set(uuid + ".Home." + newName + ".Z", location.getZ());
                yaml.set(uuid + ".Home." + newName + ".PITCH", location.getPitch());
                yaml.set(uuid + ".Home." + newName + ".YAW", location.getYaw());
                yaml.set(uuid + ".Home." + newName + ".World", location.getWorld().getName());
                try {
                    yaml.save(file);
                    return true;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }
}
