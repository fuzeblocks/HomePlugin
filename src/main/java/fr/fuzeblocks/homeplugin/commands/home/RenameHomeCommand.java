package fr.fuzeblocks.homeplugin.commands.home;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Command to rename an existing home.
 */
public class RenameHomeCommand implements CommandExecutor {

    private static final String HOME = "Home.";
    private static final String LANG = "Language.";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final LanguageManager languageManager = HomePlugin.getLanguageManager();

        if (!(sender instanceof Player)) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Only-a-player-can-execute"));
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("homeplugin.command.renamehome")) {
            player.sendMessage(languageManager.getStringWithColor(LANG + "No-permission"));
            return false;
        }

        if (args.length != 2) {
            player.sendMessage(languageManager.getStringWithColor(HOME + "Usage-rename-home"));
            return false;
        }

        return renameHome(player, args[0], args[1], languageManager);
    }

    /**
     * Renames a home from old name to new name.
     *
     * @param player The player renaming the home
     * @param oldHomeName The current home name
     * @param newHomeName The new home name
     * @param languageManager The language manager instance
     * @return true if successful, false otherwise
     */
    private boolean renameHome(Player player, String oldHomeName, String newHomeName, LanguageManager languageManager) {
        HomeManager homeManager = HomePlugin.getHomeManager();

        if (!homeManager.exist(player, oldHomeName)) {
            player.sendMessage(languageManager.getStringWithColor(HOME + "Home-does-not-exist"));
            return false;
        }

        if (homeManager.exist(player, newHomeName)) {
            player.sendMessage(languageManager.getStringWithColor(HOME + "Home-already-exists")
                    .replace("{home}", newHomeName)
                    .replace("%home%", newHomeName));
            return false;
        }

        Location homeLocation = homeManager.getHomeLocation(player, oldHomeName);

        if (homeLocation == null) {
            player.sendMessage(languageManager.getStringWithColor(LANG + "Error"));
            return false;
        }

        boolean success = homeManager.renameHome(player, oldHomeName, newHomeName);

        if (success) {
            HomePlugin.getCacheManager().removeHome(player.getUniqueId(), oldHomeName);
            HomePlugin.getCacheManager().addHome(player.getUniqueId(), newHomeName, homeLocation);

            player.sendMessage(languageManager.getStringWithColor(HOME + "Home-renamed")
                    .replace("{old_home}", oldHomeName)
                    .replace("%old_home%", oldHomeName)
                    .replace("{new_home}", newHomeName)
                    .replace("%new_home%", newHomeName));
        } else {
            player.sendMessage(languageManager.getStringWithColor(LANG + "Error"));
        }

        return success;
    }
}