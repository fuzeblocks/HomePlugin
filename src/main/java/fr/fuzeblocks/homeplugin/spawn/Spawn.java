package fr.fuzeblocks.homeplugin.spawn;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.sync.SyncMethod;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface Spawn {
    public boolean setSpawn(Location location);
    public Location getSpawn(World world);
    public boolean hasSpawn(World world);

    public boolean removeSpawn(World world);

    public boolean isStatus(Player player);

    default boolean isYAML() {
        return HomePlugin.getRegistrationType().equals(SyncMethod.YAML);
    }
}
