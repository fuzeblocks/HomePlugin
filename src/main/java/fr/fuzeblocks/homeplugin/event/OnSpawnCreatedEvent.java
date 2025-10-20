package fr.fuzeblocks.homeplugin.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * The type On spawn created event.
 */
public class OnSpawnCreatedEvent extends OnEventAction {
    /**
     * Instantiates a new On spawn created event.
     *
     * @param player   the player
     * @param location the location
     */
    public OnSpawnCreatedEvent(Player player, Location location) {
        super(player, location);
    }
}
