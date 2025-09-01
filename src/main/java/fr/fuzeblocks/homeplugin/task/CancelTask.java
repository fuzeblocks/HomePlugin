package fr.fuzeblocks.homeplugin.task;

import fr.fuzeblocks.homeplugin.event.OnTeleportTaskCancelledEvent;
import fr.fuzeblocks.homeplugin.task.exception.TeleportTaskException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CancelTask {
    public static void cancelTeleportTask(TaskManager taskManager) {
        try {
            if (taskManager != null) {
                Player player = taskManager.getPlayer();
                OnTeleportTaskCancelledEvent onTeleportTaskCancelled = new OnTeleportTaskCancelledEvent(player, taskManager.getHomeLocation(), taskManager.getHomeName(), taskManager.getTask());
                Bukkit.getPluginManager().callEvent(onTeleportTaskCancelled);
                if (!onTeleportTaskCancelled.isCancelled()) {
                    taskManager.cancelTeleportTask();
                    TaskSaveUtils.removeTaskManagerInstance(player);
                }
            }
        } catch (TeleportTaskException e) {
            e.printStackTrace();
        }
    }
}

