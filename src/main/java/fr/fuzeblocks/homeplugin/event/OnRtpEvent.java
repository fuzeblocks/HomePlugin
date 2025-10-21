package fr.fuzeblocks.homeplugin.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * The type On rtp event.
 */
public class OnRtpEvent extends OnEventAction {
    /**
     * Instantiates a new On rtp event.
     *
     * @param player   the player
     * @param location the location
     */
    public OnRtpEvent(Player player, Location location) {
        super(player, location);
    }
}
