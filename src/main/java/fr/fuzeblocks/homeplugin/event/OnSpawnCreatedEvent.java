package fr.fuzeblocks.homeplugin.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class OnSpawnCreatedEvent extends OnEventAction {
    public OnSpawnCreatedEvent(Player player, Location location) {
        super(player, location);
    }
}
