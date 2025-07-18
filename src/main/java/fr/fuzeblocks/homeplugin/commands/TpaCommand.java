package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.tpa.TpaManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                    "Only-a-player-can-execute",
                    "&cSeul un joueur peut exécuter cette commande !"
            ));
            return true;
        }

        if (!sender.hasPermission("homeplugin.command.tpa")) {
            sender.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                    "No-permission",
                    "&cVous n'avez pas la permission d'exécuter cette commande."
            ));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

            if (target != null && target.isOnline() && !target.equals(player)) {
                TpaManager.sendTpaRequest(player, target);
            } else {
                player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                        "TpaCommand.Tpa-player-not-found"
                ).replace("%player%", args[0]));
            }

            return true;
        } else {
            player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                    "TpaCommand.Tpa-usage-message",
                    "&cUtilisation : /tpa <joueur>"
            ));
        }

        return true;
    }
}
