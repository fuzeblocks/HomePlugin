package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.event.OnSpawnDeletedEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The type Delete spawn command.
 */
public class DeleteSpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String SPAWN = "Spawn.";
        String LANG = "Language.";
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (player.hasPermission("homeplugin.admin")) {
                OnSpawnDeletedEvent onSpawnDelete = new OnSpawnDeletedEvent(player, player.getLocation());
                    Bukkit.getPluginManager().callEvent(onSpawnDelete);
                    if (!onSpawnDelete.isCancelled()) {
                        HomePlugin.getSpawnManager().removeSpawn(onSpawnDelete.getLocation().getWorld());
                        player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(SPAWN + "Spawn-deleted"));
                    }
                } else {
                player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(LANG + "Player-is-not-OP"));
                }
                return true;
            } else {
            sender.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(LANG + "Only-a-player-can-execute"));
            }
        return false;
    }
}