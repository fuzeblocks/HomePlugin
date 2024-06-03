package fr.fuzeblocks.homeplugin.api.event;

import fr.fuzeblocks.homeplugin.sync.type.SyncMethod;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class OnHomeDeletedEvent extends OnEventAction  {

    public OnHomeDeletedEvent(Player player, Location location, SyncMethod type, String homeName) {
        super(player, location, type);
    }
}
