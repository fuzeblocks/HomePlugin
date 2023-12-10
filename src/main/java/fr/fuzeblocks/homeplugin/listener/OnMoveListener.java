package fr.fuzeblocks.homeplugin.listener;

import fr.fuzeblocks.homeplugin.status.StatusManager;
import fr.fuzeblocks.homeplugin.task.CancelTask;
import fr.fuzeblocks.homeplugin.task.Task;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static fr.fuzeblocks.homeplugin.task.TaskSaveUtils.getTaskManagerInstance;

public class OnMoveListener implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (StatusManager.getPlayerStatus(player)) {
                if (getTaskManagerInstance(player).getTask().equals(Task.Spawn)) {
                    CancelTask.cancelTeleportTask(getTaskManagerInstance(player));
                }
            }
        if (StatusManager.getPlayerStatus(player)) {
                if (getTaskManagerInstance(player).getTask().equals(Task.Home)) {
                    CancelTask.cancelTeleportTask(getTaskManagerInstance(player));
                }
            }
        }
    }

