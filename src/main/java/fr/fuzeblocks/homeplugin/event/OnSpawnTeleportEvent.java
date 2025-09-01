package fr.fuzeblocks.homeplugin.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class OnSpawnTeleportEvent extends OnEventAction {
    public OnSpawnTeleportEvent(Player player, Location location) {
        super(player, location);
    }
}
