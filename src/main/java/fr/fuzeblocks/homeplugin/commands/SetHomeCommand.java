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
    private OnHomeCreatedEvent onHomeCreate;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (args.length == 1) {
                String home_name = args[0];
                HomeYMLManager homeYMLManager = HomePlugin.getHomeManager();
                HomeSQLManager homeSQLManager = HomePlugin.getHomeSQLManager();
                if (HomePlugin.getRegistrationType().equals(SyncMethod.MYSQL)) {
                    if (homeSQLManager.isStatus(player)) {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("A-teleport-is-already-in-progress")));
                        return false;
                    }
                    if (havePermsHomes(player, 1)) return false;
                    onHomeCreate = new OnHomeCreatedEvent(player, player.getLocation(), SyncMethod.MYSQL, home_name);
                    Bukkit.getPluginManager().callEvent(onHomeCreate);
                    if (!onHomeCreate.isCancelled()) {
                        if (homeSQLManager.addHome(player, onHomeCreate.getHomeName())) {
                            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Home-added")));
                        } else {
                            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Home-Error")));
                        }
                    }
                    return true;
                }
                if (homeYMLManager.isStatus(player))
                    player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("A-teleport-is-already-in-progress")));
                if (havePermsHomes(player, 0)) return false;
                onHomeCreate = new OnHomeCreatedEvent(player, player.getLocation(), SyncMethod.YAML, home_name);
                Bukkit.getPluginManager().callEvent(onHomeCreate);
                if (!onHomeCreate.isCancelled()) {
                    if (homeYMLManager.addHome(player, onHomeCreate.getHomeName())) {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Home-added")));
                    } else {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Error")));
                    }
                }
                return true;
            } else {
                player.sendMessage("§cUtilisation de la commande : /sethome <nom-du-home>");
            }
        } else {
            sender.sendMessage("§cSeul un joueur peut executer cette commande !");
        }
        return false;
    }

    private boolean havePermsHomes(Player player, int i) {
        int count = HomePlugin.getConfigurationSection().getInt("Config.Home.Home-limite-for-player");
        if (player.hasPermission(HomePlugin.getConfigurationSection().getString("Config.Home.Home-limite-permission-for-bypass")))
            return false;
        boolean returnBoolean;
        if (i == 1) {
            returnBoolean = HomePlugin.getHomeSQLManager().getHomeNumber(player) == count;
        } else {
            returnBoolean = HomePlugin.getHomeManager().getHomeNumber(player) == count;
        }
        if (returnBoolean)
            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Home.Home-limite-message")));

        return returnBoolean;

    }

}

