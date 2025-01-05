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
import java.util.Map;

public class CacheCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String key = "Language.";
          if (!(sender instanceof Player)) {
            sender.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Only-a-player-can-execute")));
            return false;
          }
            Player player = ((Player) sender).getPlayer();
            if (!player.hasPermission("HomePlugin.cache")) {
                player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "No-permission")));
                return false;
            }
                CacheManager cacheManager = HomePlugin.getCacheManager();
                HomeManager homeManager = HomePlugin.getHomeManager();
                String cacheKey = "Cache.";
                if (args.length < 1) {
                    player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(cacheKey + "Cache-usage-command")));
                    return false;
                 }
                    switch (args[0].toLowerCase()) {
                        case "clearall":
                            cacheManager.clear();
                            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Cache-cleared")));
                           break;
                        case "view":
                            if (args.length < 2) {
                                player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(cacheKey + "Cache-view-usage-command")));
                                return false;
                            }
                                Player target = Bukkit.getPlayer(args[1]);
                                if (cacheManager.getHomesInCache(player) == null) {
                                    player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Have-no-cache")));
                                    return false;
                                }
                                    player.sendMessage("§eLe joueur : " + target.getName() + "§e a en cache :");
                                    Map<String, Location> home = cacheManager.getHomesInCache(player);
                                    player.sendMessage("§6" + home.size() + "§6 home/s");
                                    for (String homeName : homeManager.getHomesName(player)) {
                                        sendHomeMessage((HashMap<String, Location>) home,homeName,player);
                                    }
                            break;
                        default:
                            Player playertarget = Bukkit.getPlayerExact(args[0]);
                            if (playertarget == null) {
                                player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Player-is-not-online")));
                                return false;
                            }
                            cacheManager.clearPlayer(player);
                            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Cache-player-cleared").replace("%player%", player.getName())));
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