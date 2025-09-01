package fr.fuzeblocks.homeplugin.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;


public class OnHomeCreatedEvent extends OnEventAction {
    public OnHomeCreatedEvent(Player player, Location location, String homeName) {
        super(player, location, homeName);
    }
}
