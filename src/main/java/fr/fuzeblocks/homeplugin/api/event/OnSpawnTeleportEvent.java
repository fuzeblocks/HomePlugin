package fr.fuzeblocks.homeplugin.api.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class OnSpawnTeleportEvent extends OnEventAction implements Cancellable {
    private boolean cancelled = false;

    public OnSpawnTeleportEvent(Player player, Location spawnLocation) {
        super(player, spawnLocation);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

}
