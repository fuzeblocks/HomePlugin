package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.api.event.OnSpawnCreatedEvent;
import fr.fuzeblocks.homeplugin.sync.type.SyncMethod;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {
    private final String key = "Config.Language.";
    private final String spawnKey = "Config.Spawn.";

    private OnSpawnCreatedEvent onSpawnCreate;


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (player.hasPermission(HomePlugin.getConfigurationSection().getString(spawnKey + "SetSpawn-permission"))) {
                Location location = player.getLocation();
                if (HomePlugin.getRegistrationType().equals(SyncMethod.MYSQL)) {
                    if (HomePlugin.getSpawnSQLManager().hasSpawn(location.getWorld())) {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Spawn-already-exists")));
                        return false;
                    }
                    onSpawnCreate = new OnSpawnCreatedEvent(player, location, SyncMethod.MYSQL);
                    Bukkit.getPluginManager().callEvent(onSpawnCreate);
                    if (!onSpawnCreate.isCancelled()) {
                        HomePlugin.getSpawnSQLManager().setSpawn(onSpawnCreate.getLocation());
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Spawn-has-been-set").replace("%x%", String.valueOf(location.getX())).replace("%y%", String.valueOf(location.getY())).replace("%z%", String.valueOf(location.getZ()))));
                    }
                    return true;
                } else {
                    if (HomePlugin.getSpawnYMLManager().hasSpawn(location.getWorld())) {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Spawn-already-exists")));
                        return false;
                    }
                    onSpawnCreate = new OnSpawnCreatedEvent(player, location, SyncMethod.YAML);
                    Bukkit.getPluginManager().callEvent(onSpawnCreate);
                    if (!onSpawnCreate.isCancelled()) {
                        HomePlugin.getSpawnYMLManager().setSpawn(onSpawnCreate.getLocation());
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Spawn-has-been-set").replace("%x%", String.valueOf(location.getX())).replace("%y%", String.valueOf(location.getY())).replace("%z%", String.valueOf(location.getZ()))));
                    }
                    return true;
                }
            } else {
                player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(spawnKey + "SetSpawn-permission-deny-message")));
            }
        } else {
            sender.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Only-a-player-can-execute")));
        }
        return false;
    }
}
