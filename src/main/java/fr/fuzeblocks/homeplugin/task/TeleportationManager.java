package fr.fuzeblocks.homeplugin.task;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class TeleportationManager {

    public static void teleportPlayerToHome(Player player, String homeName) {
        sendHomeTeleportMessage(player,homeName);

        Location location = HomePlugin.getHomeManager().getHomeLocation(player, homeName);

        TaskManager taskManager = new TaskManager();
        taskManager.homeTask(homeName, player, location);
        taskManager.startTeleportTask();
        StatusManager.setPlayerStatus(player, true);

        setTaskManagerInstance(player, taskManager);
    }

    public static void teleportPlayerToSpawn(Player player) {
        sendSpawnTeleportMessage(player);
        TaskManager taskManager = new TaskManager();
        taskManager.spawnTask(player);
        taskManager.startTeleportTask();
        setTaskManagerInstance(player, taskManager);
        StatusManager.setPlayerStatus(player, true);
    }

    private static void sendSpawnTeleportMessage(Player player) {
        player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor( "Language.Start-of-teleportation-for-spawn"));
    }

    private static void sendHomeTeleportMessage(Player player, String homeName) {
        player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor("Language.Start-of-teleportation") + " " + homeName);
    }

    private static void setTaskManagerInstance(Player player, TaskManager taskManager) {
       TaskSaveUtils.setTaskManagerInstance(player, taskManager);
    }
}
