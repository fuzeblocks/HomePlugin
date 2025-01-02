package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.api.event.OnHomeDeletedEvent;
import fr.fuzeblocks.homeplugin.sync.type.SyncMethod;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public  class DeleteHomeCommand implements CommandExecutor {
    private final String key = "Config.Language.";
    private OnHomeDeletedEvent onHomeDelete;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (args.length == 1) {
                String home_name = args[0];
                if (HomePlugin.getRegistrationType().equals(SyncMethod.MYSQL)) {
                    onHomeDelete = new OnHomeDeletedEvent(player, HomePlugin.getHomeSQLManager().getHomeLocation(player, home_name), SyncMethod.MYSQL, home_name);
                    if (!onHomeDelete.isCancelled()) {
                        if (HomePlugin.getHomeSQLManager().deleteHome(player, onHomeDelete.getHomeName()) && HomePlugin.getCacheManager().delHomeInCache(player, onHomeDelete.getHomeName())) {
                            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Home-deleted")));
                            return true;
                        }
                    }
                } else {
                    onHomeDelete = new OnHomeDeletedEvent(player, HomePlugin.getHomeManager().getHomeLocation(player, home_name), SyncMethod.YAML, home_name);
                    if (!onHomeDelete.isCancelled()) {
                        if (HomePlugin.getHomeManager().deleteHome(player, onHomeDelete.getHomeName()) && HomePlugin.getCacheManager().delHomeInCache(player, onHomeDelete.getHomeName())) {
                            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Home-deleted")));
                            return true;
                        }
                    }
                }
            } else {
                player.sendMessage("§cUtilisation de la commande : /delhome <nom-du-home>");
            }
        } else {
            sender.sendMessage("§cSeul un joueur peut executer cette commande !");
        }
        return false;
    }
}
