package fr.fuzeblocks.homeplugin.core.task;

import fr.fuzeblocks.homeplugin.core.event.OnTeleportTaskCancelledEvent;
import fr.fuzeblocks.homeplugin.core.task.exception.TeleportTaskException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * The type Cancel task.
 */
public class CancelTask {
    /**
     * Cancel teleport task.
     *
     * @param taskManager the task manager
     */
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

