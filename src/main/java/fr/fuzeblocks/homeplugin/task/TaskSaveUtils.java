package fr.fuzeblocks.homeplugin.task;

import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Task save utils.
 */
public class TaskSaveUtils {

    private static final ConcurrentHashMap<Player, TaskManager> taskManagers = new ConcurrentHashMap<>();

    private TaskSaveUtils() {

    }

    /**
     * Gets task manager instance.
     *
     * @param player the player
     * @return the task manager instance
     */
    public static TaskManager getTaskManagerInstance(Player player) {
        return taskManagers.get(player);
    }

    /**
     * Sets task manager instance.
     *
     * @param player      the player
     * @param taskManager the task manager
     */
    public static void setTaskManagerInstance(Player player, TaskManager taskManager) {
        taskManagers.put(player, taskManager);
    }

    /**
     * Remove task manager instance.
     *
     * @param player the player
     */
    public static void removeTaskManagerInstance(Player player) {
        taskManagers.remove(player);
    }

    /**
     * Has task manager boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public static boolean hasTaskManager(Player player) {
        return taskManagers.containsKey(player);
    }
}
