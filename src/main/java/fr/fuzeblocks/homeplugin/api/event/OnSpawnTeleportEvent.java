package fr.fuzeblocks.homeplugin.api.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class OnSpawnTeleportEvent extends OnEventAction  {
    public OnSpawnTeleportEvent(Player player, Location spawnLocation) {
        super(player, spawnLocation);
    }
}
