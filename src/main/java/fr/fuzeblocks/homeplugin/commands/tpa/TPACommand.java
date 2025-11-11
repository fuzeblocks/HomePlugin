package fr.fuzeblocks.homeplugin.commands.tpa;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.economy.EconomyManager;
import fr.fuzeblocks.homeplugin.event.OnTpaCreatedEvent;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import fr.fuzeblocks.homeplugin.tpa.TpaManager;
import fr.fuzeblocks.homeplugin.tpa.TpaRequest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Command to send a teleport request to another player.
 */
public class TPACommand implements CommandExecutor {

    private static final String TPA = "TpaCommand.";
    private static final String LANG = "Language.";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        final LanguageManager languageManager = HomePlugin.getLanguageManager();

        if (!(sender instanceof Player)) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Only-a-player-can-execute"));
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("homeplugin.command.tpa")) {
            player.sendMessage(languageManager.getStringWithColor(LANG + "No-permission"));
            return false;
        }

        if (args.length != 1) {
            player.sendMessage(languageManager.getStringWithColor(TPA + "Tpa-usage-message"));
            return false;
        }

        return sendTpaRequest(player, args[0], languageManager);
    }

    /**
     * Sends a TPA request to a target player.
     *
     * @param player The player sending the request
     * @param targetName The name of the target player
     * @param languageManager The language manager instance
     * @return true if successful, false otherwise
     */
    private boolean sendTpaRequest(Player player, String targetName, LanguageManager languageManager) {
        Player target = Bukkit.getPlayer(targetName);

        if (target == null || !target.isOnline()) {
            player.sendMessage(languageManager.getStringWithColor(TPA + "Tpa-player-not-found")
                    .replace("%player%", targetName));
            return false;
        }

        if (target.equals(player)) {
            player.sendMessage(languageManager.getStringWithColor(TPA + "Tpa-cannot-request-yourself"));
            return false;
        }

        if (TpaManager.hasRequest(target.getUniqueId())) {
            TpaRequest existingRequest = TpaManager.getRequest(target.getUniqueId());
            if (existingRequest != null && existingRequest.sender.equals(player)) {
                player.sendMessage(languageManager.getStringWithColor(TPA + "Tpa-already-sent")
                        .replace("%player%", target.getName()));
                return false;
            }
        }

        TpaRequest request = new TpaRequest(player, target, null);
        OnTpaCreatedEvent event = new OnTpaCreatedEvent(request);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return false;
        }

        double price = EconomyManager.getTpaRequestPrice();
        if (price > 0 && !EconomyManager.pay(player, price)) {
            player.sendMessage(languageManager.getStringWithColor(LANG + "Not-Enough-Money"));
            return false;
        }

        boolean success = TpaManager.sendTpaRequest(player, target);

        if (!success) {
            player.sendMessage(languageManager.getStringWithColor(TPA + "Tpa-send-failed"));
            return false;
        }

        return true;
    }
}