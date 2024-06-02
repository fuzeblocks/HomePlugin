package fr.fuzeblocks.homeplugin.api.event;

import fr.fuzeblocks.homeplugin.sync.type.SyncMethod;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;


public class OnHomeCreatedEvent extends OnEventAction implements Cancellable {
    private boolean isCancelled = false;
    private String homeName;


    public OnHomeCreatedEvent(Player player, Location location, SyncMethod type, String homeName) {
        super(player, location, type);
        this.homeName = homeName;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
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
