package fr.fuzeblocks.homeplugin.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * The type On home teleport event.
 */
public class OnHomeTeleportEvent extends OnEventAction {
    /**
     * Instantiates a new On home teleport event.
     *
     * @param player   the player
     * @param location the location
     * @param homeName the home name
     */
    public OnHomeTeleportEvent(Player player, Location location, String homeName) {
        super(player, location, homeName);
    }
}
