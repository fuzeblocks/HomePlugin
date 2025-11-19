package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Cache management command for viewing and clearing player home cache.
 * Provides interactive messages with clickable teleport links.
 */
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
            sendUsageMessage(player);
            return false;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "clearall":
                handleClearAll(player, cacheManager);
                break;

            case "view":
                if (args.length < 2) {
                    sendMsg(player, CACHE + "Cache-view-usage-command");
                    return false;
                }
                handleViewCache(player, args[1], cacheManager, homeManager);
                break;

            default:
                handleClearPlayer(player, args[0], cacheManager);
                break;
        }

        return true;
    }

    /**
     * Sends an interactive usage message with clickable commands.
     *
     * @param player The player to send the message to
     */
    private void sendUsageMessage(Player player) {
        player.sendMessage(ChatColor.DARK_GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

        // Title
        String title = translate(CACHE + "Cache-management-title");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "         " + title));
        player.sendMessage("");

        // Get usage icon from language file
        String usageIcon = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', translate(CACHE + "Cache-usage-icon")));

        // /cache view <player> - clickable
        TextComponent viewCmd = new TextComponent("  " + usageIcon + " ");
        viewCmd.setColor(ChatColor.DARK_GRAY);

        TextComponent viewText = new TextComponent("/cache view <player>");
        viewText.setColor(ChatColor.YELLOW);
        viewText.setBold(false);
        viewText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/cache view "));

        String viewHover = translate(CACHE + "Cache-click-suggest");
        viewText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.stripColor(viewHover))
                        .color(ChatColor.GRAY).create()));

        String viewDesc = translate(CACHE + "Cache-view-description");
        TextComponent viewDescComp = new TextComponent(" - " + ChatColor.stripColor(viewDesc));
        viewDescComp.setColor(ChatColor.GRAY);

        viewCmd.addExtra(viewText);
        viewCmd.addExtra(viewDescComp);
        player.spigot().sendMessage(viewCmd);

        // /cache clearall - clickable
        TextComponent clearAllCmd = new TextComponent("  " + usageIcon + " ");
        clearAllCmd.setColor(ChatColor.DARK_GRAY);

        TextComponent clearAllText = new TextComponent("/cache clearall");
        clearAllText.setColor(ChatColor.RED);
        clearAllText.setBold(false);
        clearAllText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/cache clearall"));

        String clearAllHover = translate(CACHE + "Cache-click-suggest");
        String clearAllWarning = translate(CACHE + "Cache-clearall-warning");
        clearAllText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.stripColor(clearAllHover))
                        .color(ChatColor.GRAY).append("\n")
                        .append(ChatColor.stripColor(clearAllWarning))
                        .color(ChatColor.RED).bold(true).create()));

        String clearAllDesc = translate(CACHE + "Cache-clearall-description");
        TextComponent clearAllDescComp = new TextComponent(" - " + ChatColor.stripColor(clearAllDesc));
        clearAllDescComp.setColor(ChatColor.GRAY);

        clearAllCmd.addExtra(clearAllText);
        clearAllCmd.addExtra(clearAllDescComp);
        player.spigot().sendMessage(clearAllCmd);

        // /cache <player> - clickable
        TextComponent clearPlayerCmd = new TextComponent("  " + usageIcon + " ");
        clearPlayerCmd.setColor(ChatColor.DARK_GRAY);

        TextComponent clearPlayerText = new TextComponent("/cache <player>");
        clearPlayerText.setColor(ChatColor.GOLD);
        clearPlayerText.setBold(false);
        clearPlayerText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/cache "));

        String clearPlayerHover = translate(CACHE + "Cache-click-suggest");
        clearPlayerText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.stripColor(clearPlayerHover))
                        .color(ChatColor.GRAY).create()));

        String clearPlayerDesc = translate(CACHE + "Cache-clear-player-description");
        TextComponent clearPlayerDescComp = new TextComponent(" - " + ChatColor.stripColor(clearPlayerDesc));
        clearPlayerDescComp.setColor(ChatColor.GRAY);

        clearPlayerCmd.addExtra(clearPlayerText);
        clearPlayerCmd.addExtra(clearPlayerDescComp);
        player.spigot().sendMessage(clearPlayerCmd);

        player.sendMessage("");
        player.sendMessage(ChatColor.DARK_GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
    }

    /**
     * Handles clearing all player caches.
     *
     * @param player The admin executing the command
     * @param cacheManager The cache manager instance
     */
    private void handleClearAll(Player player, CacheManager cacheManager) {
        cacheManager.clearAllHomes();

        String message = translate(CACHE + "Cache-all-cleared-success");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    /**
     * Handles clearing a specific player's cache.
     *
     * @param player The admin executing the command
     * @param targetName The target player name
     * @param cacheManager The cache manager instance
     */
    private void handleClearPlayer(Player player, String targetName, CacheManager cacheManager) {
        Player targetToClear = Bukkit.getPlayerExact(targetName);
        if (targetToClear == null) {
            sendMsg(player, LANG + "Player-is-not-online");
            return;
        }

        cacheManager.clearHomes(targetToClear.getUniqueId());

        String message = translate(CACHE + "Cache-player-cleared-success")
                .replace("{player}", targetToClear.getName());
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    /**
     * Handles viewing a player's cached homes with interactive teleport buttons.
     *
     * @param player The admin viewing the cache
     * @param targetName The target player name
     * @param cacheManager The cache manager instance
     * @param homeManager The home manager instance
     */
    private void handleViewCache(Player player, String targetName, CacheManager cacheManager, HomeManager homeManager) {
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            sendMsg(player, LANG + "Player-is-not-online");
            return;
        }

        Map<String, Location> homes = cacheManager.getHomes(target.getUniqueId());
        if (homes == null || homes.isEmpty()) {
            String message = translate(CACHE + "Cache-player-empty")
                    .replace("{player}", target.getName());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }

        player.sendMessage("");
        player.sendMessage(ChatColor.DARK_GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

        String headerText = translate(CACHE + "Cache-player-homes-title")
                .replace("{player}", target.getName());
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', headerText));

        String countText = translate(CACHE + "Cache-total-label")
                .replace("{count}", String.valueOf(homes.size()));
        player.sendMessage("              " + ChatColor.translateAlternateColorCodes('&', countText));

        player.sendMessage("");

        for (String homeName : homeManager.getHomesName(target)) {
            sendInteractiveHomeMessage(homes, homeName, player, target);
        }

        player.sendMessage("");
        player.sendMessage(ChatColor.DARK_GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
    }

    /**
     * Sends an interactive home entry with clickable teleport button.
     *
     * @param homes The map of homes
     * @param homeName The name of the home
     * @param player The player receiving the message
     * @param target The player who owns the home
     */
    private void sendInteractiveHomeMessage(Map<String, Location> homes, String homeName, Player player, Player target) {
        Location homeLocation = homes.get(homeName);
        if (homeLocation == null) {
            return;
        }

        // Home name with icon from language file
        String homeIconRaw = translate(CACHE + "Cache-home-icon");
        String homeIcon = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', homeIconRaw));

        TextComponent homeEntry = new TextComponent("  " + homeIcon + " ");
        homeEntry.setColor(ChatColor.YELLOW);

        TextComponent nameComponent = new TextComponent(homeName);
        nameComponent.setColor(ChatColor.AQUA);
        nameComponent.setBold(true);

        homeEntry.addExtra(nameComponent);
        player.spigot().sendMessage(homeEntry);

        // Location with icon from language file
        String locIconRaw = translate(CACHE + "Cache-location-icon");
        String locIcon = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', locIconRaw));

        TextComponent locationLine = new TextComponent("     " + locIcon + " ");
        locationLine.setColor(ChatColor.GRAY);

        TextComponent coords = new TextComponent(String.format("%.1f, %.1f, %.1f",
                homeLocation.getX(), homeLocation.getY(), homeLocation.getZ()));
        coords.setColor(ChatColor.GREEN);

        String worldLabel = translate(CACHE + "Cache-world-label")
                .replace("{world}", homeLocation.getWorld().getName());
        TextComponent world = new TextComponent(" " + ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', worldLabel)));
        world.setColor(ChatColor.DARK_GRAY);

        locationLine.addExtra(coords);
        locationLine.addExtra(world);
        player.spigot().sendMessage(locationLine);

        // Teleport button (clickable)
        TextComponent buttonLine = new TextComponent("     ");

        String buttonText = translate(CACHE + "Cache-teleport-button");
        TextComponent button = new TextComponent(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', buttonText)));
        button.setColor(ChatColor.YELLOW);
        button.setBold(true);
        button.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                "/tp " + homeLocation.getBlockX() + " " + homeLocation.getBlockY() + " " + homeLocation.getBlockZ()));

        String hoverText = translate(CACHE + "Cache-teleport-hover");
        String xLabel = translate(CACHE + "Cache-coord-label-x");
        String yLabel = translate(CACHE + "Cache-coord-label-y");
        String zLabel = translate(CACHE + "Cache-coord-label-z");

        button.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', hoverText))).color(ChatColor.GRAY).append("\n")
                        .append(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', xLabel))).color(ChatColor.DARK_GRAY)
                        .append(" ").append(String.format("%.1f", homeLocation.getX())).color(ChatColor.GREEN).append("\n")
                        .append(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', yLabel))).color(ChatColor.DARK_GRAY)
                        .append(" ").append(String.format("%.1f", homeLocation.getY())).color(ChatColor.GREEN).append("\n")
                        .append(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', zLabel))).color(ChatColor.DARK_GRAY)
                        .append(" ").append(String.format("%.1f", homeLocation.getZ())).color(ChatColor.GREEN).create()));

        buttonLine.addExtra(button);
        player.spigot().sendMessage(buttonLine);

        player.sendMessage("");
    }

    /**
     * Sends a simple translated message to the sender.
     *
     * @param sender The command sender
     * @param path The language file path
     */
    private void sendMsg(CommandSender sender, String path) {
        sender.sendMessage(translate(path));
    }

    /**
     * Translates a language key to a colored string.
     *
     * @param path The language file path
     * @return The translated and colored string
     */
    private String translate(String path) {
        return HomePlugin.getLanguageManager().getStringWithColor(path);
    }
}