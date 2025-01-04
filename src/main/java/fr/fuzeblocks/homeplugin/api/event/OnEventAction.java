package fr.fuzeblocks.homeplugin.api.event;

import fr.fuzeblocks.homeplugin.sync.SyncMethod;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OnEventAction extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private String homeName;
    private Player player;
    private Location location;
    private SyncMethod type;
    private boolean isCancelled = false;

    public OnEventAction(Player player, Location location, SyncMethod type) {
        this.player = player;
        this.location = location;
        this.type = type;
    }

    public OnEventAction(Player player, Location location) {
        this.player = player;
        this.location = location;
    }
    public OnEventAction(Player player, Location location,SyncMethod type,String homeName) {
        this.player = player;
        this.location = location;
        this.type = type;
        this.homeName = homeName;
    }

    public OnEventAction(Player player, Location location, String homeName) {
        this.player = player;
        this.location = location;
        this.homeName = homeName;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public SyncMethod getType() {
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
    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }
}
