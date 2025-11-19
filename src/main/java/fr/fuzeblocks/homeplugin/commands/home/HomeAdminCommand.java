package fr.fuzeblocks.homeplugin.commands.home;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
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

import java.util.List;

/**
 * Admin command for managing player homes.
 * Provides list, delete, teleport, and add functionality for player homes.
 */
public class HomeAdminCommand implements CommandExecutor {

    private static final String LANG = "Language.";
    private static final String ADMIN = "HomeAdmin.";
    private static final LanguageManager languageManager = HomePlugin.getLanguageManager();

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
                    sendMsg(player, ADMIN + "List-usage");
                    return false;
                }
                return listHomes(player, args[1], homeManager);

            case "deletehome":
                if (args.length < 3) {
                    sendMsg(player, ADMIN + "DeleteHome-usage");
                    return false;
                }
                return deleteHome(player, args[1], args[2], homeManager);

            case "tphome":
                if (args.length < 3) {
                    sendMsg(player, ADMIN + "TpHome-usage");
                    return false;
                }
                return teleportToHome(player, args[1], args[2], homeManager);

            case "addhome":
                if (args.length < 3) {
                    sendMsg(player, ADMIN + "AddHome-usage");
                    return false;
                }
                return addHome(player, args[1], args[2], homeManager);

            default:
                sendUsage(player);
                return false;
        }
    }

    /**
     * Lists all homes of a target player with interactive teleport buttons.
     *
     * @param player The admin viewing the list
     * @param targetName The target player name
     * @param homeManager The home manager instance
     * @return true if successful, false otherwise
     */
    private boolean listHomes(Player player, String targetName, HomeManager homeManager) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sendErrorMessage(player, translate(LANG + "Player-is-not-online"));
            return false;
        }

        List<String> homeNames = homeManager.getHomesName(target);

        if (homeNames == null || homeNames.isEmpty()) {
            sendErrorMessage(player, languageManager.getStringWithColor(ADMIN + "ListHome-has-no-homes").replace("%player%", target.getName()));
            return false;
        }

        // Header
        player.sendMessage("");
        player.sendMessage(ChatColor.DARK_GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

        TextComponent header = new TextComponent("         ");
        TextComponent playerHeader = new TextComponent(languageManager.getStringWithColor(ADMIN + "Home-list-header").replace("%player%", target.getName()));
        playerHeader.setColor(ChatColor.GOLD);
        playerHeader.setBold(true);
        header.addExtra(playerHeader);
        player.spigot().sendMessage(header);

        TextComponent count = new TextComponent("              ");
        TextComponent countText = new TextComponent(languageManager.getStringWithColor(ADMIN + "Home-count").replace("%count%", String.valueOf(homeNames.size())));
        countText.setColor(ChatColor.GRAY);
        count.addExtra(countText);
        player.spigot().sendMessage(count);

        player.sendMessage("");


        for (String homeName : homeNames) {
            if (homeName != null) {
                Location loc = homeManager.getHomeLocation(target, homeName);
                if (loc != null) {
                    sendInteractiveHomeEntry(player, target, homeName, loc);
                }
            }
        }

        player.sendMessage("");
        player.sendMessage(ChatColor.DARK_GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        return true;
    }

    /**
     * Sends an interactive home entry with teleport and delete buttons.
     *
     * @param admin The admin viewing the entry
     * @param target The home owner
     * @param homeName The home name
     * @param location The home location
     */
    private void sendInteractiveHomeEntry(Player admin, Player target, String homeName, Location location) {
        TextComponent homeEntry = new TextComponent(languageManager.getStringWithColor(ADMIN + "Home-icon"));
        homeEntry.setColor(ChatColor.YELLOW);

        TextComponent nameComponent = new TextComponent(homeName);
        nameComponent.setColor(ChatColor.AQUA);
        nameComponent.setBold(true);

        homeEntry.addExtra(nameComponent);
        admin.spigot().sendMessage(homeEntry);


        TextComponent locationLine = new TextComponent("     ");

        TextComponent locIcon = new TextComponent("📍 ");
        locIcon.setColor(ChatColor.GRAY);

        TextComponent coords = new TextComponent(String.format("%.1f, %.1f, %.1f",
                location.getX(), location.getY(), location.getZ()));
        coords.setColor(ChatColor.GREEN);

        TextComponent world = new TextComponent(" (" + location.getWorld().getName() + ")");
        world.setColor(ChatColor.DARK_GRAY);

        locationLine.addExtra(locIcon);
        locationLine.addExtra(coords);
        locationLine.addExtra(world);
        admin.spigot().sendMessage(locationLine);


        TextComponent buttonLine = new TextComponent("     ");

        TextComponent tpButton = new TextComponent("[ᴛᴘ]");
        tpButton.setColor(ChatColor.GREEN);
        tpButton.setBold(true);
        tpButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                "/homeadmin tphome " + target.getName() + " " + homeName));
        tpButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(languageManager.getString(ADMIN + "Click-to-teleport")).color(ChatColor.GRAY).create()));

        TextComponent space = new TextComponent("  ");

        TextComponent delButton = new TextComponent("[ᴅᴇʟᴇᴛᴇ]");
        delButton.setColor(ChatColor.RED);
        delButton.setBold(true);
        delButton.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                "/homeadmin deletehome " + target.getName() + " " + homeName));
        delButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(languageManager.getString(ADMIN + "Click-to-delete") + "\n").color(ChatColor.GRAY)
                        .append(languageManager.getString(ADMIN + "Click-to-delete-warning")).color(ChatColor.YELLOW).create()));

        buttonLine.addExtra(tpButton);
        buttonLine.addExtra(space);
        buttonLine.addExtra(delButton);
        admin.spigot().sendMessage(buttonLine);

        admin.sendMessage("");
    }

    /**
     * Deletes a home from a target player.
     *
     * @param admin The admin executing the command
     * @param targetName The target player name
     * @param homeName The home name to delete
     * @param homeManager The home manager instance
     * @return true if successful, false otherwise
     */
    private boolean deleteHome(Player admin, String targetName, String homeName, HomeManager homeManager) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sendErrorMessage(admin, translate(LANG + "Player-is-not-online"));
            return false;
        }


        Location homeLocation = homeManager.getHomeLocation(target, homeName);
        if (homeLocation == null) {
            sendErrorMessage(admin, languageManager.getStringWithColor(ADMIN + "Home-not-found-admin").replace("%home%", homeName).replace("%player%", target.getName()));
            return false;
        }

        boolean deleted = homeManager.deleteHome(target, homeName);
        if (deleted) {

            HomePlugin.getCacheManager().removeHome(target.getUniqueId(), homeName);

            TextComponent message = new TextComponent("✓ ");
            message.setColor(ChatColor.GREEN);
            message.setBold(true);

            TextComponent text = new TextComponent(languageManager.getStringWithColor(ADMIN + "DeleteHome-text") + " ");
            text.setColor(ChatColor.GREEN);
            text.setBold(false);

            TextComponent home = new TextComponent(homeName);
            home.setColor(ChatColor.AQUA);
            home.setBold(true);

            TextComponent from = new TextComponent(" " + languageManager.getString("DeleteHome-from-player") + " ");
            from.setColor(ChatColor.GREEN);
            from.setBold(false);

            TextComponent player = new TextComponent(target.getName());
            player.setColor(ChatColor.YELLOW);
            player.setBold(true);

            message.addExtra(text);
            message.addExtra(home);
            message.addExtra(from);
            message.addExtra(player);
            admin.spigot().sendMessage(message);
            return true;
        } else {
            sendErrorMessage(admin, languageManager.getStringWithColor(ADMIN + "Failed-to-delete-home").replace("%home%", homeName).replace("%player%", target.getName()));
            return false;
        }
    }

    /**
     * Teleports the admin to a player's home.
     *
     * @param admin The admin executing the command
     * @param targetName The target player name
     * @param homeName The home name to teleport to
     * @param homeManager The home manager instance
     * @return true if successful, false otherwise
     */
    private boolean teleportToHome(Player admin, String targetName, String homeName, HomeManager homeManager) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sendErrorMessage(admin, translate(LANG + "Player-is-not-online"));
            return false;
        }

        Location loc = homeManager.getHomeLocation(target, homeName);
        if (loc == null) {
            sendErrorMessage(admin, languageManager.getStringWithColor(ADMIN + "Home-not-found-admin").replace("%home%", homeName).replace("%player%", target.getName()));
            return false;
        }

        admin.teleport(loc);

        TextComponent message = new TextComponent("✓ ");
        message.setColor(ChatColor.GREEN);
        message.setBold(true);

        TextComponent text = new TextComponent(languageManager.getStringWithColor(ADMIN + "TeleportHome-Text") + " ");
        text.setColor(ChatColor.GREEN);
        text.setBold(false);

        TextComponent player = new TextComponent(target.getName());
        player.setColor(ChatColor.YELLOW);
        player.setBold(true);

        TextComponent home = new TextComponent(homeName);
        home.setColor(ChatColor.AQUA);
        home.setBold(true);

        message.addExtra(text);
        message.addExtra(player);
        message.addExtra(home);
        admin.spigot().sendMessage(message);
        return true;
    }

    /**
     * Adds a home for a target player at the admin's current location.
     *
     * @param admin The admin executing the command
     * @param targetName The target player name
     * @param homeName The home name to add
     * @param homeManager The home manager instance
     * @return true if successful, false otherwise
     */
    private boolean addHome(Player admin, String targetName, String homeName, HomeManager homeManager) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sendErrorMessage(admin, translate(LANG + "Player-is-not-online"));
            return false;
        }

        boolean added = homeManager.addHome(target, homeName);

        if (added) {

            HomePlugin.getCacheManager().addHome(target.getUniqueId(), homeName, admin.getLocation());

            TextComponent message = new TextComponent("✓ ");
            message.setColor(ChatColor.GREEN);
            message.setBold(true);

            TextComponent text = new TextComponent(languageManager.getStringWithColor(ADMIN + "AddHome-text") + " ");
            text.setColor(ChatColor.GREEN);
            text.setBold(false);

            TextComponent home = new TextComponent(homeName);
            home.setColor(ChatColor.AQUA);
            home.setBold(true);

            TextComponent forText = new TextComponent(" " + languageManager.getString("AddHome-to-player") + " ");
            forText.setColor(ChatColor.GREEN);
            forText.setBold(false);

            TextComponent player = new TextComponent(target.getName());
            player.setColor(ChatColor.YELLOW);
            player.setBold(true);

            message.addExtra(text);
            message.addExtra(home);
            message.addExtra(forText);
            message.addExtra(player);
            admin.spigot().sendMessage(message);
            return true;
        } else {
            sendErrorMessage(admin, languageManager.getStringWithColor(ADMIN + "Home-Already-Exists-Admin").replace("%home%", homeName).replace("%player%", target.getName()));
            return false;
        }
    }

    /**
     * Sends an interactive usage message with clickable commands.
     *
     * @param player The player to send the usage to
     */
    private void sendUsage(Player player) {
        player.sendMessage(ChatColor.DARK_GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "         "
        + languageManager.getStringWithColor(ADMIN + "Usage-title"));
        player.sendMessage("");

        sendUsageCommand(player, "/homeadmin list <player>", languageManager.getStringWithColor(ADMIN + "List-usage-description"));
        sendUsageCommand(player, "/homeadmin tphome <player> <home>", languageManager.getStringWithColor(ADMIN + "TpHome-usage-description"));
        sendUsageCommand(player, "/homeadmin addhome <player> <home>", languageManager.getStringWithColor(ADMIN + "AddHome-usage-description"));
        sendUsageCommand(player, "/homeadmin deletehome <player> <home>", languageManager.getStringWithColor(ADMIN + "DeleteHome-usage-description"));

        player.sendMessage("");
        player.sendMessage(ChatColor.DARK_GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
    }

    /**
     * Sends a single usage command entry with click functionality.
     *
     * @param player The player to send to
     * @param command The command text
     * @param description The command description
     */
    private void sendUsageCommand(Player player, String command, String description) {
        TextComponent cmd = new TextComponent("  ▸ ");
        cmd.setColor(ChatColor.DARK_GRAY);

        TextComponent cmdText = new TextComponent(command);
        cmdText.setColor(ChatColor.YELLOW);
        cmdText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        cmdText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(languageManager.getStringWithColor(ADMIN + "HomeAdmin-click-suggest")).color(ChatColor.GRAY).create()));

        TextComponent desc = new TextComponent(" - " + description);
        desc.setColor(ChatColor.GRAY);

        cmd.addExtra(cmdText);
        cmd.addExtra(desc);
        player.spigot().sendMessage(cmd);
    }

    /**
     * Sends a styled error message.
     *
     * @param player The player to send to
     * @param message The error message
     */
    private void sendErrorMessage(Player player, String message) {
        TextComponent error = new TextComponent("✗ ");
        error.setColor(ChatColor.RED);
        error.setBold(true);

        TextComponent text = new TextComponent(ChatColor.stripColor(message));
        text.setColor(ChatColor.RED);
        text.setBold(false);

        error.addExtra(text);
        player.spigot().sendMessage(error);
    }

    private void sendMsg(CommandSender sender, String path) {
        sender.sendMessage(translate(path));
    }

    private String translate(String path) {
        return HomePlugin.getLanguageManager().getStringWithColor(path);
    }
}