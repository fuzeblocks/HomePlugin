package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ListHomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(HomePlugin.translateAlternateColorCodes(
                    HomePlugin.getLanguageManager().getString("Language.Only-a-player-can-execute")));
            return false;
        }

        Player player = (Player) sender;
        HomeManager homeManager = HomePlugin.getHomeManager();
        int homeCount = homeManager.getHomeNumber(player);

        String countMessage = HomePlugin.getLanguageManager().getString("Home.List.Home-count")
                .replace("%count%", String.valueOf(homeCount));
        player.sendMessage(HomePlugin.translateAlternateColorCodes(countMessage));

        for (String name : homeManager.getHomesName(player)) {
            Location loc = homeManager.getHomeLocation(player, name);
            if (loc != null) sendHomeMessage(player, name, loc);
        }

        return true;
    }

    private void sendHomeMessage(Player player, String homeName, Location location) {
        String detailHeader = HomePlugin.getLanguageManager().getString("Home.List.Home-detail-header")
                .replace("%home%", homeName);
        player.sendMessage(HomePlugin.translateAlternateColorCodes(detailHeader));

        String locationLine = HomePlugin.getLanguageManager().getString("Home.List.Home-location")
                .replace("%x%", String.valueOf(location.getBlockX()))
                .replace("%y%", String.valueOf(location.getBlockY()))
                .replace("%z%", String.valueOf(location.getBlockZ()))
                .replace("%world%", location.getWorld().getName());
        player.sendMessage(HomePlugin.translateAlternateColorCodes(locationLine));

        // Composants interactifs
        TextComponent teleportComponent = createInteractiveComponent(
                HomePlugin.getLanguageManager().getString("Home.List.Teleport-label"),
                ChatColor.GRAY,
                HomePlugin.getLanguageManager().getString("Home.List.Teleport-click"),
                ChatColor.YELLOW,
                HomePlugin.getLanguageManager().getString("Home.List.Teleport-hover"),
                "/home " + homeName
        );

        TextComponent manageComponent = new TextComponent(
                HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Home.List.Manage-label"))
        );
        manageComponent.setColor(ChatColor.GRAY);

        TextComponent relocateComponent = createInteractiveComponent(
                HomePlugin.getLanguageManager().getString("Home.List.Relocate-click"),
                ChatColor.GOLD,
                HomePlugin.getLanguageManager().getString("Home.List.Relocate-hover"),
                "/homerelocate"
        );

        TextComponent deleteComponent = createInteractiveComponent(
                HomePlugin.getLanguageManager().getString("Home.List.Delete-click"),
                ChatColor.RED,
                HomePlugin.getLanguageManager().getString("Home.List.Delete-hover"),
                "/delhome " + homeName
        );

        player.spigot().sendMessage(teleportComponent);
        player.spigot().sendMessage(manageComponent, relocateComponent, deleteComponent);
    }

    private TextComponent createInteractiveComponent(String label, ChatColor labelColor, String clickableText, ChatColor clickableColor, String hoverText, String command) {
        TextComponent labelComponent = new TextComponent(HomePlugin.translateAlternateColorCodes(label));
        labelComponent.setColor(labelColor);

        TextComponent clickableComponent = new TextComponent(HomePlugin.translateAlternateColorCodes(clickableText));
        clickableComponent.setColor(clickableColor);
        clickableComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(HomePlugin.translateAlternateColorCodes(hoverText)).color(ChatColor.GRAY).create()));
        clickableComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));

        labelComponent.addExtra(clickableComponent);
        return labelComponent;
    }

    private TextComponent createInteractiveComponent(String clickableText, ChatColor clickableColor, String hoverText, String command) {
        return createInteractiveComponent("", clickableColor, clickableText, clickableColor, hoverText, command);
    }
}
