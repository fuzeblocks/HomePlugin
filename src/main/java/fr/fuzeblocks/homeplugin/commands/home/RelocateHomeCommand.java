package fr.fuzeblocks.homeplugin.commands.home;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
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

import static fr.fuzeblocks.homeplugin.commands.home.SetHomeCommand.isFair;
import static fr.fuzeblocks.homeplugin.home.HomePermissionManager.canSetHome;

/**
 * Command to relocate an existing home to the player's current location.
 */
public class RelocateHomeCommand implements CommandExecutor {

    private static final String HOME = "Home.";
    private static final String LANG = "Language.";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final LanguageManager languageManager = HomePlugin.getLanguageManager();

        if (!(sender instanceof Player)) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Only-a-player-can-execute"));
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("homeplugin.command.relocatehome")) {
            player.sendMessage(languageManager.getStringWithColor(LANG + "No-permission"));
            return false;
        }

        if (args.length != 1) {
            sendUsage(player);
            return false;
        }

        return relocateHome(player, args[0], languageManager);
    }

    /**
     * Relocates a home to the player's current location.
     *
     * @param player The player relocating the home
     * @param homeName The name of the home to relocate
     * @param languageManager The language manager instance
     * @return true if successful, false otherwise
     */
    private boolean relocateHome(Player player, String homeName, LanguageManager languageManager) {
        HomeManager homeManager = HomePlugin.getHomeManager();

        if (!homeManager.exist(player, homeName)) {
            player.sendMessage(languageManager.getStringWithColor(HOME + "Home-does-not-exist"));
            return false;
        }

        if (!canSetHome(player)) {
            return false;
        }

        if (HomePlugin.getConfigurationSection().getBoolean("Config.Home.Prevent-Unfair-Location", true)) {
            if (!isFair(player)) {
                return false;
            }
        }

        Location newLocation = player.getLocation();

        if (newLocation.getWorld() == null) {
            player.sendMessage(languageManager.getStringWithColor(HOME + "Invalid-world"));
            return false;
        }

        boolean success = homeManager.relocateHome(player, homeName, newLocation);

        if (success) {
            HomePlugin.getCacheManager().relocateHome(player.getUniqueId(), homeName, newLocation);
            player.sendMessage(languageManager.getStringWithColor(HOME + "Home-relocated")
                    .replace("{home}", homeName)
                    .replace("%home%", homeName));
        } else {
            player.sendMessage(languageManager.getStringWithColor(LANG + "Error"));
        }

        return success;
    }
    private void sendUsage(Player player) {
        player.sendMessage(ChatColor.DARK_GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

        // Title
        String title =  translate(HOME + "Relocate-management-title");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "         " + title));
        player.sendMessage("");

        // Get usage icon from language file
        String usageIcon = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', translate(HOME + "Relocate-usage-icon")));

        // /relocatehome <existing_home> <new_home_name> - clickable
        TextComponent viewCmd = new TextComponent("  " + usageIcon + " ");
        viewCmd.setColor(ChatColor.DARK_GRAY);

        TextComponent viewText = new TextComponent("/relocatehome <existing_home> <new_home_name>");
        viewText.setColor(ChatColor.YELLOW);
        viewText.setBold(false);
        viewText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/relocatehome <existing_home> <new_home_name>"));

        String viewHover = translate(HOME + "Relocate-click-suggest");
        viewText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.stripColor(viewHover))
                        .color(ChatColor.GRAY).create()));

        String viewDesc = translate(HOME + "Relocate-view-description");
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