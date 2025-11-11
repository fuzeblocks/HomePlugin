package fr.fuzeblocks.homeplugin.commands.home;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.event.OnHomeDeletedEvent;
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

/**
 * Command to delete a player's home.
 */
public class DeleteHomeCommand implements CommandExecutor {

    private static final String LANG = "Language.";
    private static final String HOME = "Home.";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final LanguageManager languageManager = HomePlugin.getLanguageManager();


        if (!(sender instanceof Player)) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Only-a-player-can-execute"));
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("homeplugin.command.delhome")) {
            player.sendMessage(languageManager.getStringWithColor(LANG + "No-permission"));
            return false;
        }

        if (args.length != 1) {
            sendUsage(player);
            return false;
        }

        String homeName = args[0];
        HomeManager homeManager = HomePlugin.getHomeManager();

        Location homeLocation = homeManager.getHomeLocation(player, homeName);

        if (homeLocation == null) {
            player.sendMessage(languageManager.getStringWithColor(HOME + "Home-not-found"));
            return false;
        }

        OnHomeDeletedEvent event = new OnHomeDeletedEvent(player, homeLocation, homeName);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return false;
        }

        if (homeManager.deleteHome(player, event.getHomeName())) {
            HomePlugin.getCacheManager().removeHome(player.getUniqueId(), event.getHomeName());
            player.sendMessage(languageManager.getStringWithColor(HOME + "Home-deleted"));
            return true;
        }

        return false;
    }

    private void sendUsage(Player player) {
        player.sendMessage(ChatColor.DARK_GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

        // Title
        String title = translate(HOME + "DelHome-management-title");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "         " + title));
        player.sendMessage("");

        // Get usage icon from language file
        String usageIcon = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', translate(HOME + "DelHome-usage-icon")));

        // /delhome <home-name> - clickable
        TextComponent viewCmd = new TextComponent("  " + usageIcon + " ");
        viewCmd.setColor(ChatColor.DARK_GRAY);

        TextComponent viewText = new TextComponent("/delhome <home-name>");
        viewText.setColor(ChatColor.YELLOW);
        viewText.setBold(false);
        viewText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/delhome <home-name>"));

        String viewHover = translate(HOME + "DelHome-click-suggest");
        viewText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.stripColor(viewHover))
                        .color(ChatColor.GRAY).create()));

        String viewDesc = translate(HOME + "DelHome-view-description");
        TextComponent viewDescComp = new TextComponent(" - " + ChatColor.stripColor(viewDesc));
        viewDescComp.setColor(ChatColor.GRAY);

        viewCmd.addExtra(viewText);
        viewCmd.addExtra(viewDescComp);
        player.spigot().sendMessage(viewCmd);
        player.sendMessage("");
        player.sendMessage(ChatColor.DARK_GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
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