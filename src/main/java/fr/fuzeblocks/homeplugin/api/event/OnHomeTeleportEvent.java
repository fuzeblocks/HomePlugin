package fr.fuzeblocks.homeplugin.api.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class OnHomeTeleportEvent extends OnEventAction implements Cancellable {
    private boolean cancelled = false;

    public OnHomeTeleportEvent(Player player, Location homeLocation) {
        super(player, homeLocation);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
