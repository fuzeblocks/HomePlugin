package fr.fuzeblocks.homeplugin.task;

import fr.fuzeblocks.homeplugin.task.exception.TeleportTaskException;

public interface CancelTask {
    static void cancelTeleportTask(TaskManager taskManager) {
        try {
            if (taskManager != null) {
                System.out.println("[HomePlugin] Canceling teleport task for player " + taskManager.getPlayer().getName());
                taskManager.cancelTeleportTask();
            }
        } catch (TeleportTaskException e) {
            e.printStackTrace();
        }
    }
}

