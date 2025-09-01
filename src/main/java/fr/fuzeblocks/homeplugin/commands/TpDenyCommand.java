package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.event.OnTpaCreatedEvent;
import fr.fuzeblocks.homeplugin.event.OnTpaDeniedEvent;
import fr.fuzeblocks.homeplugin.tpa.TpaManager;
import fr.fuzeblocks.homeplugin.tpa.TpaRequest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpDenyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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

        Player target = (Player) sender;

        if (args.length != 1) {
            target.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                    "TpaCommand.Tpa-deny-usage",
                    "&cUtilisation : /tpdeny <joueur>"
            ));
            return true;
        }

        Player senderPlayer = Bukkit.getPlayer(args[0]);
        if (senderPlayer == null || !senderPlayer.isOnline()) {
            target.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                    "TpaCommand.Tpa-player-not-found",
                    "&cLe joueur %player% n'est pas en ligne ou n'existe pas."
            ).replace("%player%", args[0]));
            return true;
        }


        TpaRequest request = TpaManager.getRequest(target.getUniqueId());
        if (request == null || !request.sender.getUniqueId().equals(senderPlayer.getUniqueId())) {
            target.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                    "TpaCommand.Tpa-no-request-from-player",
                    "&cVous n'avez pas de demande de téléportation de %player%."
            ).replace("%player%", senderPlayer.getName()));
            return true;
        }

        OnTpaDeniedEvent onTpaCreatedEvent = new OnTpaDeniedEvent(request);
        Bukkit.getPluginManager().callEvent(onTpaCreatedEvent);
        if (!onTpaCreatedEvent.isCancelled()) {
            TpaManager.cancelRequest(senderPlayer.getUniqueId());
        } else {
            return true;
        }

        senderPlayer.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                "TpaCommand.Tpa-denied-by-target",
                "&c%player% a refusé votre demande de téléportation."
        ).replace("%player%", target.getName()));

        target.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                "TpaCommand.Tpa-denied",
                "&cVous avez refusé la demande de téléportation de %player%."
        ).replace("%player%", senderPlayer.getName()));


        return true;
    }
}
