package fr.fuzeblocks.homeplugin.commands.warp;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.economy.EconomyManager;
import fr.fuzeblocks.homeplugin.gui.warp.WarpGUIManager;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import fr.fuzeblocks.homeplugin.task.TaskManager;
import fr.fuzeblocks.homeplugin.task.TeleportationManager;
import fr.fuzeblocks.homeplugin.warps.WarpData;
import fr.fuzeblocks.homeplugin.warps.WarpManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * The type Warp command.
 */
public class WarpCommand implements CommandExecutor {
    private static final String HOME = "Warp.";
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
            if (!warpManager.warpExists(warpName)) {
                player.sendMessage(languageManager.getStringWithColor(HOME + "Warp-does-not-exist").replace("{warp}", warpName)); //Missing key. In dev
                return false;
            }

            WarpData warpData = warpManager.getWarp(warpName);

            if (warpData.canAccess(player.getUniqueId()) && EconomyManager.pay(player, EconomyManager.getWarpUsePrice())) {
                TeleportationManager.teleportPlayerToWarp(player, warpName);
            } else {
                player.sendMessage(languageManager.getStringWithColor(HOME + "Warp-teleport-failed").replace("{warp}", warpName)); //Missing key. In dev
            }
            return true;
        } else if (args.length > 1) {
            if (!player.hasPermission("homeplugin.command.warp.modify")) {
                player.sendMessage(languageManager.getStringWithColor(LANG + "No-permission"));
                return false;
            }
            String subCommand = args[0].toLowerCase();
            if (subCommand.equals("set") && args.length == 2 && args[1] != null) {
                String warpName = args[1];
                warpManager.addWarp(new WarpData(warpName,player.getName(),player.getUniqueId(),player.getLocation()));
                player.sendMessage(languageManager.getStringWithColor(HOME + "Warp-set-success").replace("{warp}", warpName)); //Missing key. In dev
                return true;
            } else if (subCommand.equals("delete") && args.length == 2 && args[1] != null) {
                String warpName = args[1];
                if (!warpManager.warpExists(warpName)) {
                    player.sendMessage(languageManager.getStringWithColor(HOME + "Warp-does-not-exist").replace("{warp}", warpName)); //Missing key. In dev
                    return false;
                }
                warpManager.deleteWarp(warpName);
                player.sendMessage(languageManager.getStringWithColor(HOME + "Warp-delete-success").replace("{warp}", warpName)); //Missing key. In dev
                return true;
            } else if (subCommand.equals("list")) {
                WarpGUIManager.openWarpListGUI(player);
            } else if (subCommand.equals("modify")) {
                //Todo open friendly GUI to modify warp settings

            } else {
                player.sendMessage(languageManager.getStringWithColor(HOME + "Usage-warp-command")); //Missing key. In dev
                return false;
            }
        } else {
            player.sendMessage(languageManager.getStringWithColor(HOME + "Usage-warp-command")); //Missing key. In dev
            return false;
        }
        return false;
    }
}
