package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.api.event.OnHomeDelete;
import fr.fuzeblocks.homeplugin.home.yml.HomeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelHomeCommand implements CommandExecutor {
    private String key = "Config.Language.";
    private OnHomeDelete onHomeDelete;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (args.length == 1) {
                String home_name = args[0];
                if (HomePlugin.getRegistrationType() == 1) {
                    onHomeDelete = new OnHomeDelete(player, HomePlugin.getHomeSQLManager().getHomeLocation(player,home_name),1,home_name);
                    if (!onHomeDelete.isCancelled()) {
                        if (HomePlugin.getHomeSQLManager().delHome(player, onHomeDelete.getHomeName()) && HomePlugin.getCacheManager().delHomeInCache(player, onHomeDelete.getHomeName())) {
                            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Home-deleted")));
                            return true;
                        }
                    }
                } else {
                    onHomeDelete = new OnHomeDelete(player, HomePlugin.getHomeManager().getHomeLocation(player,home_name),0,home_name);
                    if (!onHomeDelete.isCancelled()) {
                        if (HomePlugin.getHomeManager().delHome(player, onHomeDelete.getHomeName()) && HomePlugin.getCacheManager().delHomeInCache(player, onHomeDelete.getHomeName())) {
                            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Home-deleted")));
                            return true;
                        }
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
