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
    private final String cacheKey = "Config.Cache.";

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
                                        sendHomeMessage(home,homeName,player);
                                    }
                                    for (String homeName : homeSQLManager.getHomesName(player)) {
                                        sendHomeMessage(home,homeName,player);
                                    }
                                    return true;
                                } else {
                                    player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Have-no-cache")));
                                }
                            } else {
                                player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(cacheKey + "Cache-view-usage-command")));
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
                    player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(cacheKey + "Cache-usage-command")));
                }
            } else {
                player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "No-permission")));
            }
        } else {
            sender.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Only-a-player-can-execute")));
        }
        return false;
    }
    private void sendHomeMessage(HashMap<String, Location> home,String homeName,Player player) {
        if (home.get(homeName) != null) {
            Location homeLocation = home.get(homeName);
            player.sendMessage("§4Nom du home en cache : " + homeName);
            player.sendMessage("§aLocalisation de " + homeName + " : X : " + homeLocation.getX() + " Y : " + homeLocation.getY() + " Z : " + homeLocation.getZ() + " Monde : " + homeLocation.getWorld().getName());
        }
    }

}
