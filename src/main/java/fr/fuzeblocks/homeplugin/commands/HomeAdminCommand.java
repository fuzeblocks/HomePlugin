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
        String key = "Language.";
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (player.hasPermission("HomePlugin.admin")) {
                if (args.length >= 1) {
                    Player target = Bukkit.getPlayerExact(args[0]);
                    if (target != null) {
                        HomeManager homeManager = HomePlugin.getHomeManager();
                            player.sendMessage("§eLe joueur : " + target.getName() + "§e a pour home/s :");
                            List<String> homeName = homeManager.getHomesName(player);
                            player.sendMessage("§6" + homeName.size() + "§6 home/s");
                            for (String home : homeName) {
                                if (home != null) {
                                    Location homeLocation = homeManager.getHomeLocation(player, home);
                                    player.sendMessage("§4Nom du home : " + homeName);
                                    player.sendMessage("§aLocalisation de " + homeName + " : X : " + homeLocation.getX() + " Y : " + homeLocation.getY() + " Z : " + homeLocation.getZ() + " Monde : " + homeLocation.getWorld().getName());
                                }
                            }
                            return true;
                    } else {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Player-is-not-online")));
                    }
                } else {
                    String homeAdminKey = "HomeAdmin.";
                    player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(homeAdminKey + "HomeAdmin-usage-message")));
                }
            } else {
                player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "No-permission")));
            }
        } else {
            sender.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Only-a-player-can-execute")));
        }
        return false;
    }
}