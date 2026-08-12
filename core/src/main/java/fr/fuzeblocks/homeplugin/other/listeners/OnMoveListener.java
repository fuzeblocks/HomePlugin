package fr.fuzeblocks.homeplugin.other.listeners;

import fr.fuzeblocks.homeplugin.core.status.StatusManager;
import fr.fuzeblocks.homeplugin.core.task.CancelTask;
import fr.fuzeblocks.homeplugin.core.task.Task;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static fr.fuzeblocks.homeplugin.core.task.TaskSaveUtils.getTaskManagerInstance;

/**
 * The type On move listener.
 */
public class OnMoveListener implements Listener {
    /**
     * On move.
     *
     * @param event the event
     */
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (event.getFrom().getYaw() != event.getTo().getYaw() || event.getFrom().getPitch() != event.getTo().getPitch()) {
            return;
        }
        if (StatusManager.getPlayerStatus(player)) {
            if (getTaskManagerInstance(player).getTask().equals(Task.SPAWN)) {
                CancelTask.cancelTeleportTask(getTaskManagerInstance(player));
            }
        }
        if (StatusManager.getPlayerStatus(player)) {
            if (getTaskManagerInstance(player).getTask().equals(Task.HOME)) {
                CancelTask.cancelTeleportTask(getTaskManagerInstance(player));
            }
        }
    }
}

