package fr.fuzeblocks.homeplugin.api.event;

import fr.fuzeblocks.homeplugin.task.Task;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OnTeleportTaskCanceled extends Event implements Cancellable {
    private boolean cancelled = false;
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Location homelocation;
    private String homeName;
    private Task task;


    public OnTeleportTaskCanceled(Player player, Location homelocation, String homeName,Task task) {
        this.player = player;
        this.homelocation = homelocation;
        this.homeName = homeName;
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getHomelocation() {
        return homelocation;
    }

    public String getHomeName() {
        return homeName;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
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
