package fr.fuzeblocks.homeplugin.commands.tpa;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.event.OnTpaDeniedEvent;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import fr.fuzeblocks.homeplugin.tpa.TpaManager;
import fr.fuzeblocks.homeplugin.tpa.TpaRequest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command to deny a teleport request from another player.
 */
public class TPADenyCommand implements CommandExecutor {

    private static final String TPA = "TpaCommand.";
    private static final String LANG = "Language.";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final LanguageManager languageManager = HomePlugin.getLanguageManager();

        if (!(sender instanceof Player)) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Only-a-player-can-execute"));
            return false;
        }

        Player target = (Player) sender;

        if (!target.hasPermission("homeplugin.command.tpa")) {
            target.sendMessage(languageManager.getStringWithColor(LANG + "No-permission"));
            return false;
        }

        if (args.length != 1) {
            target.sendMessage(languageManager.getStringWithColor(TPA + "Tpa-deny-usage"));
            return false;
        }

        return denyTpaRequest(target, args[0], languageManager);
    }

    /**
     * Denies a TPA request from a specific player.
     *
     * @param target The player denying the request
     * @param senderName The name of the player who sent the request
     * @param languageManager The language manager instance
     * @return true if successful, false otherwise
     */
    private boolean denyTpaRequest(Player target, String senderName, LanguageManager languageManager) {
        Player senderPlayer = Bukkit.getPlayer(senderName);

        if (senderPlayer == null || !senderPlayer.isOnline()) {
            target.sendMessage(languageManager.getStringWithColor(TPA + "Tpa-player-not-found")
                    .replace("%player%", senderName));
            return false;
        }

        TpaRequest request = TpaManager.getRequest(target.getUniqueId());

        if (request == null) {
            target.sendMessage(languageManager.getStringWithColor(TPA + "Tpa-no-pending-request"));
            return false;
        }

        if (!request.sender.getUniqueId().equals(senderPlayer.getUniqueId())) {
            target.sendMessage(languageManager.getStringWithColor(TPA + "Tpa-no-request-from-player")
                    .replace("%player%", senderPlayer.getName()));
            return false;
        }

        OnTpaDeniedEvent event = new OnTpaDeniedEvent(request);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return false;
        }

        TpaManager.cancelRequest(target.getUniqueId());

        senderPlayer.sendMessage(languageManager.getStringWithColor(TPA + "Tpa-denied-by-target")
                .replace("%player%", target.getName()));

        target.sendMessage(languageManager.getStringWithColor(TPA + "Tpa-denied")
                .replace("%player%", senderPlayer.getName()));

        return true;
    }
}