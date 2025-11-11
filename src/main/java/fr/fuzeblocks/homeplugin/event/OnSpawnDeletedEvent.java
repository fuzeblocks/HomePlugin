package fr.fuzeblocks.homeplugin.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * The type On spawn deleted event.
 * Represents deletion of a spawn at a specific location.
 */
public class OnSpawnDeletedEvent extends OnEventAction {

    /**
     * Instantiates a new On spawn deleted event.
     *
     * @param player   the player
     * @param location the location
     */
// Backward-compatible: treat 'location' as the affected spawn location and infer 'from'
    public OnSpawnDeletedEvent(Player player, Location location) {
        super(player, player != null ? player.getLocation() : null, location);
    }

}