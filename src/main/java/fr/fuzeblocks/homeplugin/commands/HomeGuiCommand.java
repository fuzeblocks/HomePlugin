package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.gui.HomeGUI;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HomeGuiCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = ((Player) sender).getPlayer();
        HomeGUI homeGUI = new HomeGUI(HomeManager.getInstance().getHomesName(player));
        player.openInventory(homeGUI.getInventory());
        return false;
    }
}
