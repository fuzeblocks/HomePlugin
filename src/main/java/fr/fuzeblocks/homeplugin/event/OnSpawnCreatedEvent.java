package fr.fuzeblocks.homeplugin.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * The type On spawn created event.
 * Represents creation of a spawn at a specific location.
 */
public class OnSpawnCreatedEvent extends OnEventAction {

    /**
     * Instantiates a new On spawn created event.
     *
     * @param player   the player
     * @param location the location
     */
// Backward-compatible: treat 'location' as destination and infer 'from'
    public OnSpawnCreatedEvent(Player player, Location location) {
        super(player, player != null ? player.getLocation() : null, location);
    }

}