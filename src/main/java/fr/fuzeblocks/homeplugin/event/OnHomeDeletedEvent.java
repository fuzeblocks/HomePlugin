package fr.fuzeblocks.homeplugin.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * The type On home deleted event.
 */
public class OnHomeDeletedEvent extends OnEventAction {

    /**
     * Instantiates a new On home deleted event.
     *
     * @param player   the player
     * @param location the location
     * @param homeName the home name
     */
    public OnHomeDeletedEvent(Player player, Location location, String homeName) {
        super(player, location, homeName);
    }
}
