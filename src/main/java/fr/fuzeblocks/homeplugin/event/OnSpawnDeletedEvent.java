package fr.fuzeblocks.homeplugin.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class OnSpawnDeletedEvent extends OnEventAction {

    public OnSpawnDeletedEvent(Player player, Location location) {
        super(player, location);
    }
}
