package fr.fuzeblocks.homeplugin.spawn.yml;

import fr.fuzeblocks.homeplugin.spawn.Spawn;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class SpawnYMLManager implements Spawn {
    private final YamlConfiguration yaml;
    private final File file;

    public SpawnYMLManager(File file) {
        this.file = file;
        yaml = YamlConfiguration.loadConfiguration(file);
    }

    public boolean setSpawn(Location location) {
        String key = "Spawn." + location.getWorld().getUID();
        yaml.set(key + ".X", location.getX());
        yaml.set(key + ".Y", location.getY());
        yaml.set(key + ".Z", location.getZ());
        yaml.set(key + ".YAW", location.getYaw());
        yaml.set(key + ".PITCH", location.getPitch());
        yaml.set(key + ".World", location.getWorld().getName());
        try {
            yaml.save(file);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Location getSpawn(World world) {
        String key = "Spawn." + world.getUID();
        if (yaml.contains(key)) {
            return new Location(Bukkit.getWorld(yaml.getString(key + ".World")), yaml.getDouble(key + ".X"), yaml.getDouble(key + ".Y"), yaml.getDouble(key + ".Z"), (float) yaml.getDouble(key + ".YAW"), (float) yaml.getDouble(key + ".PITCH"));
        } else {
            return null;
        }
    }

    public boolean hasSpawn(World world) {
        String key = "Spawn." + world.getUID();
        try {
            Objects.requireNonNull(yaml.getString(key)).equalsIgnoreCase(world.getUID().toString());
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    public boolean removeSpawn(World world) {
        String key = "Spawn." + world.getUID();
        yaml.set(key, null);
        try {
            yaml.save(file);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean isStatus(Player player) {
        return StatusManager.getPlayerStatus(player);
    }
}
