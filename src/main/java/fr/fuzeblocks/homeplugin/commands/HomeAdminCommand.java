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

    private static final String LANG = "Language.";
    private static final String ADMIN = "HomeAdmin.";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sendMsg(sender, LANG + "Only-a-player-can-execute");
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("HomePlugin.admin")) {
            sendMsg(player, LANG + "No-permission");
            return false;
        }

        if (args.length < 1) {
            sendMsg(player, ADMIN + "HomeAdmin-usage-message");
            return false;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null) {
            sendMsg(player, LANG + "Player-is-not-online");
            return false;
        }

        HomeManager homeManager = HomePlugin.getHomeManager();
        List<String> homeNames = homeManager.getHomesName(target);

        sendMsg(player, ADMIN + "Home-list-header", "%player%", target.getName());
        sendMsg(player, ADMIN + "Home-count", "%count%", String.valueOf(homeNames.size()));

        for (String home : homeNames) {
            if (home != null) {
                Location loc = homeManager.getHomeLocation(target, home);
                if (loc != null) {

                    sendMsg(player, ADMIN + "Home-name", "%home%", home);

                    String locationMsg = translate(ADMIN + "Home-location")
                            .replace("%home%", home)
                            .replace("%x%", String.format("%.1f", loc.getX()))
                            .replace("%y%", String.format("%.1f", loc.getY()))
                            .replace("%z%", String.format("%.1f", loc.getZ()))
                            .replace("%world%", loc.getWorld().getName());

                    player.sendMessage("  " + HomePlugin.translateAlternateColorCodes(locationMsg));
                }
            }
        }

        return true;
    }

    private void sendMsg(CommandSender sender, String path) {
        sender.sendMessage(translate(path));
    }

    private void sendMsg(CommandSender sender, String path, String placeholder, String replacement) {
        sender.sendMessage(translate(path).replace(placeholder, replacement));
    }

    private String translate(String path) {
        return HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(path));
    }
}
