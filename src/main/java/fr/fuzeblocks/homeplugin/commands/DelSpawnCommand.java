package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.api.event.OnSpawnDelete;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelSpawnCommand implements CommandExecutor {
    private String key = "Config.Language.";
    private OnSpawnDelete onSpawnDelete;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (player.isOp()) {
                if (HomePlugin.getRegistrationType() == 1) {
                    onSpawnDelete = new OnSpawnDelete(player,player.getLocation(),1);
                    Bukkit.getPluginManager().callEvent(onSpawnDelete);
                    if (!onSpawnDelete.isCancelled()) {
                        HomePlugin.getSpawnSQLManager().removeSpawn(player.getWorld());
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Spawn-deleted")));
                    }
                } else {
                    onSpawnDelete = new OnSpawnDelete(player,player.getLocation(),0);
                    Bukkit.getPluginManager().callEvent(onSpawnDelete);
                    if (!onSpawnDelete.isCancelled()) {
                        HomePlugin.getSpawnManager().removeSpawn(onSpawnDelete.getLocation().getWorld());
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Spawn-deleted")));
                    }
                }
                return true;
            } else {
                player.sendMessage("§cVous n'étes pas op !");
            }
        } else {
            sender.sendMessage("§cSeul un joueur peut executer cette commande !");
        }
        return false;
    }
}