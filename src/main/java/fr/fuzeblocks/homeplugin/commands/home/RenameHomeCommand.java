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

/**
 * Command to rename an existing home.
 */
public class RenameHomeCommand implements CommandExecutor {

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

        if (!player.hasPermission("homeplugin.command.renamehome")) {
            player.sendMessage(languageManager.getStringWithColor(LANG + "No-permission"));
            return false;
        }

        if (args.length != 2) {
            sendUsage(player);
            return false;
        }

        return renameHome(player, args[0], args[1], languageManager);
    }

    /**
     * Renames a home from old name to new name.
     *
     * @param player          The player renaming the home
     * @param oldHomeName     The current home name
     * @param newHomeName     The new home name
     * @param languageManager The language manager instance
     * @return true if successful, false otherwise
     */
    private boolean renameHome(Player player, String oldHomeName, String newHomeName, LanguageManager languageManager) {
        HomeManager homeManager = HomePlugin.getHomeManager();

        if (!homeManager.exist(player, oldHomeName)) {
            player.sendMessage(languageManager.getStringWithColor(HOME + "Home-does-not-exist"));
            return false;
        }

        if (homeManager.exist(player, newHomeName)) {
            player.sendMessage(languageManager.getStringWithColor(HOME + "Home-already-exists")
                    .replace("{home}", newHomeName)
                    .replace("%home%", newHomeName));
            return false;
        }

        Location homeLocation = homeManager.getHomeLocation(player, oldHomeName);

        if (homeLocation == null) {
            player.sendMessage(languageManager.getStringWithColor(LANG + "Error"));
            return false;
        }

        boolean success = homeManager.renameHome(player, oldHomeName, newHomeName);

        if (success) {
            HomePlugin.getCacheManager().removeHome(player.getUniqueId(), oldHomeName);
            HomePlugin.getCacheManager().addHome(player.getUniqueId(), newHomeName, homeLocation);

            player.sendMessage(languageManager.getStringWithColor(HOME + "Home-renamed")
                    .replace("{old_home}", oldHomeName)
                    .replace("%old_home%", oldHomeName)
                    .replace("{new_home}", newHomeName)
                    .replace("%new_home%", newHomeName));
        } else {
            player.sendMessage(languageManager.getStringWithColor(LANG + "Error"));
        }

        return success;
    }

    private void sendUsage(Player player) {
        player.sendMessage(ChatColor.DARK_GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

        // Title
        String title = translate(HOME + "Rename-management-title");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "         " + title));
        player.sendMessage("");

        // Get usage icon from language file
        String usageIcon = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', translate(HOME + "Rename-usage-icon")));

        // /renamehome <old_name> <new_name> - clickable
        TextComponent viewCmd = new TextComponent("  " + usageIcon + " ");
        viewCmd.setColor(ChatColor.DARK_GRAY);

        TextComponent viewText = new TextComponent("/renamehome <old_name> <new_name>");
        viewText.setColor(ChatColor.YELLOW);
        viewText.setBold(false);
        viewText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/renamehome <old_name> <new_name>"));

        String viewHover = translate(HOME + "Rename-click-suggest");
        viewText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.stripColor(viewHover))
                        .color(ChatColor.GRAY).create()));

        String viewDesc = translate(HOME + "Rename-view-description");
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