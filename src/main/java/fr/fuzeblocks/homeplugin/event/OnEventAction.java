package fr.fuzeblocks.homeplugin.event;

import fr.fuzeblocks.homeplugin.sync.SyncMethod;
import fr.fuzeblocks.homeplugin.tpa.TpaRequest;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Base custom event for teleport-like actions.
 * Provides explicit origin (from) and destination (to).
 * getLocation() is retained for backward compatibility and returns the destination (to).
 */
public class OnEventAction extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Location from; // origin
    private final Location to;   // destination

    private final SyncMethod type; // may be null
    private String homeName;       // may be null
    private boolean isCancelled = false;

    // -----------------------
    // Preferred constructors
    // -----------------------

    /**
     * Preferred constructor with explicit origin and destination and optional type.
     *
     * @param player the player
     * @param from   the from
     * @param to     the to
     * @param type   the type
     */
    public OnEventAction(Player player, Location from, Location to, SyncMethod type) {
        this.player = player;
        this.from = from != null ? from.clone() : null;
        this.to = to != null ? to.clone() : null;
        this.type = type;
    }

    /**
     * Preferred constructor with explicit origin and destination, optional type, and home name.
     *
     * @param player   the player
     * @param from     the from
     * @param to       the to
     * @param type     the type
     * @param homeName the home name
     */
    public OnEventAction(Player player, Location from, Location to, SyncMethod type, String homeName) {
        this.player = player;
        this.from = from != null ? from.clone() : null;
        this.to = to != null ? to.clone() : null;
        this.type = type;
        this.homeName = homeName;
    }

    /**
     * Preferred constructor with explicit origin and destination (no type).
     *
     * @param player the player
     * @param from   the from
     * @param to     the to
     */
    public OnEventAction(Player player, Location from, Location to) {
        this.player = player;
        this.from = from != null ? from.clone() : null;
        this.to = to != null ? to.clone() : null;
        this.type = null;
    }

    /**
     * Preferred constructor with explicit origin and destination and home name (no type).
     *
     * @param player   the player
     * @param from     the from
     * @param to       the to
     * @param homeName the home name
     */
    public OnEventAction(Player player, Location from, Location to, String homeName) {
        this.player = player;
        this.from = from != null ? from.clone() : null;
        this.to = to != null ? to.clone() : null;
        this.type = null;
        this.homeName = homeName;
    }

    // ---------------------------------
    // Backward-compatible constructors
    // ---------------------------------

    /**
     * Backward-compatible: 'location' is treated as the destination ("to").
     * 'from' defaults to the player's current location at event creation time.
     *
     * @param player   the player
     * @param location the location
     * @param type     the type
     */
    public OnEventAction(Player player, Location location, SyncMethod type) {
        this.player = player;
        this.from = player != null ? player.getLocation().clone() : null;
        this.to = location != null ? location.clone() : null;
        this.type = type;
    }

    /**
     * Backward-compatible: 'location' is treated as the destination ("to").
     * 'from' defaults to the player's current location at event creation time.
     *
     * @param player   the player
     * @param location the location
     */
    public OnEventAction(Player player, Location location) {
        this.player = player;
        this.from = player != null ? player.getLocation().clone() : null;
        this.to = location != null ? location.clone() : null;
        this.type = null;
    }

    /**
     * Backward-compatible: 'location' is treated as the destination ("to").
     * 'from' defaults to the player's current location at event creation time.
     *
     * @param player   the player
     * @param location the location
     * @param type     the type
     * @param homeName the home name
     */
    public OnEventAction(Player player, Location location, SyncMethod type, String homeName) {
        this.player = player;
        this.from = player != null ? player.getLocation().clone() : null;
        this.to = location != null ? location.clone() : null;
        this.type = type;
        this.homeName = homeName;
    }

    /**
     * Backward-compatible: 'location' is treated as the destination ("to").
     * 'from' defaults to the player's current location at event creation time.
     *
     * @param player   the player
     * @param location the location
     * @param homeName the home name
     */
    public OnEventAction(Player player, Location location, String homeName) {
        this.player = player;
        this.from = player != null ? player.getLocation().clone() : null;
        this.to = location != null ? location.clone() : null;
        this.type = null;
        this.homeName = homeName;
    }

    /**
     * Convenience constructor for TPA events.
     * Sets 'from' to the sender's current location and 'to' to the target's current location.
     *
     * @param tpaRequest the tpa request
     */
    public OnEventAction(TpaRequest tpaRequest) {
        this.player = tpaRequest.sender;
        this.from = tpaRequest.sender != null ? tpaRequest.sender.getLocation().clone() : null;
        this.to = (tpaRequest.target != null && tpaRequest.target.getLocation() != null)
                ? tpaRequest.target.getLocation().clone()
                : null;
        this.type = null;
    }

    // -----------------------
    // Bukkit/Event plumbing
    // -----------------------

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

    // -------------
    // Accessors
    // -------------

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Origin location (where the player came FROM).
     *
     * @return the from
     */
    public Location getFrom() {
        return from != null ? from.clone() : null;
    }

    /**
     * Destination location (where the player is going TO).
     *
     * @return the to
     */
    public Location getTo() {
        return to != null ? to.clone() : null;
    }

    /**
     * Backward-compatible getter for the destination.
     * Prefer getTo() in new code.
     *
     * @return the location
     */
    @Deprecated
    public Location getLocation() {
        return getTo();
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