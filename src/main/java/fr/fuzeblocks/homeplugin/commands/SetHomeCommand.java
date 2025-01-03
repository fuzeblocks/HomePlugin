package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.api.event.OnHomeCreatedEvent;
import fr.fuzeblocks.homeplugin.home.sql.HomeSQLManager;
import fr.fuzeblocks.homeplugin.home.yml.HomeYMLManager;
import fr.fuzeblocks.homeplugin.sync.type.SyncMethod;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand implements CommandExecutor {
    private final String key = "Config.Language.";
    private final String homeKey = "Config.Home.";
    private OnHomeCreatedEvent onHomeCreate;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (args.length == 1) {
                String home_name = args[0];
                HomeYMLManager homeYMLManager = HomePlugin.getHomeYMLManager();
                HomeSQLManager homeSQLManager = HomePlugin.getHomeSQLManager();
                if (HomePlugin.getRegistrationType().equals(SyncMethod.MYSQL)) {
                    if (homeSQLManager.isStatus(player)) {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "A-teleport-is-already-in-progress")));
                        return false;
                    }
                    if (havePermsHomes(player, 1)) return false;
                    onHomeCreate = new OnHomeCreatedEvent(player, player.getLocation(), SyncMethod.MYSQL, home_name);
                    Bukkit.getPluginManager().callEvent(onHomeCreate);
                    if (!onHomeCreate.isCancelled()) {
                        if (homeSQLManager.addHome(player, onHomeCreate.getHomeName())) {
                            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Home-added")));
                        } else {
                            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Home-Error")));
                        }
                    }
                    return true;
                }
                if (homeYMLManager.isStatus(player))
                    player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "A-teleport-is-already-in-progress")));
                if (havePermsHomes(player, 0)) return false;
                onHomeCreate = new OnHomeCreatedEvent(player, player.getLocation(), SyncMethod.YAML, home_name);
                Bukkit.getPluginManager().callEvent(onHomeCreate);
                if (!onHomeCreate.isCancelled()) {
                    if (homeYMLManager.addHome(player, onHomeCreate.getHomeName())) {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Home-added")));
                    } else {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Error")));
                    }
                }
                return true;
            } else {
                player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(homeKey + "SetHome-usage-message")));
            }
        } else {
            sender.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Only-a-player-can-execute")));
        }
        return false;
    }

    private boolean havePermsHomes(Player player, int i) {
        int count = HomePlugin.getConfigurationSection().getInt(homeKey + "Home-limite-for-player");
        if (player.hasPermission(HomePlugin.getConfigurationSection().getString( homeKey + "Home-limite-permission-for-bypass")))
            return false;
        boolean returnBoolean;
        if (i == 1) {
            returnBoolean = HomePlugin.getHomeSQLManager().getHomeNumber(player) == count;
        } else {
            returnBoolean = HomePlugin.getHomeYMLManager().getHomeNumber(player) == count;
        }
        if (returnBoolean)
            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(homeKey + "Home-limite-message")));

        return returnBoolean;

    }

}