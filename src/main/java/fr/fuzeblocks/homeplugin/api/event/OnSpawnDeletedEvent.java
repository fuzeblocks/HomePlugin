package fr.fuzeblocks.homeplugin.api.event;

import fr.fuzeblocks.homeplugin.sync.SyncMethod;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class OnSpawnDeletedEvent extends OnEventAction {

    public OnSpawnDeletedEvent(Player player, Location location, SyncMethod type) {
        super(player, location, type);
    }
}
