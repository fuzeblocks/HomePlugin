package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.api.event.OnHomeDeletedEvent;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public  class DeleteHomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String key = "Language.";
        if (!(sender instanceof Player)) {
            sender.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Only-a-player-can-execute")));
        }

        Player player = ((Player) sender).getPlayer();
            if (args.length != 1) {
                player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Home.DelHome-usage-message")));
            }

            String home_name = args[0];
                HomeManager homeManager = HomePlugin.getHomeManager();
                OnHomeDeletedEvent onHomeDelete = new OnHomeDeletedEvent(player, homeManager.getHomeLocation(player, home_name), HomePlugin.getRegistrationType(), home_name);
                    if (!onHomeDelete.isCancelled() && homeManager.deleteHome(player, onHomeDelete.getHomeName())) {
                            HomePlugin.getCacheManager().delHomeInCache(player, onHomeDelete.getHomeName());
                            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Home-deleted")));
                            return true;
                        }

        return false;
    }
}