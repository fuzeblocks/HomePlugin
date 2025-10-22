package fr.fuzeblocks.homeplugin.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Fired when a player teleports to a home.
 * Destination is provided to the parent via 'to'. Origin comes from 'from'.
 */
public class OnHomeTeleportEvent extends OnEventAction {

    /**
     * Instantiates a new On home teleport event.
     *
     * @param player   the player
     * @param to       the to
     * @param homeName the home name
     */
// Backward-compatible: infer 'from' from the player's current location
    public OnHomeTeleportEvent(Player player, Location to, String homeName) {
        super(player, player != null ? player.getLocation() : null, to, homeName);
    }

    /**
     * Instantiates a new On home teleport event.
     *
     * @param player   the player
     * @param from     the from
     * @param to       the to
     * @param homeName the home name
     */
// Preferred: explicit 'from' and 'to'
    public OnHomeTeleportEvent(Player player, Location from, Location to, String homeName) {
        super(player, from, to, homeName);
    }
}