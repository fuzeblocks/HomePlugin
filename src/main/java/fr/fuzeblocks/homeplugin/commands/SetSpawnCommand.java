package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {
    private String key = "Config.Language.";
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (player.isOp()) {
                Location location = player.getLocation();
                if (HomePlugin.getRegistrationType() == 1) {
                    if (HomePlugin.getSpawnSQLManager().hasSpawn(location.getWorld())) {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Spawn-already-exists")));
                        return false;
                    }
                    HomePlugin.getSpawnSQLManager().setSpawn(location);
                    player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Spawn-has-been-set").replace("%x%",String.valueOf(location.getX())).replace("%y%",String.valueOf(location.getY())).replace("%z%",String.valueOf(location.getZ()))));
                    return true;
                } else {
                    if (HomePlugin.getSpawnManager().hasSpawn(location.getWorld())) {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Spawn-already-exists")));
                        return false;
                    }
                    HomePlugin.getSpawnManager().setSpawn(location);
                    player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Spawn-has-been-set").replace("%x%",String.valueOf(location.getX())).replace("%y%",String.valueOf(location.getY())).replace("%z%",String.valueOf(location.getZ()))));
                    return true;
                }
            } else {
                player.sendMessage("§cVous n'étes pas op !");
            }
         } else {
            sender.sendMessage("§cSeul un joueur peut executer cette commande !");
        }
        return false;
    }
}
