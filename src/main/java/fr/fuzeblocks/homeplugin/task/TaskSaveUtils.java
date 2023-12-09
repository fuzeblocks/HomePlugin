package fr.fuzeblocks.homeplugin.task;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class TaskSaveUtils {
    private static HashMap<Player, TaskManager> taskManagerHashMap = new HashMap<>();
    public static TaskManager getTaskManagerInstance(Player player) {
        return taskManagerHashMap.get(player);
    }
    public static void setTaskManagerInstance(Player player,TaskManager taskManager) {
        if (!taskManagerHashMap.containsKey(player)) {
            taskManagerHashMap.put(player,taskManager);
        } else {
            taskManagerHashMap.remove(player);
            taskManagerHashMap.put(player, taskManager);
        }
    }
}
