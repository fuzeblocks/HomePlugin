package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.api.event.OnHomeDeletedEvent;
import fr.fuzeblocks.homeplugin.sync.type.SyncMethod;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public  class DeleteHomeCommand implements CommandExecutor {
    private final String key = "Language.";
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
                            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Home-deleted")));
                            return true;
                        }
                    }
                } else {
                    onHomeDelete = new OnHomeDeletedEvent(player, HomePlugin.getHomeYMLManager().getHomeLocation(player, home_name), SyncMethod.YAML, home_name);
                    if (!onHomeDelete.isCancelled()) {
                        if (HomePlugin.getHomeYMLManager().deleteHome(player, onHomeDelete.getHomeName()) && HomePlugin.getCacheManager().delHomeInCache(player, onHomeDelete.getHomeName())) {
                            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Home-deleted")));
                            return true;
                        }
                    }
                }
            } else {
                player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Home.DelHome-usage-message")));
            }
        } else {
            sender.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Only-a-player-can-execute")));
        }
        return false;
    }
}