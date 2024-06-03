package fr.fuzeblocks.homeplugin.api.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class OnHomeTeleportEvent extends OnEventAction {

    public OnHomeTeleportEvent(Player player, Location homeLocation) {
        super(player, homeLocation);
    }
}
