package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Content;
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

        BaseComponent[] locationComponent = new ComponentBuilder("Location: ")
                .color(ChatColor.BLUE)
                .append("x: " + homeLocation.getBlockX() + " y: " + homeLocation.getBlockY() + " z: " + homeLocation.getBlockZ())
                .color(ChatColor.GOLD)
                .create();
        player.spigot().sendMessage(locationComponent);
        TextComponent teleportTextComponent = new TextComponent("Teleport:");
        teleportTextComponent.setColor(ChatColor.GRAY);
        TextComponent teleportationTextComponent = new TextComponent(" [Click]");
        teleportationTextComponent.setColor(ChatColor.YELLOW);
        teleportationTextComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to teleport at your home").create()));
        teleportationTextComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home " + homeName));
        player.spigot().sendMessage(teleportTextComponent,teleportationTextComponent);

        TextComponent manageTextComponent = new TextComponent("Manage home:");
        manageTextComponent.setColor(ChatColor.GRAY);

        TextComponent relocateTextComponent = new TextComponent(" [Relocate home]");
        relocateTextComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to relocate your home").create()));
        relocateTextComponent.setColor(ChatColor.GOLD);
        relocateTextComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/homerelocate"));

        TextComponent deleteTextComponent = new TextComponent(" [Delete home]");
        deleteTextComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to delete your home").create()));
        deleteTextComponent.setColor(ChatColor.YELLOW);
        deleteTextComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/delhome " + homeName));

        player.spigot().sendMessage(manageTextComponent, relocateTextComponent, deleteTextComponent);

    }

}
