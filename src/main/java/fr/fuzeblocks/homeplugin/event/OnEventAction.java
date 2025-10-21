package fr.fuzeblocks.homeplugin.event;

import fr.fuzeblocks.homeplugin.sync.SyncMethod;
import fr.fuzeblocks.homeplugin.tpa.TpaRequest;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * The type On event action.
 */
public class OnEventAction extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private String homeName;
    private final Player player;
    private final Location location;
    private final SyncMethod type;
    private boolean isCancelled = false;

    /**
     * Instantiates a new On event action.
     *
     * @param player   the player
     * @param location the location
     * @param type     the type
     */
    public OnEventAction(Player player, Location location, SyncMethod type) {
        this.player = player;
        this.location = location;
        this.type = type;
    }

    /**
     * Instantiates a new On event action.
     *
     * @param player   the player
     * @param location the location
     */
    public OnEventAction(Player player, Location location) {
        this.player = player;
        this.location = location;
        type = null;
    }

    /**
     * Instantiates a new On event action.
     *
     * @param player   the player
     * @param location the location
     * @param type     the type
     * @param homeName the home name
     */
    public OnEventAction(Player player, Location location, SyncMethod type, String homeName) {
        this.player = player;
        this.location = location;
        this.type = type;
        this.homeName = homeName;
    }

    /**
     * Instantiates a new On event action.
     *
     * @param player   the player
     * @param location the location
     * @param homeName the home name
     */
    public OnEventAction(Player player, Location location, String homeName) {
        this.player = player;
        this.location = location;
        this.homeName = homeName;
        type = null;
    }

    /**
     * Instantiates a new On event action.
     *
     * @param tpaRequest the tpa request
     */
    public OnEventAction(TpaRequest tpaRequest) {
        this.player = tpaRequest.sender;
        this.location = tpaRequest.target.getLocation();
        this.type = null;
    }

    /**
     * Gets handler list.
     *
     * @return the handler list
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
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

    /**
     * Gets home name.
     *
     * @return the home name
     */
    public String getHomeName() {
        return homeName;
    }

    /**
     * Sets home name.
     *
     * @param homeName the home name
     */
    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }
}
