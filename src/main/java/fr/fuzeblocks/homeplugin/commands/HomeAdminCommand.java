package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.sql.HomeSQLManager;
import fr.fuzeblocks.homeplugin.home.yml.HomeYMLManager;
import fr.fuzeblocks.homeplugin.sync.type.SyncMethod;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HomeAdminCommand implements CommandExecutor {
    private final String homeAdminKey = "Config.HomeAdmin.";
    private final String key = "Config.Language.";
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (player.hasPermission("HomePlugin.admin")) {
                if (args.length >= 1) {
                    Player target = Bukkit.getPlayerExact(args[0]);
                    if (target != null) {
                        if (HomePlugin.getRegistrationType().equals(SyncMethod.MYSQL)) {
                            HomeSQLManager homeSQLManager = HomePlugin.getHomeSQLManager();
                            player.sendMessage("§eLe joueur : " + target.getName() + "§e a pour home/s :");
                            List<String> homeName = homeSQLManager.getHomesName(player);
                            player.sendMessage("§6" + homeName.size() + "§6 home/s");
                            for (String home : homeName) {
                                if (home != null) {
                                    Location homeLocation = homeSQLManager.getHomeLocation(player, home);
                                    player.sendMessage("§4Nom du home en : " + homeName);
                                    player.sendMessage("§aLocalisation de " + homeName + " : X : " + homeLocation.getX() + " Y : " + homeLocation.getY() + " Z : " + homeLocation.getZ() + " Monde : " + homeLocation.getWorld().getName());
                                }
                            }
                            return true;
                        } else {
                            HomeYMLManager homeYMLManager = HomePlugin.getHomeYMLManager();
                            player.sendMessage("§eLe joueur : " + target.getName() + "§e a pour home/s :");
                            List<String> homeName = homeYMLManager.getHomesName(player);
                            player.sendMessage("§6" + homeName.size() + "§6 home/s");
                            for (String home : homeName) {
                                if (home != null) {
                                    Location homeLocation = homeYMLManager.getHomeLocation(player, home);
                                    player.sendMessage("§4Nom du home en : " + homeName);
                                    player.sendMessage("§aLocalisation de " + homeName + " : X : " + homeLocation.getX() + " Y : " + homeLocation.getY() + " Z : " + homeLocation.getZ() + " Monde : " + homeLocation.getWorld().getName());
                                }
                            }
                            return true;
                        }
                    } else {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Player-is-not-online")));
                    }
                } else {
                    player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(homeAdminKey + "HomeAdmin-usage-message")));
                }
            } else {
                player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "No-permission")));
            }
        } else {
            sender.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Only-a-player-can-execute")));
        }
        return false;
    }
}