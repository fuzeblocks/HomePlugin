package fr.fuzeblocks.homeplugin.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * The type On spawn teleport event.
 * Destination is provided to the parent via 'to'. Origin comes from 'from'.
 */
public class OnSpawnTeleportEvent extends OnEventAction {

    /**
     * Instantiates a new On spawn teleport event.
     *
     * @param player the player
     * @param to     the to
     */
// Backward-compatible: infer 'from'
    public OnSpawnTeleportEvent(Player player, Location to) {
        super(player, player != null ? player.getLocation() : null, to);
    }

    /**
     * Instantiates a new On spawn teleport event.
     *
     * @param player the player
     * @param from   the from
     * @param to     the to
     */
// Preferred: explicit 'from' and 'to'
    public OnSpawnTeleportEvent(Player player, Location from, Location to) {
        super(player, from, to);
    }
}