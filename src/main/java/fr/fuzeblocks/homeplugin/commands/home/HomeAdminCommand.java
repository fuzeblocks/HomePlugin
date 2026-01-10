package fr.fuzeblocks.homeplugin.commands.home;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.home.offline.HomeOfflineManager;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * The type Home admin command.
 */
public class HomeAdminCommand implements CommandExecutor {

    private static final String LANG = "Language.";
    private static final String ADMIN = "HomeAdmin.";
    private static final LanguageManager lang = HomePlugin.getLanguageManager();



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(translate(LANG + "Only-a-player-can-execute"));
            return false;
        }

        Player admin = (Player) sender;

        if (!admin.hasPermission("homeplugin.admin")) {
            sendError(admin, translate(LANG + "No-permission"));
            return false;
        }

        if (args.length < 1) {
            sendUsage(admin);
            return false;
        }

        HomeManager homeManager = HomePlugin.getHomeManager();
        String sub = args[0].toLowerCase();

        if (sub.equals("list")) {
            if (args.length < 2) {
                sendUsage(admin);
                return false;
            }
            return listHomes(admin, args[1], homeManager);
        }

        if (sub.equals("deletehome")) {
            if (args.length < 3) {
                sendUsage(admin);
                return false;
            }
            return deleteHome(admin, args[1], args[2], homeManager);
        }

        if (sub.equals("tphome")) {
            if (args.length < 3) {
                sendUsage(admin);
                return false;
            }
            return tpHome(admin, args[1], args[2], homeManager);
        }

        if (sub.equals("addhome")) {
            if (args.length < 3) {
                sendUsage(admin);
                return false;
            }
            return addHome(admin, args[1], args[2], homeManager);
        }

        sendUsage(admin);
        return false;
    }



    private static class Target {
        private final Player online;
        private final UUID uuid;
        private final String name;

        private Target(Player online, UUID uuid, String name) {
            this.online = online;
            this.uuid = uuid;
            this.name = name;
        }
    }

    private Target resolveTarget(String name) {

        Player online = Bukkit.getPlayerExact(name);
        if (online != null) {
            return new Target(online, online.getUniqueId(), online.getName());
        }

        OfflinePlayer offline = Bukkit.getOfflinePlayer(name);
        if (!offline.hasPlayedBefore()) {
            return null;
        }

        return new Target(null, offline.getUniqueId(), offline.getName());
    }



    private boolean listHomes(Player admin, String targetName, HomeManager hm) {

        Target t = resolveTarget(targetName);
        if (t == null) {
            sendError(admin, translate(LANG + "Player-never-joined"));
            return false;
        }

        List<String> homes;
        if (t.online != null) {
            homes = hm.getHomesName(t.online);
        } else {
            homes = HomeOfflineManager.getInstance().getHomesName(t.uuid);
        }

        if (homes == null || homes.isEmpty()) {
            sendError(admin, lang.getStringWithColor(ADMIN + "ListHome-has-no-homes")
                    .replace("%player%", t.name));
            return false;
        }

        admin.sendMessage(ChatColor.DARK_GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

        for (String home : homes) {

            Location loc;
            if (t.online != null) {
                loc = hm.getHomeLocation(t.online, home);
            } else {
                loc = HomeOfflineManager.getInstance().getHomeLocation(t.uuid, home);
            }

            if (loc != null) {
                sendHomeEntry(admin, t.name, home, loc);
            }
        }

        admin.sendMessage(ChatColor.DARK_GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        return true;
    }

    private boolean deleteHome(Player admin, String targetName, String homeName, HomeManager hm) {

        Target t = resolveTarget(targetName);
        if (t == null) {
            sendError(admin, translate(LANG + "Player-never-joined"));
            return false;
        }

        boolean success;
        if (t.online != null) {
            success = hm.deleteHome(t.online, homeName);
        } else {
            success = HomeOfflineManager.getInstance().deleteHome(t.uuid, homeName);
        }

        if (!success) {
            sendError(admin, lang.getStringWithColor(ADMIN + "Home-not-found-admin")
                    .replace("%home%", homeName)
                    .replace("%player%", t.name));
            return false;
        }

        HomePlugin.getCacheManager().removeHome(t.uuid, homeName);
        return true;
    }

    private boolean tpHome(Player admin, String targetName, String homeName, HomeManager hm) {

        Target t = resolveTarget(targetName);
        if (t == null) {
            sendError(admin, translate(LANG + "Player-never-joined"));
            return false;
        }

        Location loc;
        if (t.online != null) {
            loc = hm.getHomeLocation(t.online, homeName);
        } else {
            loc = HomeOfflineManager.getInstance().getHomeLocation(t.uuid, homeName);
        }

        if (loc == null) {
            sendError(admin, lang.getStringWithColor(ADMIN + "Home-not-found-admin")
                    .replace("%home%", homeName)
                    .replace("%player%", t.name));
            return false;
        }

        admin.teleport(loc);
        return true;
    }

    private boolean addHome(Player admin, String targetName, String homeName, HomeManager hm) {

        Target t = resolveTarget(targetName);
        if (t == null) {
            sendError(admin, translate(LANG + "Player-never-joined"));
            return false;
        }

        boolean success;
        if (t.online != null) {
            success = hm.addHome(t.online, homeName);
        } else {
            success = HomeOfflineManager.getInstance()
                    .setHome(t.uuid, homeName, admin.getLocation());
        }

        if (!success) {
            sendError(admin, lang.getStringWithColor(ADMIN + "Home-Already-Exists-Admin")
                    .replace("%home%", homeName)
                    .replace("%player%", t.name));
            return false;
        }

        return true;
    }

    private void sendHomeEntry(Player admin, String playerName, String home, Location loc) {

        TextComponent line = new TextComponent("• " + home);
        line.setColor(ChatColor.AQUA);

        TextComponent tp = new TextComponent(" [TP]");
        tp.setColor(ChatColor.GREEN);
        tp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                "/homeadmin tphome " + playerName + " " + home));

        TextComponent del = new TextComponent(" [DEL]");
        del.setColor(ChatColor.RED);
        del.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                "/homeadmin deletehome " + playerName + " " + home));

        line.addExtra(tp);
        line.addExtra(del);
        admin.spigot().sendMessage(line);
    }



    private void sendError(Player p, String msg) {
        p.sendMessage(ChatColor.RED + "✗ " + ChatColor.stripColor(msg));
    }

    private void sendUsage(Player p) {
        p.sendMessage(ChatColor.YELLOW + "/homeadmin list <player>");
        p.sendMessage(ChatColor.YELLOW + "/homeadmin tphome <player> <home>");
        p.sendMessage(ChatColor.YELLOW + "/homeadmin addhome <player> <home>");
        p.sendMessage(ChatColor.YELLOW + "/homeadmin deletehome <player> <home>");
    }

    private String translate(String path) {
        return HomePlugin.getLanguageManager().getStringWithColor(path);
    }
}
