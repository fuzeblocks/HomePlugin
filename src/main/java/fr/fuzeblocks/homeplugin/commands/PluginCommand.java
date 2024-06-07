package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.plugin.HomePlugin;
import fr.fuzeblocks.homeplugin.plugin.PluginManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;



public class PluginCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
             sendMessage(((Player) sender).getPlayer());
        }
        return false;
    }
    private void sendMessage(Player player) {
        for (HomePlugin plugin : PluginManager.getInstance().getHomePlugin()) {
            player.sendMessage("§l§aName : " + plugin.getName() + ". Version : " + plugin.getVersion());
        }
        player.sendMessage("§l§aTotal : " + PluginManager.getInstance().getHomePlugin().size() + " " + "loaded");
    }
}
