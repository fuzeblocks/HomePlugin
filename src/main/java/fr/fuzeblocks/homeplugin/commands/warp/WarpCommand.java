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

import java.util.UUID;

/**
 * The type Warp command.
 */
public class WarpCommand implements CommandExecutor {
    private static final String WARP = "Warp.";
    private static final String LANG = "Language.";

    // message keys todo in the languages files
    private static final String KEY_WARP_DOES_NOT_EXIST = WARP + "Warp-does-not-exist";
    private static final String KEY_WARP_SET_SUCCESS = WARP + "Warp-set-success";
    private static final String KEY_WARP_DELETE_SUCCESS = WARP + "Warp-delete-success";
    private static final String KEY_WARP_USAGE = WARP + "Usage-warp-command";
    private static final String KEY_ONLY_PLAYER = LANG + "Only-a-player-can-execute";
    private static final String KEY_NO_PERMISSION = LANG + "No-permission";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final LanguageManager languageManager = HomePlugin.getLanguageManager();

        if (!(sender instanceof Player)) {
            sender.sendMessage(languageManager.getStringWithColor(KEY_ONLY_PLAYER));
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("homeplugin.command.warp.use")) {
            player.sendMessage(languageManager.getStringWithColor(KEY_NO_PERMISSION));
            return false;
        }

        final WarpManager warpManager = HomePlugin.getWarpManager();

        if (args.length == 1) {
            String arg = args[0];

            if (arg.equalsIgnoreCase("list")) {
                WarpGUIManager.openWarpListGUI(player);
                return true;
            }

            if (arg.equalsIgnoreCase("modify")) {
                WarpGUIManager.openEditWarpGUI(player);
                return true;
            }

            // treat as warp name -> teleport attempt
            return handleTeleport(player, arg, languageManager, warpManager);
        }

        if (args.length > 0) {
            String subCommand = args[0];

            // only players with modify permission can use set/delete subcommands
            if (!player.hasPermission("homeplugin.command.warp.modify")) {
                player.sendMessage(languageManager.getStringWithColor(KEY_NO_PERMISSION));
                return false;
            }

            if (subCommand.equalsIgnoreCase("set") && args.length == 2) {
                return handleSet(player, args[1], languageManager, warpManager);
            } else if (subCommand.equalsIgnoreCase("delete") && args.length == 2) {
                return handleDelete(player, args[1], languageManager, warpManager);
            } else {
                player.sendMessage(languageManager.getStringWithColor(KEY_WARP_USAGE, "&cUtilisation : /warp <nom-du-warp>"));
                return false;
            }
        }

        // no args -> show usage
        sender.sendMessage(languageManager.getStringWithColor(KEY_WARP_USAGE, "&cUtilisation : /warp <nom-du-warp>"));
        return false;
    }

    // Handle teleport attempt in a single place to ensure correct order of checks and payment
    private boolean handleTeleport(Player player, String warpName, LanguageManager languageManager, WarpManager warpManager) {
        if (!warpManager.warpExists(warpName)) {
            player.sendMessage(languageManager.getStringWithColor(KEY_WARP_DOES_NOT_EXIST, "&cLe warp &e({warp}) spécifié n'existe pas !").replace("{warp}", warpName));
            return false;
        }

        WarpData warpData = warpManager.getWarp(warpName);

       UUID uuid = player.getUniqueId();


        if (!warpData.canAccess(uuid)) {
            player.sendMessage(languageManager.getStringWithColor(
                WARP + "Warp-no-access",
                "&cVous n'avez pas la permission d'accéder à ce warp."
            ).replace("{warp}", warpName));
            return false;
        }

        if (!player.hasPermission(warpData.getPermission())) {
            player.sendMessage(languageManager.getStringWithColor(
                WARP + "Warp-no-permission",
                "&cVous n'avez pas la permission d'accéder à ce warp."
            ).replace("{warp}", warpName));
            return false;
        }

        // expiration check: teleport only if NOT expired
        if (warpManager.isExpired(warpData)) {
            player.sendMessage(languageManager.getStringWithColor(WARP + "Warp-expired", "&cLe warp &e({warp}) &ca expiré.").replace("{warp}", warpName));
            return false;
        }

        // payment: only after all validations passed
        if (!EconomyManager.pay(player, EconomyManager.getWarpUsePrice())) {
            player.sendMessage(languageManager.getStringWithColor(WARP + "Warp-not-enough-money", "&cVous n'avez pas assez d'argent pour utiliser ce warp.").replace("{warp}", warpName));
            return false;
        }

        // all good -> teleport
        TeleportationManager.teleportPlayerToWarp(player, warpName);
        return true;
    }

    private boolean handleSet(Player player, String warpName, LanguageManager languageManager, WarpManager warpManager) {
        warpManager.addWarp(new WarpData(warpName, player.getName(), player.getUniqueId(), player.getLocation()));
        player.sendMessage(languageManager.getStringWithColor(KEY_WARP_SET_SUCCESS, "&aLe warp &e({warp}) &aa été défini.").replace("{warp}", warpName));
        return true;
    }

    private boolean handleDelete(Player player, String warpName, LanguageManager languageManager, WarpManager warpManager) {
        if (!warpManager.warpExists(warpName)) {
            player.sendMessage(languageManager.getStringWithColor(KEY_WARP_DOES_NOT_EXIST, "&cLe warp &e({warp}) spécifié n'existe pas !").replace("{warp}", warpName));
            return false;
        }
        warpManager.deleteWarp(warpName);
        player.sendMessage(languageManager.getStringWithColor(KEY_WARP_DELETE_SUCCESS, "&aLe warp &e({warp}) &aa été supprimé.").replace("{warp}", warpName));
        return true;
    }
}
