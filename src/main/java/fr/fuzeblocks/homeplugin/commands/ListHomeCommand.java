package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ListHomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            HomeManager homeManager = HomePlugin.getHomeManager();
            player.sendMessage("Home number : " + homeManager.getHomeNumber(player));
            for (String name : homeManager.getHomesName(player)) {
                player.sendMessage("Home : " + name);
            }
        }
        return false;
    }
}
