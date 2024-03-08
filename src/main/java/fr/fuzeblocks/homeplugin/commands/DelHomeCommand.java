package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelHomeCommand implements CommandExecutor {
    private String key = "Config.Language.";
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (args.length == 1) {
                String home_name = args[0];
                if (HomePlugin.getRegistrationType() == 1) {
                    if (HomePlugin.getHomeSQLManager().delHome(player, home_name) && HomePlugin.getCacheManager().delHomeInCache(player, home_name)) {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Home-deleted")));
                        return true;
                    }
                } else {
                    if (HomePlugin.getHomeManager().delHome(player, home_name) && HomePlugin.getCacheManager().delHomeInCache(player, home_name)) {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Home-deleted")));
                        return true;
                    } else {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Home-does-not-exist")));
                    }
                }
            }  else {
                player.sendMessage("§cUtilisation de la commande : /delhome <nom-du-home>");
            }
        } else {
            sender.sendMessage("§cSeul un joueur peut executer cette commande !");
        }
        return false;
    }
}
