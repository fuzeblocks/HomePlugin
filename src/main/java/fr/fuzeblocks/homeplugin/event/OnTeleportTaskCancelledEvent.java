package fr.fuzeblocks.homeplugin.event;

import fr.fuzeblocks.homeplugin.task.Task;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * The type On teleport task cancelled event.
 */
public class OnTeleportTaskCancelledEvent extends OnEventAction {
    private final Task task;

    /**
     * Instantiates a new On teleport task cancelled event.
     *
     * @param player       the player
     * @param homelocation the homelocation
     * @param homeName     the home name
     * @param task         the task
     */
    public OnTeleportTaskCancelledEvent(Player player, Location homelocation, String homeName, Task task) {
        super(player, homelocation, homeName);
        this.task = task;
    }

    /**
     * Gets task.
     *
     * @return the task
     */
    public Task getTask() {
        return task;
    }
}
