package fr.fuzeblocks.homeplugin.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * The type On home deleted event.
 * Represents deletion of a home at a specific location.
 */
public class OnHomeDeletedEvent extends OnEventAction {

    /**
     * Instantiates a new On home deleted event.
     *
     * @param player   the player
     * @param location the location
     * @param homeName the home name
     */
// Backward-compatible: treat 'location' as the affected home location and infer 'from'
    public OnHomeDeletedEvent(Player player, Location location, String homeName) {
        super(player, player != null ? player.getLocation() : null, location, homeName);
    }
}