package fr.fuzeblocks.homeplugin.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class OnRtpEvent extends OnEventAction {
    public OnRtpEvent(Player player, Location location) {
        super(player, location);
    }
}
