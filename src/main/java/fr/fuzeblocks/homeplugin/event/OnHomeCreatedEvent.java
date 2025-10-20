package fr.fuzeblocks.homeplugin.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;


/**
 * The type On home created event.
 */
public class OnHomeCreatedEvent extends OnEventAction {
    /**
     * Instantiates a new On home created event.
     *
     * @param player   the player
     * @param location the location
     * @param homeName the home name
     */
    public OnHomeCreatedEvent(Player player, Location location, String homeName) {
        super(player, location, homeName);
    }
}
