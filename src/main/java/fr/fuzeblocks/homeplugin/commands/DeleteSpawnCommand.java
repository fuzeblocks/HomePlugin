package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.api.event.OnSpawnDeletedEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteSpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String SPAWN = "Spawn.";
        String LANG = "Language.";
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (player.hasPermission("homeplugin.admin")) {
                OnSpawnDeletedEvent onSpawnDelete = new OnSpawnDeletedEvent(player, player.getLocation(), HomePlugin.getRegistrationType());
                    Bukkit.getPluginManager().callEvent(onSpawnDelete);
                    if (!onSpawnDelete.isCancelled()) {
                        HomePlugin.getSpawnManager().removeSpawn(onSpawnDelete.getLocation().getWorld());
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(SPAWN + "Spawn-deleted")));
                    }
                } else {
                player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(LANG + "Player-is-not-OP")));
                }
                return true;
            } else {
            sender.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(LANG + "Only-a-player-can-execute")));
            }
        return false;
    }
}