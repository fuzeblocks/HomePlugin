package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.api.event.OnHomeCreate;
import fr.fuzeblocks.homeplugin.home.yml.HomeManager;
import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand implements CommandExecutor {
    private String key = "Config.Language.";
    private OnHomeCreate onHomeCreate;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (args.length == 1) {
                String home_name = args[0];
                HomeManager homeManager = HomePlugin.getHomeManager();
                fr.fuzeblocks.homeplugin.home.sql.HomeManager homeSQLManager = HomePlugin.getHomeSQLManager();
                if (HomePlugin.getRegistrationType() == 1) {
                    if (homeSQLManager.isStatus(player)) {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString("Config.Language.A-teleport-is-already-in-progress")));
                        return false;
                    }
                    if (havePermsHomes(player,1)) return false;
                    onHomeCreate = new OnHomeCreate(player,player.getLocation(),1,home_name);
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
                if (homeManager.isStatus(player))
                    player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString("Config.Language.A-teleport-is-already-in-progress")));
                if (havePermsHomes(player,0)) return false;
                onHomeCreate = new OnHomeCreate(player,player.getLocation(),0,home_name);
                Bukkit.getPluginManager().callEvent(onHomeCreate);
                if (!onHomeCreate.isCancelled()) {
                    if (homeManager.addHome(player, onHomeCreate.getHomeName())) {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Home-added")));
                    } else {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Error")));
                    }
                }
                    return true;
            } else {
                player.sendMessage("§cUtilisation de la commande : /sethome <nom-du-home>");
            }
        }  else {
            sender.sendMessage("§cSeul un joueur peut executer cette commande !");
        }
        return false;
    }
    private boolean havePermsHomes(Player player,int i) {
        int count = HomePlugin.getConfigurationSection().getInt("Config.Home.Home-limite-for-player");
        if (player.hasPermission(HomePlugin.getConfigurationSection().getString("Config.Home.Home-limite-permission-for-bypass"))) return false;
        boolean returnBoolean;
        if (i == 1) {
            returnBoolean = HomePlugin.getHomeSQLManager().getHomeNumber(player) == count;
        } else {
            returnBoolean = HomePlugin.getHomeManager().getHomeNumber(player) == count;
        }
        if (returnBoolean) player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString("Config.Home.Home-limite-message")));

        return returnBoolean;

    }

}

