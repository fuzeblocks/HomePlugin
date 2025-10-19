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
        String detailHeader = HomePlugin.getLanguageManager().getStringWithColor(HOME + "List.Home-detail-header")
                .replace("%home%", homeName);
        player.sendMessage(detailHeader);

        String locationLine = HomePlugin.getLanguageManager().getStringWithColor(HOME + "List.Home-location")
                .replace("%x%", String.valueOf(location.getBlockX()))
                .replace("%y%", String.valueOf(location.getBlockY()))
                .replace("%z%", String.valueOf(location.getBlockZ()))
                .replace("%world%", location.getWorld().getName());
        player.sendMessage(locationLine);

        // Composants interactifs
        TextComponent teleportComponent = createInteractiveComponent(
                HomePlugin.getLanguageManager().getStringWithColor(HOME + "List.Teleport-label"),
                ChatColor.GRAY,
                HomePlugin.getLanguageManager().getStringWithColor(HOME + "List.Teleport-click"),
                ChatColor.YELLOW,
                HomePlugin.getLanguageManager().getStringWithColor(HOME + "List.Teleport-hover"),
                "/home " + homeName
        );

        TextComponent manageComponent = new TextComponent(
                HomePlugin.getLanguageManager().getStringWithColor(HOME + "List.Manage-label")
        );
        manageComponent.setColor(ChatColor.GRAY);

        TextComponent relocateComponent = createInteractiveComponent(
                HomePlugin.getLanguageManager().getStringWithColor(HOME + "List.Relocate-click"),
                ChatColor.GOLD,
                HomePlugin.getLanguageManager().getStringWithColor(HOME + "List.Relocate-hover"),
                "/homerelocate"
        );

        TextComponent deleteComponent = createInteractiveComponent(
                HomePlugin.getLanguageManager().getStringWithColor(HOME + "List.Delete-click"),
                ChatColor.RED,
                HomePlugin.getLanguageManager().getStringWithColor(HOME + "List.Delete-hover"),
                "/delhome " + homeName
        );

       //TODO send message


    }

    private TextComponent createInteractiveComponent(String label, ChatColor labelColor, String clickableText, ChatColor clickableColor, String hoverText, String command) {
        TextComponent labelComponent = new TextComponent(label);
        labelComponent.setColor(labelColor);

        TextComponent clickableComponent = new TextComponent(clickableText);
        clickableComponent.setColor(clickableColor);
        clickableComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(hoverText).color(ChatColor.GRAY).create()));
        clickableComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));

        labelComponent.addExtra(clickableComponent);
        return labelComponent;
    }

    private TextComponent createInteractiveComponent(String clickableText, ChatColor clickableColor, String hoverText, String command) {
        return createInteractiveComponent("", clickableColor, clickableText, clickableColor, hoverText, command);
    }
}
