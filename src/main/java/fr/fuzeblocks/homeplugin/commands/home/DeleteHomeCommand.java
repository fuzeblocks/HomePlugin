package fr.fuzeblocks.homeplugin.commands.home;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.event.OnHomeDeletedEvent;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command to delete a player's home.
 */
public class DeleteHomeCommand implements CommandExecutor {

    private static final String LANG = "Language.";
    private static final String HOME = "Home.";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final LanguageManager languageManager = HomePlugin.getLanguageManager();


        if (!(sender instanceof Player)) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Only-a-player-can-execute"));
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("homeplugin.command.delhome")) {
            player.sendMessage(languageManager.getStringWithColor(LANG + "No-permission"));
            return false;
        }

        if (args.length != 1) {
            player.sendMessage(languageManager.getStringWithColor(HOME + "DelHome-usage-message"));
            return false;
        }

        String homeName = args[0];
        HomeManager homeManager = HomePlugin.getHomeManager();

        Location homeLocation = homeManager.getHomeLocation(player, homeName);

        if (homeLocation == null) {
            player.sendMessage(languageManager.getStringWithColor(HOME + "Home-not-found"));
            return false;
        }

        OnHomeDeletedEvent event = new OnHomeDeletedEvent(player, homeLocation, homeName);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return false;
        }

        if (homeManager.deleteHome(player, event.getHomeName())) {
            HomePlugin.getCacheManager().removeHome(player.getUniqueId(), event.getHomeName());
            player.sendMessage(languageManager.getStringWithColor(HOME + "Home-deleted"));
            return true;
        }

        return false;
    }
}