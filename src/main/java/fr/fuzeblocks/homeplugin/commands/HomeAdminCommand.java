package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HomeAdminCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (player.hasPermission("HomePlugin.admin")) {
                if (args.length >= 1) {
                    Player target = Bukkit.getPlayerExact(args[0]);
                    if (target != null) {
                        HomeManager homeManager = HomePlugin.getHomeManager();
                            player.sendMessage("§eLe joueur : " + target.getName() + "§e a en cache :");
                        List<String> homeName = homeManager.getHomesNames(player);
                            player.sendMessage("§6" + homeName.size() + "§6 home/s");
                            for (String home : homeName) {
                                if (home != null) {
                                    Location homeLocation = homeManager.getHomeLocation(player,home);
                                    player.sendMessage("§4Nom du home en cache : " + homeName);
                                    player.sendMessage("§aLocalisation de " + homeName + " : X : " + homeLocation.getX() + " Y : " + homeLocation.getY() + " Z : " + homeLocation.getZ() + " Monde : " + homeLocation.getWorld().getName());
                                }
                            }
                            return true;
                    }  else {
                        player.sendMessage("§cLe joueur choisi n'existe pas ou n'est pas en ligne !");
                    }
                } else {
                    player.sendMessage("§cUtilisation de la commande : /homeadmin joueur");
                }
            } else {
                player.sendMessage("§cVous n'avez pas la permission pour utiliser cette commande !");
            }
        } else {
            sender.sendMessage("§cSeul un joueur peut éxecuter cette commande !");
        }
        return false;
    }
}
