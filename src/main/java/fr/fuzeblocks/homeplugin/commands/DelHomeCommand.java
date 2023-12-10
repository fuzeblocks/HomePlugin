package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelHomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (args.length == 1) {
                String home_name = args[0];
                if (HomePlugin.getHomeManager().delHome(player,home_name)) {
                    player.sendMessage("§aLe home a été supprimé avec succés !");
                    return true;
                } else {
                    player.sendMessage("§cLe home séléctionné n'existe pas !");
                }
            } else {
                player.sendMessage("§cUtilisation de la commande : /delhome <nom-du-home>");
            }
        } else {
            sender.sendMessage("§cSeul un joueur peut executer cette commande !");
        }
        return false;
    }
}
