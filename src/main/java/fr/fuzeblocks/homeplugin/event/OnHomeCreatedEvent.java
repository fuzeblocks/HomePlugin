package fr.fuzeblocks.homeplugin.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * The type On home created event.
 * Represents creation of a home at a specific location.
 */
public class OnHomeCreatedEvent extends OnEventAction {

    /**
     * Instantiates a new On home created event.
     *
     * @param player   the player
     * @param location the location
     * @param homeName the home name
     */
// Backward-compatible: treat 'location' as destination and infer 'from'
    public OnHomeCreatedEvent(Player player, Location location, String homeName) {
        super(player, player != null ? player.getLocation() : null, location, homeName);
    }
}