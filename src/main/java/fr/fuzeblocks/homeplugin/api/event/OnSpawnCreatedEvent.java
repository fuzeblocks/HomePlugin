package fr.fuzeblocks.homeplugin.api.event;

import fr.fuzeblocks.homeplugin.sync.SyncMethod;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class OnSpawnCreatedEvent extends OnEventAction {
    public OnSpawnCreatedEvent(Player player, Location location, SyncMethod type) {
        super(player, location, type);
    }
}
