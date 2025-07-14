package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.api.event.OnHomeCreatedEvent;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.home.HomePermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand implements CommandExecutor {
    private final String HOME = "Home.";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String LANG = "Language.";
        if (!(sender instanceof Player)) {
            sender.sendMessage(translate(LANG + "Only-a-player-can-execute"));
            return false;
        }

        if (!sender.hasPermission("homeplugin.command.sethome")) {
            sender.sendMessage(translate(HOME + "SetHome-permission-deny-message"));
            return false;
        }

        Player player = (Player) sender;
        HomeManager homeManager = HomePlugin.getHomeManager();

        // /sethome info
        if (args.length == 1 && args[0].equalsIgnoreCase("info")) {
            int used = homeManager.getHomeNumber(player);
            int max = HomePermissionManager.getMaxHomes(player);
            int remaining = max - used;

            player.sendMessage(translate(HOME + "Home-info-count")
                    .replace("%used%", String.valueOf(used))
                    .replace("%max%", String.valueOf(max))
                    .replace("%remaining%", String.valueOf(Math.max(0, remaining))));
            return true;
        }

        // /sethome <name>
        if (args.length == 1) {
            if (homeManager.isStatus(player)) {
                player.sendMessage(translate(LANG + "A-teleport-is-already-in-progress"));
                return false;
            }

            if (!canSetHome(player)) return false;

            String homeName = args[0];
            OnHomeCreatedEvent event = new OnHomeCreatedEvent(player, player.getLocation(), HomePlugin.getRegistrationType(), homeName);
            Bukkit.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                boolean success = homeManager.addHome(player, event.getHomeName());
                if (success) {
                    player.sendMessage(translate(HOME + "Home-added"));
                } else {
                    player.sendMessage(translate(LANG + "Error"));
                }
            }

            return true;
        }
        player.sendMessage(translate(HOME + "SetHome-usage-message"));
        return false;
    }

    private boolean canSetHome(Player player) {
        String bypassPerm = HomePlugin.getLanguageManager().getString(HOME + "Home-limite-permission-for-bypass");
        if (player.hasPermission(bypassPerm)) return true;

        boolean allowed = HomePermissionManager.canSetHome(player);
        if (!allowed) {
            player.sendMessage(translate(HOME + "Home-limite-message"));
        }

        return allowed;
    }

    private String translate(String key) {
        return HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key));
    }
}
