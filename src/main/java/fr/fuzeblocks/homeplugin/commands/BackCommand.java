package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.back.BackManager;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * The type Back command.
 */
/*
 * The type Back command.
 *
 * Command `/back` — teleports the player to their last recorded location.
 *
 */
public class BackCommand implements CommandExecutor {

    private static final String PERM_USE = "homeplugin.back.use";
    private static final String LANG = "Language.";
    private static final String BACK = "Back.";

    private final BackManager backManager = BackManager.getInstance();
    private final LanguageManager languageManager = HomePlugin.getLanguageManager();
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Only-a-player-can-execute"));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission(PERM_USE)) {
            player.sendMessage(languageManager.getStringWithColor(LANG + "No-permission"));
            return true;
        }

        if (!backManager.hasLastLocation(player)) {
            player.sendMessage(languageManager.getStringWithColor(BACK + "No-previous-location"));
            return true;
        }

        Location current = player.getLocation().clone();
        Location target = backManager.getLastLocation(player).orElse(null);

        if (target == null || target.getWorld() == null) {
            player.sendMessage(languageManager.getStringWithColor(BACK + "Previous-location-invalid"));
            return true;
        }


        backManager.setLastLocation(player, current);

        boolean success = player.teleport(target, PlayerTeleportEvent.TeleportCause.PLUGIN);
        if (success) {
            player.sendMessage(languageManager.getStringWithColor(BACK + "Teleported"));
        } else {
            player.sendMessage(languageManager.getStringWithColor(BACK + "Teleport-failed"));
        }
        return true;
    }
}