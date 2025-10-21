package fr.fuzeblocks.homeplugin.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * The type On spawn deleted event.
 */
public class OnSpawnDeletedEvent extends OnEventAction {

    /**
     * Instantiates a new On spawn deleted event.
     *
     * @param player   the player
     * @param location the location
     */
    public OnSpawnDeletedEvent(Player player, Location location) {
        super(player, location);
    }
}
