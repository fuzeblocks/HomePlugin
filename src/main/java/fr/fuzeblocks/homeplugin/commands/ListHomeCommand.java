package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The type List home command.
 */
public class ListHomeCommand implements CommandExecutor {

    private final String HOME = "Home.";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String LANG = "Language.";
        if (!(sender instanceof Player)) {
            sender.sendMessage(
                    HomePlugin.getLanguageManager().getStringWithColor(LANG + "Only-a-player-can-execute"));
            return false;
        }
        if (!sender.hasPermission("homeplugin.command.listhome")) {
            sender.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(LANG +"No-permission"));
            return false;
        }

        Player player = (Player) sender;
        HomeManager homeManager = HomePlugin.getHomeManager();
        int homeCount = homeManager.getHomeNumber(player);

        String countMessage = HomePlugin.getLanguageManager().getStringWithColor(HOME + "List.Home-count")
                .replace("%count%", String.valueOf(homeCount));
        player.sendMessage(countMessage);

        for (String name : homeManager.getHomesName(player)) {
            Location loc = homeManager.getHomeLocation(player, name);
            if (loc != null) sendHomeMessage(player, name, loc);
        }

        return true;
    }


    private void sendHomeMessage(Player player, String homeName, Location location) {
        String detailHeader = HomePlugin.getLanguageManager()
                .getString(HOME + "List.Home-detail-header")
                .replace("%home%", homeName);
        player.sendMessage(detailHeader);

        String locationLine = HomePlugin.getLanguageManager().getStringWithColor(HOME + "List.Home-location")
                .replace("%x%", String.valueOf(location.getBlockX()))
                .replace("%y%", String.valueOf(location.getBlockY()))
                .replace("%z%", String.valueOf(location.getBlockZ()))
                .replace("%world%", location.getWorld().getName());
        player.sendMessage(locationLine);

        // Composants interactifs (Adventure)
        Component teleportComponent = Component.text(
                        HomePlugin.getLanguageManager().getString(HOME + "List.Teleport-label"),
                        NamedTextColor.GRAY
                )
                .append(Component.text(" "))
                .append(Component.text(
                                HomePlugin.getLanguageManager().getString(HOME + "List.Teleport-click"),
                                NamedTextColor.YELLOW,
                                TextDecoration.BOLD
                        )
                        .hoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(
                                Component.text(
                                        HomePlugin.getLanguageManager().getString(HOME + "List.Teleport-hover"),
                                        NamedTextColor.GRAY
                                )
                        ))
                        .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/home " + homeName)));

        Component manageComponent = Component.text(
                HomePlugin.getLanguageManager().getString(HOME + "List.Manage-label"),
                NamedTextColor.GRAY
        );

        Component relocateComponent = Component.text(
                        HomePlugin.getLanguageManager().getString(HOME + "List.Relocate-click"),
                        NamedTextColor.GOLD
                )
                .hoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(
                        Component.text(
                                HomePlugin.getLanguageManager().getString(HOME + "List.Relocate-hover"),
                                NamedTextColor.GRAY
                        )
                ))
                .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/homerelocate " + homeName));

        Component deleteComponent = Component.text(
                        HomePlugin.getLanguageManager().getString(HOME + "List.Delete-click"),
                        NamedTextColor.RED
                )
                .hoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(
                        Component.text(
                                HomePlugin.getLanguageManager().getString(HOME + "List.Delete-hover"),
                                NamedTextColor.GRAY
                        )
                ))
                .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/homedelete " + homeName));

        HomePlugin.getAdventure().player(player).sendMessage(teleportComponent);
        HomePlugin.getAdventure().player(player).sendMessage(manageComponent.append(Component.text(" ")).append(relocateComponent).append(Component.text(" ")).append(deleteComponent));
    }

}
