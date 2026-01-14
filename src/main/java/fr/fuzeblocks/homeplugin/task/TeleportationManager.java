package fr.fuzeblocks. homeplugin.task;

import fr.fuzeblocks. homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.language. LanguageManager;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import org.bukkit.Location;
import org.bukkit. entity.Player;

/**
 * The type Teleportation manager.
 */
public class TeleportationManager {

    private static final String HOME_PREFIX = "Home.";
    private static final String WARP_PREFIX = "Warp.";
    private static final String LANGUAGE_PREFIX = "Language.";
    private static final LanguageManager languageManager = HomePlugin.getLanguageManager();

    /**
     * Teleport player to home.
     *
     * @param player   the player
     * @param homeName the home name
     */
    public static void teleportPlayerToHome(Player player, String homeName) {
        sendHomeTeleportMessage(player, homeName);

        Location location = HomePlugin.getHomeManager().getHomeLocation(player, homeName);

        // Early return if world is not loaded
        if (location == null || location.getWorld() == null) {
            player.sendMessage(languageManager.getStringWithColor(HOME_PREFIX + "Home-world-not-loaded"));
            return;
        }

        startHomeTeleportTask(player, homeName, location);
    }

    /**
     * Teleport player to spawn.
     *
     * @param player the player
     */
    public static void teleportPlayerToSpawn(Player player) {
        sendSpawnTeleportMessage(player);
        startSpawnTeleportTask(player);
    }

    /**
     * Teleport player to warp.
     *
     * @param player   the player
     * @param warpName the warp name
     */
    public static void teleportPlayerToWarp(Player player, String warpName) {
        sendWarpTeleportMessage(player, warpName);

        Location location = HomePlugin.getWarpManager().getWarp(warpName).getLocation();

        // Early return if world is not loaded
        if (location == null || location.getWorld() == null) {
            player.sendMessage(languageManager.getStringWithColor(WARP_PREFIX + "Warp-world-not-loaded"));
            return;
        }

        startWarpTeleportTask(player, warpName, location);
    }

    /**
     * Starts a home teleport task for the player.
     *
     * @param player   the player
     * @param homeName the home name
     * @param location the target location
     */
    private static void startHomeTeleportTask(Player player, String homeName, Location location) {
        TaskManager taskManager = new TaskManager();
        taskManager.homeTask(homeName, player, location);
        taskManager.startTeleportTask();

        StatusManager.setPlayerStatus(player, true);
        TaskSaveUtils.setTaskManagerInstance(player, taskManager);
    }

    /**
     * Starts a spawn teleport task for the player.
     *
     * @param player the player
     */
    private static void startSpawnTeleportTask(Player player) {
        TaskManager taskManager = new TaskManager();
        taskManager.spawnTask(player);
        taskManager.startTeleportTask();

        StatusManager.setPlayerStatus(player, true);
        TaskSaveUtils. setTaskManagerInstance(player, taskManager);
    }

     private static void startWarpTeleportTask(Player player, String warpName, Location location) {
        TaskManager taskManager = new TaskManager();
        taskManager.warpTask(warpName,player,location);
        taskManager.startTeleportTask();

        StatusManager.setPlayerStatus(player, true);
        TaskSaveUtils.setTaskManagerInstance(player, taskManager);
    }

    /**
     * Sends spawn teleport start message to player.
     *
     * @param player the player
     */
    private static void sendSpawnTeleportMessage(Player player) {
        String message = languageManager.getStringWithColor(LANGUAGE_PREFIX + "Start-of-teleportation-for-spawn");
        player.sendMessage(message);
    }

    /**
     * Sends home teleport start message to player.
     *
     * @param player   the player
     * @param homeName the home name
     */
    private static void sendHomeTeleportMessage(Player player, String homeName) {
        String message = languageManager.getStringWithColor(LANGUAGE_PREFIX + "Start-of-teleportation") + " " + homeName;
        player.sendMessage(message);
    }
    private static void sendWarpTeleportMessage(Player player, String warpName) {
       //Todo
    }
}