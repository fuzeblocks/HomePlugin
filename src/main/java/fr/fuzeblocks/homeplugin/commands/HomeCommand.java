package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.sql.HomeSQLManager;
import fr.fuzeblocks.homeplugin.home.yml.HomeYMLManager;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import fr.fuzeblocks.homeplugin.sync.type.SyncMethod;
import fr.fuzeblocks.homeplugin.task.TaskManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static fr.fuzeblocks.homeplugin.task.TaskSaveUtils.setTaskManagerInstance;


public class HomeCommand implements CommandExecutor {
    private static TaskManager taskManager;
    private final HomePlugin instance;

    public HomeCommand(HomePlugin instance) {
        this.instance = instance;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                String homeName = args[0];
                HomeYMLManager homeYMLManager = HomePlugin.getHomeManager();
                HomeSQLManager homeSQLManager = HomePlugin.getHomeSQLManager();
                if (HomePlugin.getRegistrationType().equals(SyncMethod.MYSQL)) {
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
                            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Home-does-not-exist")));
                        }
                    } else {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString( "Have-no-home")));
                    }
                } else {
                    if (homeYMLManager.isStatus(player)) {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("A-teleport-is-already-in-progress")));
                        return false;
                    }
                    if (homeYMLManager.getHomeNumber(player) > 0) {
                        if (verifyInCache(homeYMLManager, player, homeName)) {
                            setPlayerTeleportation(player, homeName, homeYMLManager.getCacheManager().getHomesInCache(player).get(homeName));
                            return true;
                        }
                        Location homeLocation = homeYMLManager.getHomeLocation(player, homeName);
                        if (homeLocation != null) {
                            homeYMLManager.getCacheManager().addHomeInCache(player, homeName, homeLocation);
                            setPlayerTeleportation(player, homeName, homeLocation);
                            return true;
                        } else {
                            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Home-does-not-exist")));
                        }
                    } else {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Have-no-home")));
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

    private void setPlayerTeleportation(Player player, String homeName, Location location) {
        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Start-of-teleportation")) + " " + homeName);
        StatusManager.setPlayerStatus(player, true);
        taskManager = new TaskManager(instance);
        taskManager.homeTask(homeName, player, location);
        taskManager.startTeleportTask();
        setTaskManagerInstance(player, taskManager);
    }

    private boolean verifyInCache(HomeYMLManager homeYMLManager, Player player, String homeName) {
        if (homeYMLManager.getCacheManager().getHomesInCache(player) != null) {
            HashMap<String, Location> homes = homeYMLManager.getCacheManager().getHomesInCache(player);
            return homes.containsKey(homeName);
        } else {
            return false;
        }
    }

    private boolean verifyInCache(HomeSQLManager homeSQLManager, Player player, String homeName) {
        if (homeSQLManager.getCacheManager().getHomesInCache(player) != null) {
            HashMap<String, Location> homes = homeSQLManager.getCacheManager().getHomesInCache(player);
            return homes.containsKey(homeName);
        } else {
            return false;
        }
    }
}
