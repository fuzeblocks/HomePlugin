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

import static fr.fuzeblocks.homeplugin.commands.home.SetHomeCommand.isFair;
import static fr.fuzeblocks.homeplugin.home.HomePermissionManager.canSetHome;

/**
 * Command to relocate an existing home to the player's current location.
 */
public class RelocateHomeCommand implements CommandExecutor {

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

        if (!player.hasPermission("homeplugin.command.relocatehome")) {
            player.sendMessage(languageManager.getStringWithColor(LANG + "No-permission"));
            return false;
        }

        if (args.length != 1) {
            player.sendMessage(languageManager.getStringWithColor(HOME + "Usage-relocate-home"));
            return false;
        }

        return relocateHome(player, args[0], languageManager);
    }

    /**
     * Relocates a home to the player's current location.
     *
     * @param player The player relocating the home
     * @param homeName The name of the home to relocate
     * @param languageManager The language manager instance
     * @return true if successful, false otherwise
     */
    private boolean relocateHome(Player player, String homeName, LanguageManager languageManager) {
        HomeManager homeManager = HomePlugin.getHomeManager();

        if (!homeManager.exist(player, homeName)) {
            player.sendMessage(languageManager.getStringWithColor(HOME + "Home-does-not-exist"));
            return false;
        }

        if (!canSetHome(player)) {
            return false;
        }

        if (HomePlugin.getConfigurationSection().getBoolean("Config.Home.PreventUnfairLocation", true)) {
            if (!isFair(player)) {
                return false;
            }
        }

        Location newLocation = player.getLocation();

        if (newLocation.getWorld() == null) {
            player.sendMessage(languageManager.getStringWithColor(HOME + "Invalid-world"));
            return false;
        }

        boolean success = homeManager.relocateHome(player, homeName, newLocation);

        if (success) {
            HomePlugin.getCacheManager().relocateHome(player.getUniqueId(), homeName, newLocation);
            player.sendMessage(languageManager.getStringWithColor(HOME + "Home-relocated")
                    .replace("{home}", homeName)
                    .replace("%home%", homeName));
        } else {
            player.sendMessage(languageManager.getStringWithColor(LANG + "Error"));
        }

        return success;
    }
}