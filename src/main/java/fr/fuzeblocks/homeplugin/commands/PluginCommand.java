package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.language.LanguageManager;
import fr.fuzeblocks.homeplugin.plugin.HomePlugin;
import fr.fuzeblocks.homeplugin.plugin.PluginManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Command to display information about loaded HomePlugin addons.
 */
public class PluginCommand implements CommandExecutor {

    private static final String PLUGIN_COMMAND = "PluginCommand.";
    private static final String LANGUAGE = "Language.";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(translate(LANGUAGE + "Only-a-player-can-execute"));
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("homeplugin.admin")) {
            player.sendMessage(translate(LANGUAGE + "No-permission"));
            return false;
        }

        sendMessage(player);
        return true;
    }

    /**
     * Sends the plugin list message to the player.
     *
     * @param player The player to send the message to
     */
    private void sendMessage(Player player) {
        PluginManager pluginManager = PluginManager.getInstance();

        if (pluginManager == null) {
            player.sendMessage(translate(PLUGIN_COMMAND + "Plugin-manager-not-initialized"));
            return;
        }

        List<HomePlugin> plugins = pluginManager.getHomePlugin();

        if (plugins == null || plugins.isEmpty()) {
            player.sendMessage(translate(PLUGIN_COMMAND + "No-plugins-loaded"));
            return;
        }

        player.sendMessage(translate(PLUGIN_COMMAND + "Plugin-list-header"));

        for (HomePlugin plugin : plugins) {
            if (plugin != null) {
                String name = plugin.getName();
                String version = plugin.getVersion();

                if (name == null) name = "Unknown";
                if (version == null) version = "Unknown";

                String msg = translate(PLUGIN_COMMAND + "Plugin-info-format")
                        .replace("%name%", name)
                        .replace("%version%", version);
                player.sendMessage(msg);
            }
        }

        player.sendMessage(translate(PLUGIN_COMMAND + "Plugin-list-total")
                .replace("%count%", String.valueOf(plugins.size())));
    }

    /**
     * Translates a language key to colored text.
     *
     * @param path The language key
     * @return The translated colored text
     */
    private String translate(String path) {
        String text = fr.fuzeblocks.homeplugin.HomePlugin.getLanguageManager().getString(path);
        if (text == null) {
            return path;
        }
        return LanguageManager.translateAlternateColorCodes(text);
    }
}