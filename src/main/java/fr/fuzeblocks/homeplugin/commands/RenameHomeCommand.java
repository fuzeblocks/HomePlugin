package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The type Rename home command.
 */
public class RenameHomeCommand implements CommandExecutor {

    private String HOME = "Home.";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(HomePlugin.getLanguageManager().getStringWithColor("Language.Only-a-player-can-execute"));
            return false;
        }

        Player player = (Player) sender;

        HomeManager homeManager = HomePlugin.getHomeManager();

        if (args.length == 2) { //renamehome <oldHomeName> <newHomeName>
            String oldHomeName = args[0];
            String newHomeName = args[1];

            if (!homeManager.exist(player, oldHomeName)) {
                player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(HOME + "Home-does-not-exist"));
                return false;
            }

            if (homeManager.exist(player, newHomeName)) {
                player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(HOME + "Home-already-exists").replace("{home}", newHomeName));
                return false;
            }

            homeManager.renameHome(player, oldHomeName, newHomeName);
            player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(HOME + "Home-renamed").replace("{old_home}", oldHomeName).replace("{new_home}", newHomeName));
            return true;
        } else {
            player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(HOME + "Usage-edit-home"));
        }

        return false;
    }
}
