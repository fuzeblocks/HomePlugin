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

public class HomeYMLManager implements Home {
    private final File file;
    private final YamlConfiguration yaml;

    public HomeYMLManager(File file) {
        this.file = file;
        this.yaml = YamlConfiguration.loadConfiguration(file);
    }

    public boolean addHome(Player player, String name) {
        String key = player.getUniqueId() + ".Home";
        if (!yaml.contains(key + "." + name)) {
            Location location = player.getLocation();
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

    @Override
    public boolean setHome(Player player, String name, Location location) {
        String key = player.getUniqueId() + ".Home";
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

    public List<Location> getHomesLocation(Player player) {
        List<Location> homes = new ArrayList<>();
        if (getHomeNumber(player) > 0) {
            String key = player.getUniqueId() + ".Home.";
            Set<String> homeNames = yaml.getConfigurationSection(key).getKeys(false);
            for (String homeName : homeNames) {
                Location homeLocation = getHomeLocation(player, homeName);
                if (homeLocation != null) {
                    homes.add(homeLocation);
                }
            }
        }
        return homes;
    }

    public int getHomeNumber(Player player) {
        String key = player.getUniqueId() + ".Home.";
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

    public List<String> getHomesName(Player player) {
        List<String> home_names = new ArrayList<>();
        ConfigurationSection homesSection = yaml.getConfigurationSection(player.getUniqueId() + ".Home");
        if (homesSection != null) {
            Set<String> homeNames = homesSection.getKeys(false);
            home_names.addAll(homeNames);
        }
        return home_names;
    }

    public CacheManager getCacheManager() {
        return HomePlugin.getCacheManager();
    }

    public Location getHomeLocation(Player player, String homeName) {
        String key = player.getUniqueId() + ".Home." + homeName + ".";
        if (yaml.contains(key)) {
            return new Location(Bukkit.getWorld(yaml.getString(key + "World")), yaml.getDouble(key + "X"), yaml.getDouble(key + "Y"), yaml.getDouble(key + "Z"), (float) yaml.getDouble(key + "YAW"), (float) yaml.getDouble(key + "PITCH"));
        }
        return null;
    }

    public boolean deleteHome(Player player, String homeName) {
        String key = player.getUniqueId() + ".Home." + homeName;
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

    public boolean exist(Player player, String homeName) {
        String key = player.getUniqueId() + ".Home." + homeName;
        return yaml.contains(key);
    }
}
