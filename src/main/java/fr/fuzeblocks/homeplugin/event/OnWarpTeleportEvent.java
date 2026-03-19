package fr.fuzeblocks.homeplugin.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class OnWarpTeleportEvent extends OnEventAction {
    public OnWarpTeleportEvent(Player player, Location location, String homeName) {
        super(player, location, homeName);
    }
}
