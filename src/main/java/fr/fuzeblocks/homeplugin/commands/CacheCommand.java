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

import java.util.Map;

public class CacheCommand implements CommandExecutor {

    private static final String LANG = "Language.";
    private static final String CACHE = "Cache.";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sendMsg(sender, LANG + "Only-a-player-can-execute");
            return false;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("homeplugin.admin")) {
            sendMsg(player, LANG + "No-permission");
            return false;
        }

        CacheManager cacheManager = HomePlugin.getCacheManager();
        HomeManager homeManager = HomePlugin.getHomeManager();

        if (args.length < 1) {
            sendMsg(player, CACHE + "Cache-usage-command");
            return false;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "clearall":
                cacheManager.clearAllHomes();
                sendMsg(player, CACHE + "Cache-cleared");
                break;
            case "view":
                if (args.length < 2) {
                    sendMsg(player, CACHE + "Cache-view-usage-command");
                    return false;
                }

               Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sendMsg(player, LANG + "Player-is-not-online");
                    return false;
                }

                Map<String, Location> homes = cacheManager.getHomes(target.getUniqueId());
                if (homes == null || homes.isEmpty()) {
                    sendMsg(player, CACHE + "Have-no-cache");
                    return false;
                }

                sendMsg(player, CACHE + "Cache-view-header", "%player%", target.getName());
                sendMsg(player, CACHE + "Cache-home-count", "%count%", String.valueOf(homes.size()));

                for (String homeName : homeManager.getHomesName(target)) {
                    sendHomeMessage(homes, homeName, player);
                }
                break;

            default:
                Player targetToClear = Bukkit.getPlayerExact(args[0]);
                if (targetToClear == null) {
                    sendMsg(player, LANG + "Player-is-not-online");
                    return false;
                }

                cacheManager.clearHomes(targetToClear.getUniqueId());
                sendMsg(player, CACHE + "Cache-player-cleared", "%player%", targetToClear.getName());
                break;
        }

        return true;
    }

    private void sendHomeMessage(Map<String, Location> homes, String homeName, Player player) {
        Location homeLocation = homes.get(homeName);
        if (homeLocation != null) {
            String nameLine = translate(CACHE + "Cache-home-name")
                    .replace("%home%", "&b" + homeName);
            player.sendMessage(nameLine);

            String locationLine = translate(CACHE + "Cache-home-location")
                    .replace("%home%", "§b" + homeName)
                .replace("%x%", "§a" + String.format("%.1f", homeLocation.getX()))
                    .replace("%y%", "§a" + String.format("%.1f", homeLocation.getY()))
                    .replace("%z%", "§a" + String.format("%.1f", homeLocation.getZ()))
                    .replace("%world%", "§e" + homeLocation.getWorld().getName());

            player.sendMessage(locationLine);
        }
    }


    private void sendMsg(CommandSender sender, String path) {
        sender.sendMessage(translate(path));
    }

    private void sendMsg(CommandSender sender, String path, String placeholder, String replacement) {
        sender.sendMessage(translate(path).replace(placeholder, replacement));
    }

    private String translate(String path) {
        return HomePlugin.getLanguageManager().getStringWithColor(path);
    }
}
