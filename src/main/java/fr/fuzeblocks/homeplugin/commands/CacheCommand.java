package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CacheCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (player.hasPermission("HomePlugin.cache")) {
                CacheManager cacheManager = HomePlugin.getCacheManager();
                HomeManager homeManager = HomePlugin.getHomeManager();
                if (args[0] != null) {
                    switch (args[0].toLowerCase()) {
                       case "clearall":
                           cacheManager.clear();
                           player.sendMessage("§aLe cache a étés vidé pour tous les joueurs !");
                           return true;
                        case "view":
                            if (args[1] != null) {
                                Player target = Bukkit.getPlayer(args[1]);
                                    if (cacheManager.getHomesInCache(player) != null) {
                                        player.sendMessage("§aLe joueur : " + target.getName() + "§a a en cache :");
                                        HashMap<String, Location> home = cacheManager.getHomesInCache(player);
                                        player.sendMessage(home.size() + "§a home/s");
                                        for (String homeName : homeManager.getHomesNames(player)) {
                                            if (home.get(homeName) != null) {
                                                Location homeLocation = home.get(homeName);
                                                player.sendMessage("§aNom du home en cache : " + homeName);
                                                player.sendMessage("§aLocalisation de " + homeName + " : X : " + homeLocation.getX() + " Y : " + homeLocation.getY() + " Z : " + homeLocation.getZ() + " Monde : " + homeLocation.getWorld().getName());
                                            }
                                        }
                                        return true;
                                    } else {
                                        player.sendMessage("§cLe joueur n'a pas de cache !");
                                    }
                            } else {
                                player.sendMessage("§cUtilisation de la commande : /cache <view> joueur");
                            }
                        default:
                          Player target = Bukkit.getPlayerExact(args[0]);
                          if (target != null) {
                              cacheManager.clearPlayer(player);
                              player.sendMessage("§aLe cache pour le joueur : " + target.getName() + " §aa été vidé !");
                              return true;
                          } else {
                              player.sendMessage("§cLe joueur choisi n'existe pas ou n'est pas en ligne !");
                          }
                    }
                } else {
                    player.sendMessage("§cUtilisation de la commande : /cache <clearall> <joueur>");
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
