package fr.fuzeblocks.homeplugin.api.event;

import fr.fuzeblocks.homeplugin.task.Task;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class OnTeleportTaskCancelledEvent extends OnEventAction {
    private final Task task;

    public OnTeleportTaskCancelledEvent(Player player, Location homelocation, String homeName, Task task) {
        super(player, homelocation);
        this.task = task;
    }
    public Task getTask() {
        return task;
    }
}
