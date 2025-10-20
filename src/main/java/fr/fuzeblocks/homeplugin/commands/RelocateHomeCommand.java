package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static fr.fuzeblocks.homeplugin.commands.SetHomeCommand.isFair;
import static fr.fuzeblocks.homeplugin.home.HomePermissionManager.canSetHome;

/**
 * The type Relocate home command.
 */
public class RelocateHomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String HOME = "Home.";
        if (!(sender instanceof Player)) {
            sender.sendMessage(HomePlugin.getLanguageManager().getStringWithColor("Language.Only-a-player-can-execute"));
            return false;
        }

        Player player = (Player) sender;
        HomeManager homeManager = HomePlugin.getHomeManager();

        if (args.length == 1) {
            String homeName = args[0];
            if (!homeManager.exist(player, homeName)) {
                player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(HOME + "Home-does-not-exist"));
                return false;
            }

            if (!canSetHome(player)) return false;

            if (HomePlugin.getConfigurationSection().getBoolean("Config.Home.PreventUnfairLocation", true)) {
                if (!isFair(player)) return false;
            }

            homeManager.relocateHome(player, homeName, player.getLocation());
            player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(HOME + "Home-relocated").replace("{home}", homeName));
            return true;
        } else {
            player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(HOME + "Usage-relocate-home"));
        }

        return false;
    }
}