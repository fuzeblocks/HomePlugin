package fr.fuzeblocks.homeplugin.api.event;

import fr.fuzeblocks.homeplugin.sync.type.SyncMethod;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class OnSpawnCreatedEvent extends OnEventAction implements Cancellable {
    private boolean isCancelled = false;

    public OnSpawnCreatedEvent(Player player, Location location, SyncMethod type) {
        super(player, location, type);
    }


    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
    }
}
