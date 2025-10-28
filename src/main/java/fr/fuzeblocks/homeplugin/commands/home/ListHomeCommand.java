package fr.fuzeblocks.homeplugin.commands.home;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Command to list all homes of a player with interactive management options.
 */
public class ListHomeCommand implements CommandExecutor {

    private static final String HOME_LIST = "Home.List.";
    private static final String HOME = "Home.";
    private static final String LANG = "Language.";
    private static final LanguageManager languageManager = HomePlugin.getLanguageManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Only-a-player-can-execute"));
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("homeplugin.command.listhome")) {
            player.sendMessage(languageManager.getStringWithColor(LANG + "No-permission"));
            return false;
        }

        HomeManager homeManager = HomePlugin.getHomeManager();
        List<String> homeNames = homeManager.getHomesName(player);

        if (homeNames == null || homeNames.isEmpty()) {
            player.sendMessage(languageManager.getStringWithColor(HOME + "Have-no-home"));
            return false;
        }

        int homeCount = homeNames.size();

        sendHeader(player, homeCount);

        for (String name : homeNames) {
            if (name != null) {
                Location loc = homeManager.getHomeLocation(player, name);
                if (loc != null && loc.getWorld() != null) {
                    sendHomeEntry(player, name, loc);
                }
            }
        }

        sendFooter(player);

        return true;
    }

    /**
     * Sends the header with home count.
     *
     * @param player The player
     * @param homeCount Number of homes
     */
    private void sendHeader(Player player, int homeCount) {
        Component header = Component.text()
                .append(Component.text("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬", NamedTextColor.DARK_GRAY))
                .build();

        // Title from language file
        String titleText = translate(HOME_LIST + "List-header-title");
        Component title = Component.text()
                .append(Component.text("         "))
                .append(deserialize(titleText))
                .build();

        // Count from language file
        String countText = translate(HOME_LIST + "List-total-label")
                .replace("{count}", String.valueOf(homeCount));
        Component count = Component.text()
                .append(Component.text("              "))
                .append(deserialize(countText))
                .build();

        HomePlugin.getAdventure().player(player).sendMessage(header);
        HomePlugin.getAdventure().player(player).sendMessage(title);
        HomePlugin.getAdventure().player(player).sendMessage(count);
        HomePlugin.getAdventure().player(player).sendMessage(Component.empty());
    }

    /**
     * Sends a single home entry with interactive buttons.
     *
     * @param player The player
     * @param homeName The home name
     * @param location The home location
     */
    private void sendHomeEntry(Player player, String homeName, Location location) {
        // Home name line with icon from language file
        String homeIcon = ChatColor.stripColor(translate(HOME_LIST + "List-home-icon"));
        Component homeNameLine = Component.text()
                .append(Component.text("  "))
                .append(deserialize(translate(HOME_LIST + "List-home-icon")))
                .append(Component.text(" "))
                .append(Component.text(homeName, NamedTextColor.AQUA).decorate(net.kyori.adventure.text.format.TextDecoration.BOLD))
                .build();

        // Location line with icon from language file
        Component locationLine = Component.text()
                .append(Component.text("     "))
                .append(deserialize(translate(HOME_LIST + "List-location-icon")))
                .append(Component.text(" "))
                .append(Component.text(
                        String.format("%d, %d, %d",
                                location.getBlockX(),
                                location.getBlockY(),
                                location.getBlockZ()),
                        NamedTextColor.GREEN))
                .append(Component.text(" "))
                .append(deserialize(translate(HOME_LIST + "List-world-label")
                        .replace("{world}", location.getWorld().getName())))
                .build();

        // Buttons from language file
        Component buttons = Component.text("     ")
                .append(createTeleportButton(homeName))
                .append(Component.text("  "))
                .append(createRelocateButton(homeName))
                .append(Component.text("  "))
                .append(createDeleteButton(homeName));

        HomePlugin.getAdventure().player(player).sendMessage(homeNameLine);
        HomePlugin.getAdventure().player(player).sendMessage(locationLine);
        HomePlugin.getAdventure().player(player).sendMessage(buttons);
        HomePlugin.getAdventure().player(player).sendMessage(Component.empty());
    }

    /**
     * Creates the teleport button component.
     *
     * @param homeName The home name
     * @return The button component
     */
    private Component createTeleportButton(String homeName) {
        String buttonText = translate(HOME_LIST + "List-teleport-button");
        String hoverText = translate(HOME_LIST + "List-teleport-hover")
                .replace("{home}", homeName);

        return deserialize(buttonText)
                .hoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(deserialize(hoverText)))
                .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/home " + homeName));
    }

    /**
     * Creates the relocate button component.
     *
     * @param homeName The home name
     * @return The button component
     */
    private Component createRelocateButton(String homeName) {
        String buttonText = translate(HOME_LIST + "List-relocate-button");
        String hoverText = translate(HOME_LIST + "List-relocate-hover")
                .replace("{home}", homeName);

        return deserialize(buttonText)
                .hoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(deserialize(hoverText)))
                .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/relocatehome " + homeName));
    }

    /**
     * Creates the delete button component.
     *
     * @param homeName The home name
     * @return The button component
     */
    private Component createDeleteButton(String homeName) {
        String buttonText = translate(HOME_LIST + "List-delete-button");
        String hoverText = translate(HOME_LIST + "List-delete-hover")
                .replace("{home}", homeName);

        return deserialize(buttonText)
                .hoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(deserialize(hoverText)))
                .clickEvent(net.kyori.adventure.text.event.ClickEvent.suggestCommand("/delhome " + homeName));
    }

    /**
     * Sends the footer separator.
     *
     * @param player The player
     */
    private void sendFooter(Player player) {
        Component footer = Component.text("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬", NamedTextColor.DARK_GRAY);
        HomePlugin.getAdventure().player(player).sendMessage(footer);
    }

    /**
     * Translates a language key to a colored string.
     *
     * @param path The language file path
     * @return The translated and colored string
     */
    private String translate(String path) {
        return languageManager.getStringWithColor(path);
    }

    /**
     * Deserializes a legacy formatted string (with & color codes) to an Adventure Component.
     *
     * @param text The text with & color codes
     * @return The Adventure Component
     */
    private Component deserialize(String text) {
        // Convert & color codes to § first
        String converted = ChatColor.translateAlternateColorCodes('&', text);
        // Then deserialize to Adventure Component
        return LegacyComponentSerializer.legacySection().deserialize(converted);
    }
}