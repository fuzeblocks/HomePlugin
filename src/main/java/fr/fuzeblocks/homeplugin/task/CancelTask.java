package fr.fuzeblocks.homeplugin.task;

import fr.fuzeblocks.homeplugin.api.event.OnTeleportTaskCanceled;
import fr.fuzeblocks.homeplugin.task.exception.TeleportTaskException;
import org.bukkit.Bukkit;

public class CancelTask {
   public static void cancelTeleportTask(TaskManager taskManager) {
        try {
            if (taskManager != null) {
                OnTeleportTaskCanceled onTeleportTaskCanceled = new OnTeleportTaskCanceled(taskManager.getPlayer(), taskManager.getHomeLocation(), taskManager.getHomeName(), taskManager.getTask());
                Bukkit.getPluginManager().callEvent(onTeleportTaskCanceled);
                System.out.println("[HomePlugin] Canceling teleport task for player " + taskManager.getPlayer().getName());
                if (onTeleportTaskCanceled.isCancelled()) {
                    taskManager.cancelTeleportTask();
                }
            }
        } catch (TeleportTaskException e) {
            e.printStackTrace();
        }
    }
}

