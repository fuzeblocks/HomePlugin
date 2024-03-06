package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.home.yml.HomeManager;
import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (args.length == 1) {
                String home_name = args[0];
                HomeManager homeManager = HomePlugin.getHomeManager();
                fr.fuzeblocks.homeplugin.home.sql.HomeManager homeSQLManager = HomePlugin.getHomeSQLManager();
                if (HomePlugin.getRegistrationType() == 1) {
                    if (homeSQLManager.isStatus(player)) {
                        return false;
                    }
                    if (homeSQLManager.addHome(player,home_name)) {
                        player.sendMessage("§aHome ajouté !");
                    } else {
                        player.sendMessage("§cUne erreur est survenu !");
                    }
                    return true;
                }
                if (homeManager.isStatus(player)) {
                    return false;
                }

                   if (homeManager.addHome(player,home_name)) {
                       player.sendMessage("§aHome ajouté !");
                   } else {
                       player.sendMessage("§cUne erreur est survenu !");
                   }
                    return true;
            } else {
                player.sendMessage("§cUtilisation de la commande : /sethome <nom-du-home>");
            }
        }  else {
            sender.sendMessage("§cSeul un joueur peut executer cette commande !");
        }
        return false;
    }

}

