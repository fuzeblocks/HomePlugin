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

/**
 * The type Home admin command.
 */
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

        if (!player.hasPermission("homeplugin.admin")) {
            sendMsg(player, LANG + "No-permission");
            return false;
        }

        if (args.length < 1) {
            sendUsage(player);
            return false;
        }

        String subCommand = args[0].toLowerCase();
        HomeManager homeManager = HomePlugin.getHomeManager();

        switch (subCommand) {

            case "list":
                if (args.length < 2) {
                    sendMsg(player, "HomeAdmin.List-usage");
                    return false;
                }
                return listHomes(player, args[1], homeManager);

            case "deletehome":
                if (args.length < 3) {
                    sendMsg(player, "HomeAdmin.DeleteHome-usage");
                    return false;
                }
                return deleteHome(player, args[1], args[2], homeManager);

            case "tphome":
                if (args.length < 3) {
                    sendMsg(player, "HomeAdmin.TpHome-usage");
                    return false;
                }
                return teleportToHome(player, args[1], args[2], homeManager);

            case "addhome":
                if (args.length < 3) {
                    sendMsg(player, "HomeAdmin.AddHome-usage");
                    return false;
                }
                return addHome(player, args[1], args[2], homeManager);

            default:
                sendUsage(player);
                return false;
        }
    }

    private boolean listHomes(Player player, String targetName, HomeManager homeManager) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sendMsg(player, LANG + "Player-is-not-online");
            return false;
        }

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

    private boolean deleteHome(Player player, String targetName, String homeName, HomeManager homeManager) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sendMsg(player, LANG + "Player-is-not-online");
            return false;
        }
        boolean deleted = homeManager.deleteHome(target, homeName);
        if (deleted) {
            sendMsg(player, ADMIN + "Home-deleted", "%home%", homeName);
            return true;
        } else {
            sendMsg(player, ADMIN + "Home-not-found", "%home%", homeName);
            return false;
        }
    }

    private boolean teleportToHome(Player player, String targetName, String homeName, HomeManager homeManager) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sendMsg(player, LANG + "Player-is-not-online");
            return false;
        }
        Location loc = homeManager.getHomeLocation(target, homeName);
        if (loc == null) {
            sendMsg(player, ADMIN + "Home-not-found", "%home%", homeName, "%player%", target.getName());
            return false;
        }
        player.teleport(loc);
        sendMsg(player, ADMIN + "Teleported-to-home", "%home%", homeName, "%player%", target.getName());
        return true;
    }

    private boolean addHome(Player admin, String targetName, String homeName, HomeManager homeManager) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sendMsg(admin, LANG + "Player-is-not-online");
            return false;
        }

        boolean added = homeManager.addHome(admin, homeName);

        if (added) {
            sendMsg(admin, ADMIN + "Home-added", "%home%", homeName, "%player%", target.getName());
            return true;
        } else {
            sendMsg(admin, ADMIN + "Home-already-exists", "%home%", homeName, "%player%", target.getName());
            return false;
        }
    }

    private void sendUsage(Player player) {
        sendMsg(player, ADMIN + "HomeAdmin-usage-message");
    }

    private void sendMsg(CommandSender sender, String path) {
        sender.sendMessage(translate(path));
    }

    private void sendMsg(CommandSender sender, String path, String home, String homeReplacement, String player, String playerReplacement) {
        sender.sendMessage(translate(path).replace(home, homeReplacement)
                .replace(player, playerReplacement));
    }

    private void sendMsg(CommandSender sender, String path, String placeholder, String replacement) {
        sender.sendMessage(translate(path).replace(placeholder, replacement));
    }

    private String translate(String path) {
        return HomePlugin.getLanguageManager().getStringWithColor(path);
    }
}
