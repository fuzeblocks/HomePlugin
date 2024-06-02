package fr.fuzeblocks.homeplugin.api.event;

import fr.fuzeblocks.homeplugin.task.Task;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class OnTeleportTaskCancelledEvent extends OnEventAction implements Cancellable {
    private boolean cancelled = false;
    private final String homeName;
    private final Task task;


    public OnTeleportTaskCancelledEvent(Player player, Location homelocation, String homeName, Task task) {
        super(player, homelocation);
        this.homeName = homeName;
        this.task = task;
    }

    public Task getTask() {
        return task;
    }


    public String getHomeName() {
        return homeName;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
