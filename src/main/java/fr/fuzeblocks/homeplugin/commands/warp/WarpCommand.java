package fr.fuzeblocks.homeplugin.commands.warp;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.economy.EconomyManager;
import fr.fuzeblocks.homeplugin.gui.warp.WarpGUIManager;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import fr.fuzeblocks.homeplugin.task.TeleportationManager;
import fr.fuzeblocks.homeplugin.warps.WarpData;
import fr.fuzeblocks.homeplugin.warps.WarpManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The type Warp command.
 */
public class WarpCommand implements CommandExecutor {
    private static final String WARP = "Warp.";
    private static final String LANG = "Language.";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final LanguageManager languageManager = HomePlugin.getLanguageManager();

        if (!(sender instanceof Player)) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Only-a-player-can-execute"));
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("homeplugin.command.warp.use")) {
            player.sendMessage(languageManager.getStringWithColor(LANG + "No-permission"));
            return false;
        }

        final WarpManager warpManager = HomePlugin.getWarpManager();

        if (args.length == 1) {

            String warpName = args[0];


             if (warpName.equals("list")) {
                WarpGUIManager.openWarpListGUI(player);
            } else if (warpName.equals("modify")) {
                WarpGUIManager.openEditWarpGUI(player);
            } else {
                if (!warpManager.warpExists(warpName)) {
                    player.sendMessage(languageManager.getStringWithColor(WARP + "Warp-does-not-exist", "&cLe warp &e({warp}) spécifié n'existe pas !").replace("{warp}", warpName));
                    return false;
                }
                WarpData warpData = warpManager.getWarp(warpName);
                if (warpData.canAccess(player.getUniqueId()) && EconomyManager.pay(player, EconomyManager.getWarpUsePrice()) && warpManager.isExpired(warpData)) {
                    TeleportationManager.teleportPlayerToWarp(player, warpName);
                } else {
                    player.sendMessage(languageManager.getStringWithColor(WARP + "Warp-teleport-failed", "&cLa téléportation vers le warp &e({warp}) a échoué !").replace("{warp}", warpName));
                }
            }
             return true;
        }

        if (args.length > 0) {
            String subCommand = args[0];

            if (!player.hasPermission("homeplugin.command.warp.modify")) {
                player.sendMessage(languageManager.getStringWithColor(LANG + "No-permission"));
                return false;
            }

            if (subCommand.equals("set") && args.length == 2 && args[1] != null) {
                String warpName = args[1];
                warpManager.addWarp(new WarpData(warpName, player.getName(), player.getUniqueId(), player.getLocation()));
                player.sendMessage(languageManager.getStringWithColor(WARP + "Warp-set-success", "&aLe warp &e({warp}) &aa été défini.").replace("{warp}", warpName)); //
                return true;
            } else if (subCommand.equals("delete") && args.length == 2 && args[1] != null) {
                String warpName = args[1];
                if (!warpManager.warpExists(warpName)) {
                    player.sendMessage(languageManager.getStringWithColor(WARP + "Warp-does-not-exist", "&cLe warp &e({warp}) spécifié n'existe pas !").replace("{warp}", warpName));
                    return false;
                }
                warpManager.deleteWarp(warpName);
                player.sendMessage(languageManager.getStringWithColor(WARP + "Warp-delete-success", "&aLe warp &e({warp}) &aa été supprimé.").replace("{warp}", warpName));
                return true;
            } else {
                player.sendMessage(languageManager.getStringWithColor(WARP + "Usage-warp-command", "&cUtilisation : /warp <nom-du-warp>"));
                return false;
            }
        }

        return false;
    }
}
