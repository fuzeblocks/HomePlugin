package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.event.OnHomeDeletedEvent;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public  class DeleteHomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String LANG = "Language.";
        String HOME = "Home.";
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (!sender.hasPermission("homeplugin.command.delhome")) {
                sender.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(LANG + "No-permission"));
                return false;
            }
            if (args.length == 1) {
                String home_name = args[0];
                HomeManager homeManager = HomePlugin.getHomeManager();
                OnHomeDeletedEvent onHomeDelete = new OnHomeDeletedEvent(player, homeManager.getHomeLocation(player, home_name), home_name);
                    if (!onHomeDelete.isCancelled() && homeManager.deleteHome(player, onHomeDelete.getHomeName())) {
                            HomePlugin.getCacheManager().removeHome(player.getUniqueId(), onHomeDelete.getHomeName());
                            player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(HOME + "Home-deleted"));
                            return true;
                        }

            } else {
                player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(HOME +"DelHome-usage-message"));
            }
        } else {
            sender.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(LANG + "Only-a-player-can-execute"));
        }
        return false;
    }
}