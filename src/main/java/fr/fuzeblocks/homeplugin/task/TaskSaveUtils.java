package fr.fuzeblocks.homeplugin.task;

import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

public class TaskSaveUtils {

    private static final ConcurrentHashMap<Player, TaskManager> taskManagers = new ConcurrentHashMap<>();

    private TaskSaveUtils() {

    }

    public static TaskManager getTaskManagerInstance(Player player) {
        return taskManagers.get(player);
    }

    public static void setTaskManagerInstance(Player player, TaskManager taskManager) {
        taskManagers.put(player, taskManager);
    }

    public static void removeTaskManagerInstance(Player player) {
        taskManagers.remove(player);
    }

    public static boolean hasTaskManager(Player player) {
        return taskManagers.containsKey(player);
    }
}
