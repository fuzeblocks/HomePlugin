package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.home.yml.HomeManager;
import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.status.StatusManager ;
import fr.fuzeblocks.homeplugin.task.TaskManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static fr.fuzeblocks.homeplugin.task.TaskSaveUtils.setTaskManagerInstance;


public class HomeCommand implements CommandExecutor {
    private final HomePlugin instance;
    private static TaskManager taskManager;
    private String key = "Config.Language.";

    public HomeCommand(HomePlugin instance) {
        this.instance = instance;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                String homeName = args[0];
                HomeManager homeManager = HomePlugin.getHomeManager();
                fr.fuzeblocks.homeplugin.home.sql.HomeManager homeSQLManager = HomePlugin.getHomeSQLManager();
                if (HomePlugin.getRegistrationType() == 1) {
                    if (homeSQLManager.isStatus(player)) {
                        return false;
                    }
                    if (homeSQLManager.getHomeNumber(player) > 0) {
                        if (verifyInCache(homeSQLManager, player, homeName)) {
                            setPlayerTeleportation(player, homeName, homeSQLManager.getCacheManager().getHomesInCache(player).get(homeName));
                            return true;
                        }
                        Location homeLocation = homeSQLManager.getHomeLocation(player, homeName);
                        if (homeLocation != null) {
                            homeSQLManager.getCacheManager().addHomeInCache(player, homeName, homeLocation);
                            setPlayerTeleportation(player, homeName, homeLocation);
                            return true;
                        } else {
                            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Home-does-not-exist")));
                        }
                    } else {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Have-no-home")));
                    }
                } else {
                    if (homeManager.isStatus(player)) {
                        return false;
                    }
                    if (homeManager.getHomeNumber(player) > 0) {
                        if (verifyInCache(homeManager, player, homeName)) {
                            setPlayerTeleportation(player, homeName, homeManager.getCacheManager().getHomesInCache(player).get(homeName));
                            return true;
                        }
                        Location homeLocation = homeManager.getHomeLocation(player, homeName);
                        if (homeLocation != null) {
                            homeManager.getCacheManager().addHomeInCache(player, homeName, homeLocation);
                            setPlayerTeleportation(player, homeName, homeLocation);
                            return true;
                        } else {
                            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Home-does-not-exist")));
                        }
                    } else {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Have-no-home")));
                    }
                }
            } else {
                player.sendMessage("§cUtilisation de la commande : /home <nom-du-home>");
            }
        } else {
            sender.sendMessage("§cSeul un joueur peut executer cette commande !");
        }
        return false;
    }
    private void setPlayerTeleportation(Player player,String homeName,Location location) {
        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Start-of-teleportation")) + " " + homeName);
        StatusManager.setPlayerStatus(player, true);
        taskManager = new TaskManager(instance);
        taskManager.homeTask(homeName,player,location);
        taskManager.startTeleportTask();
        setTaskManagerInstance(player,taskManager);
    }

   private boolean verifyInCache(HomeManager homeManager, Player player, String homeName) {
       if (homeManager.getCacheManager().getHomesInCache(player) != null) {
           HashMap<String, Location> homes = homeManager.getCacheManager().getHomesInCache(player);
           if (homes.containsKey(homeName)) {
               return true;
           }
       }  else {
           return false;
       }
       return false;
   }
    private boolean verifyInCache(fr.fuzeblocks.homeplugin.home.sql.HomeManager homeManager, Player player, String homeName) {
        if (homeManager.getCacheManager().getHomesInCache(player) != null) {
            HashMap<String, Location> homes = homeManager.getCacheManager().getHomesInCache(player);
            if (homes.containsKey(homeName)) {
                return true;
            }
        }  else {
            return false;
        }
        return false;
    }
}
