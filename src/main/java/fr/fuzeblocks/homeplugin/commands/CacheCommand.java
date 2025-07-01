package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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

        if (!(sender instanceof Player player)) {
            sendMsg(sender, LANG + "Only-a-player-can-execute");
            return false;
        }

        if (!player.hasPermission("HomePlugin.cache")) {
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
            case "clearall" -> {
                cacheManager.clear();
                sendMsg(player, LANG + "Cache-cleared");
            }

            case "view" -> {
                if (args.length < 2) {
                    sendMsg(player, CACHE + "Cache-view-usage-command");
                    return false;
                }

                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sendMsg(player, LANG + "Player-is-not-online");
                    return false;
                }

                Map<String, Location> homes = cacheManager.getHomesInCache(target);
                if (homes == null || homes.isEmpty()) {
                    sendMsg(player, LANG + "Have-no-cache");
                    return false;
                }

                sendMsg(player, CACHE + "Cache-view-header", "%player%", target.getName());
                sendMsg(player, CACHE + "Cache-home-count", "%count%", String.valueOf(homes.size()));

                for (String homeName : homeManager.getHomesName(target.getUniqueId())) {
                    sendHomeMessage(homes, homeName, player);
                }
            }

            default -> {
                Player targetToClear = Bukkit.getPlayerExact(args[0]);
                if (targetToClear == null) {
                    sendMsg(player, LANG + "Player-is-not-online");
                    return false;
                }

                cacheManager.clearPlayer(targetToClear);
                sendMsg(player, LANG + "Cache-player-cleared", "%player%", targetToClear.getName());
            }
        }

        return true;
    }

    private void sendHomeMessage(Map<String, Location> homes, String homeName, Player player) {
        Location homeLocation = homes.get(homeName);
        if (homeLocation != null) {

            player.sendMessage("§4Nom du home en cache : " + homeName);
            player.sendMessage(String.format(
                    "§aLocalisation de %s : X: %.1f Y: %.1f Z: %.1f Monde: %s",
                    homeName,
                    homeLocation.getX(),
                    homeLocation.getY(),
                    homeLocation.getZ(),
                    homeLocation.getWorld().getName()
            ));


            TextComponent message = Component.text("► ")
                    .color(NamedTextColor.GRAY)
                    .append(Component.text("Se téléporter à ").color(NamedTextColor.GREEN))
                    .append(Component.text(homeName).color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD))
                    .clickEvent(ClickEvent.callback(home -> {
                        if (homeLocation != null) {
                            player.teleport(homeLocation);
                            home.sendMessage(Component.text("§aVous avez été téléporté au home : " + homeName));
                        } else {
                            home.sendMessage(Component.text("§cErreur : Le home n'existe pas dans le cache."));
                        }
                    }))
                    .hoverEvent(HoverEvent.showText(Component.text("Cliquez pour vous téléporter")))
                    .append(Component.text(" ⬅").color(NamedTextColor.GRAY));

            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("§7Cliquez ci-dessous :");
            player.sendMessage("");

            HomePlugin.adventure().player(player).sendMessage(message);

        }
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
