package fr.fuzeblocks.homeplugin.api.event;

import fr.fuzeblocks.homeplugin.sync.SyncMethod;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class OnHomeCreatedEvent extends OnEventAction {
    public OnHomeCreatedEvent(Player player, Location location, SyncMethod type, String homeName) {
        super(player, location, type,homeName);
    }
}
