package fr.fuzeblocks.homeplugin.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class OnHomeTeleportEvent extends OnEventAction {
    public OnHomeTeleportEvent(Player player, Location location, String homeName) {
        super(player, location, homeName);
    }
}
