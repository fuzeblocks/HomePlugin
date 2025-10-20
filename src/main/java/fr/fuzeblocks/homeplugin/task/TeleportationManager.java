package fr.fuzeblocks.homeplugin.task;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;


/**
 * The type Teleportation manager.
 */
public class TeleportationManager {

    /**
     * Teleport player to home.
     *
     * @param player   the player
     * @param homeName the home name
     */
    public static void teleportPlayerToHome(Player player, String homeName) {
        sendHomeTeleportMessage(player, homeName);

        Location location = HomePlugin.getHomeManager().getHomeLocation(player, homeName);

        TaskManager taskManager = new TaskManager();
        taskManager.homeTask(homeName, player, location);
        taskManager.startTeleportTask();
        StatusManager.setPlayerStatus(player, true);

        setTaskManagerInstance(player, taskManager);
    }

    /**
     * Teleport player to spawn.
     *
     * @param player the player
     */
    public static void teleportPlayerToSpawn(Player player) {
        sendSpawnTeleportMessage(player);
        TaskManager taskManager = new TaskManager();
        taskManager.spawnTask(player);
        taskManager.startTeleportTask();
        setTaskManagerInstance(player, taskManager);
        StatusManager.setPlayerStatus(player, true);
    }

    private static void sendSpawnTeleportMessage(Player player) {
        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Language.Start-of-teleportation-for-spawn")));
    }

    private static void sendHomeTeleportMessage(Player player, String homeName) {
        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Language.Start-of-teleportation")) + " " + homeName);
    }

    private static void setTaskManagerInstance(Player player, TaskManager taskManager) {
        TaskSaveUtils.setTaskManagerInstance(player, taskManager);
    }
}
