package fr.fuzeblocks.homeplugin.api.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OnHomeCreate extends Event implements Cancellable {
    private boolean isCancelled = false;
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Location location;
    private int type; //return 1 if it is in the database
    private String homeName;

    public OnHomeCreate(Player player, Location location,int type,String homeName) {
        this.player = player;
        this.location = location;
        this.type = type;
        this.homeName = homeName;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public int getType() {
        return type;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}