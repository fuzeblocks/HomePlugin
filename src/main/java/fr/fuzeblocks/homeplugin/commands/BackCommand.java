package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.back.BackManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * The type Back command.
 */
public class BackCommand implements CommandExecutor {

    private static final String PERM_USE = "homeplugin.back.use";

    private static final String LANG = "Language.";
    private static final String BACK = "Back.";

    private final BackManager backManager = BackManager.getInstance();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(LANG + "Only-a-player-can-execute"));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission(PERM_USE)) {
            player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(LANG + "No-permission"));
            return true;
        }

        if (!backManager.hasLastLocation(player)) {
            player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(BACK + "No-previous-location"));
            return true;
        }

        Location current = player.getLocation().clone();
        Location target = backManager.getLastLocation(player).orElse(null);

        if (target == null || target.getWorld() == null) {
            player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(BACK + "Previous-location-invalid"));
            return true;
        }


        backManager.setLastLocation(player, current);

        boolean success = player.teleport(target, PlayerTeleportEvent.TeleportCause.PLUGIN);
        if (success) {
            player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(BACK + "Teleported"));
        } else {
            player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(BACK + "Teleport-failed"));
        }
        return true;
    }
}