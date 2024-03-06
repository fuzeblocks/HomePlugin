package fr.fuzeblocks.homeplugin.spawn.yml;

import fr.fuzeblocks.homeplugin.status.StatusManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;


public class SpawnManager {
    private YamlConfiguration yaml;
    private File file;

    public SpawnManager(File file) {
        this.file = file;
        yaml = YamlConfiguration.loadConfiguration(file);
    }
    public boolean setSpawn(Location location) {
        String key = "Spawn.";
        yaml.set(key + "X",location.getX());
        yaml.set(key + "Y",location.getY());
        yaml.set(key + "Z",location.getZ());
        yaml.set(key + "YAW",location.getYaw());
        yaml.set(key + "PITCH",location.getPitch());
        yaml.set(key + "World", location.getWorld().getName());
        try {
            yaml.save(file);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public Location getSpawn() {
        String key = "Spawn.";
        if (yaml.contains(key)) {
            return new Location(Bukkit.getWorld(yaml.getString(key + "World")),yaml.getDouble(key + "X"),yaml.getDouble(key + "Y"),yaml.getDouble(key + "Z"), (float) yaml.getDouble(key + "YAW"), (float) yaml.getDouble(key + "PITCH"));
        } else {
            return null;
        }
    }
    public boolean hasSpawn() {
        String key = "Spawn.";
        if (yaml.contains(key)) {
            return true;
        } else {
            return false;
        }
    }
    public boolean removeSpawn() {
        String key = "Spawn";
        yaml.set(key,null);
        try {
            yaml.save(file);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean isStatus(Player player) {
        if (StatusManager.getPlayerStatus(player)) {
            player.sendMessage("§cUne téléportation est déja en cours !");
            return true;
        } else {
            return false;
        }
    }
}
