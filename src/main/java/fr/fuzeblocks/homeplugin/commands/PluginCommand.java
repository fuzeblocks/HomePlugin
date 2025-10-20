package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.plugin.HomePlugin;
import fr.fuzeblocks.homeplugin.plugin.PluginManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The type Plugin command.
 */
public class PluginCommand implements CommandExecutor {

    private static final String PLUGIN_COMMAND = "PluginCommand.";
    private static final String LANGUAGE = "Language.";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (!sender.hasPermission("homeplugin.admin")) {
                sender.sendMessage(translate(LANGUAGE + "No-permission"));
                return true;
            }
            sendMessage(((Player) sender).getPlayer());
        }
        return false;
    }

    private void sendMessage(Player player) {
        if (PluginManager.getInstance().getHomePlugin().isEmpty()) {
            player.sendMessage(translate(PLUGIN_COMMAND + "No-plugins-loaded"));
            return;
        }
        player.sendMessage(translate(PLUGIN_COMMAND + "Plugin-list-header"));
        for (HomePlugin plugin : PluginManager.getInstance().getHomePlugin()) {
            String msg = translate(PLUGIN_COMMAND + "Plugin-info-format")
                    .replace("%name%", plugin.getName())
                    .replace("%version%", plugin.getVersion());
            player.sendMessage(msg);
        }
        player.sendMessage(translate(PLUGIN_COMMAND + "Plugin-list-total")
                .replace("%count%", String.valueOf(PluginManager.getInstance().getHomePlugin().size())));
    }

    private String translate(String path) {
        return fr.fuzeblocks.homeplugin.HomePlugin.translateAlternateColorCodes(
                fr.fuzeblocks.homeplugin.HomePlugin.getLanguageManager().getString(path)
        );
    }
}
