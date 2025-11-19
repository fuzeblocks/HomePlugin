package fr.fuzeblocks.homeplugin.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * The type On rtp event.
 * Destination is provided to the parent via 'to'. Origin comes from 'from'.
 */
public class OnRTPEvent extends OnEventAction {

    /**
     * Instantiates a new On rtp event.
     *
     * @param player the player
     * @param to     the to
     */
// Backward-compatible: infer 'from' from the player's current location
    public OnRTPEvent(Player player, Location to) {
        super(player, player != null ? player.getLocation() : null, to);
    }

    /**
     * Instantiates a new On rtp event.
     *
     * @param player the player
     * @param from   the from
     * @param to     the to
     */
// Preferred: explicit 'from' and 'to'
    public OnRTPEvent(Player player, Location from, Location to) {
        super(player, from, to);
    }
}