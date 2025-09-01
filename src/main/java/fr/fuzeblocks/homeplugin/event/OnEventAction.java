package fr.fuzeblocks.homeplugin.event;

import fr.fuzeblocks.homeplugin.sync.SyncMethod;
import fr.fuzeblocks.homeplugin.tpa.TpaRequest;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OnEventAction extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private String homeName;
    private final Player player;
    private final Location location;
    private final SyncMethod type;
    private boolean isCancelled = false;

    public OnEventAction(Player player, Location location, SyncMethod type) {
        this.player = player;
        this.location = location;
        this.type = type;
    }

    public OnEventAction(Player player, Location location) {
        this.player = player;
        this.location = location;
        type = null;
    }

    public OnEventAction(Player player, Location location, SyncMethod type, String homeName) {
        this.player = player;
        this.location = location;
        this.type = type;
        this.homeName = homeName;
    }

    public OnEventAction(Player player, Location location, String homeName) {
        this.player = player;
        this.location = location;
        this.homeName = homeName;
        type = null;
    }
    public OnEventAction(TpaRequest tpaRequest) {
        this.player = tpaRequest.sender;
        this.location = tpaRequest.target.getLocation();
        this.type = null;
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

    public Location getLocation() {
        return location;
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
