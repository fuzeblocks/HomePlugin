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
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            HomeManager homeManager = HomePlugin.getHomeManager();
            player.sendMessage("§bHome number : " + homeManager.getHomeNumber(player));
            for (String name : homeManager.getHomesName(player)) {
                Location homeLocation = HomePlugin.getHomeManager().getHomeLocation(player,name);
               sendHomeMessage(player,name,homeLocation);
            }
        }
        return false;
    }
    private void sendHomeMessage(Player player, String homeName, Location homeLocation) {

        player.sendMessage(ChatColor.GREEN + "View details for your home, " + ChatColor.BOLD + homeName);

        player.spigot().sendMessage(new ComponentBuilder("Location: ")
                .color(ChatColor.BLUE)
                .append("x: " + homeLocation.getBlockX() + " y: " + homeLocation.getBlockY() + " z: " + homeLocation.getBlockZ())
                .color(ChatColor.GOLD)
                .create());

        TextComponent teleportComponent = createInteractiveComponent(
                "Teleport:",
                ChatColor.GRAY,
                " [Click]",
                ChatColor.YELLOW,
                "Click to teleport to your home",
                "/home " + homeName
        );

        TextComponent manageComponent = new TextComponent("Manage home:");
        manageComponent.setColor(ChatColor.GRAY);

        TextComponent relocateComponent = createInteractiveComponent(
                " [Relocate home]",
                ChatColor.GOLD,
                "Click to relocate your home",
                "/homerelocate"
        );

        TextComponent deleteComponent = createInteractiveComponent(
                " [Delete home]",
                ChatColor.YELLOW,
                "Click to delete your home",
                "/delhome " + homeName
        );

        player.spigot().sendMessage(teleportComponent);
        player.spigot().sendMessage(manageComponent, relocateComponent, deleteComponent);
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
        return createInteractiveComponent("", clickableColor , clickableText, clickableColor, hoverText, command);
    }


}
