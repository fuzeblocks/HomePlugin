package fr.fuzeblocks.homeplugin.api.event;

import fr.fuzeblocks.homeplugin.sync.type.SyncMethod;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OnEventAction extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Location location;
    private SyncMethod type;

    public OnEventAction(Player player, Location location, SyncMethod type) {
        this.player = player;
        this.location = location;
        this.type = type;
    }

    public OnEventAction(Player player, Location location) {
        this.player = player;
        this.location = location;
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
}
