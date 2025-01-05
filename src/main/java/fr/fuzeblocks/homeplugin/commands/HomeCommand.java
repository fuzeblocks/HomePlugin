package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import fr.fuzeblocks.homeplugin.task.TaskManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

import static fr.fuzeblocks.homeplugin.task.TaskSaveUtils.setTaskManagerInstance;


public class HomeCommand implements CommandExecutor {
    private static TaskManager taskManager;
    private final HomePlugin instance;
    private final String key = "Language.";

    public HomeCommand(HomePlugin instance) {
        this.instance = instance;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
        sender.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Only-a-player-can-execute")));
    }
        Player player = (Player) sender;
        if (args.length != 1) {
        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Home.Home-usage-message")));
        }
            String homeName = args[0];
            HomeManager homeManager = HomePlugin.getHomeManager();
        if (!homeManager.isStatus(player)) {
            if (homeManager.getHomeNumber(player) <= 0) {
                player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Have-no-home")));
            }
            if (!verifyInCache(homeManager, player, homeName)) {
                Location homeLocation = homeManager.getHomeLocation(player, homeName);
                if (homeLocation == null) {
                    player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Home-does-not-exist")));
                }
                    homeManager.getCacheManager().addHomeInCache(player, homeName, homeLocation);
                    setPlayerTeleportation(player, homeName, homeLocation);
                    return true;

            } else {
                setPlayerTeleportation(player, homeName, homeManager.getCacheManager().getHomesInCache(player).get(homeName));
                return true;
            }
        } else {
            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "A-teleport-is-already-in-progress")));
            return true;
        }
    }

    private void setPlayerTeleportation(Player player, String homeName, Location location) {
        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Start-of-teleportation")) + " " + homeName);
        StatusManager.setPlayerStatus(player, true);
        taskManager = new TaskManager(instance);
        taskManager.homeTask(homeName, player, location);
        taskManager.startTeleportTask();
        setTaskManagerInstance(player, taskManager);
    }

    private boolean verifyInCache(HomeManager homeManager, Player player, String homeName) {
        if (homeManager.getCacheManager().getHomesInCache(player) != null) {
            Map<String, Location> homes = homeManager.getCacheManager().getHomesInCache(player);
            return homes.containsKey(homeName);
        } else {
            return false;
        }
    }

}