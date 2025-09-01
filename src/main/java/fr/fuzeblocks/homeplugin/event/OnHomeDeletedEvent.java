package fr.fuzeblocks.homeplugin.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class OnHomeDeletedEvent extends OnEventAction {

    public OnHomeDeletedEvent(Player player, Location location, String homeName) {
        super(player, location,homeName);
    }
}
