package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.home.yml.HomeManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CacheCommand implements CommandExecutor {
    private final String key = "Config.Language.";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (player.hasPermission("HomePlugin.cache")) {
                CacheManager cacheManager = HomePlugin.getCacheManager();
                HomeManager homeManager = HomePlugin.getHomeManager();
                fr.fuzeblocks.homeplugin.home.sql.SQLHomeManager homeSQLManager = HomePlugin.getHomeSQLManager();
                if (args.length >= 1) {
                    switch (args[0].toLowerCase()) {
                        case "clearall":
                            cacheManager.clear();
                            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Cache-cleared")));
                            return true;
                        case "view":
                            if (args.length >= 2) {
                                Player target = Bukkit.getPlayer(args[1]);
                                if (cacheManager.getHomesInCache(player) != null) {
                                    player.sendMessage("§eLe joueur : " + target.getName() + "§e a en cache :");
                                    HashMap<String, Location> home = cacheManager.getHomesInCache(player);
                                    player.sendMessage("§6" + home.size() + "§6 home/s");
                                    for (String homeName : homeManager.getHomesName(player)) {
                                        if (home.get(homeName) != null) {
                                            Location homeLocation = home.get(homeName);
                                            player.sendMessage("§4Nom du home en cache : " + homeName);
                                            player.sendMessage("§aLocalisation de " + homeName + " : X : " + homeLocation.getX() + " Y : " + homeLocation.getY() + " Z : " + homeLocation.getZ() + " Monde : " + homeLocation.getWorld().getName());
                                        }
                                    }
                                    return true;
                                } else {
                                    player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Have-no-cache")));
                                }
                            } else {
                                player.sendMessage("§cUtilisation de la commande : /cache <view> joueur");
                            }
                            break;
                        default:
                            Player target = Bukkit.getPlayerExact(args[0]);
                            if (target != null) {
                                cacheManager.clearPlayer(player);
                                player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Cache-player-cleared").replace("%player%", player.getName())));
                                return true;
                            } else {
                                player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Player-is-not-online")));
                            }
                    }
                } else {
                    player.sendMessage("§cUtilisation de la commande : /cache <clearall> <view>");
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
