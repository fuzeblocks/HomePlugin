package fr.fuzeblocks.homeplugin.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * The type On spawn teleport event.
 */
public class OnSpawnTeleportEvent extends OnEventAction {
    /**
     * Instantiates a new On spawn teleport event.
     *
     * @param player   the player
     * @param location the location
     */
    public OnSpawnTeleportEvent(Player player, Location location) {
        super(player, location);
    }
}
