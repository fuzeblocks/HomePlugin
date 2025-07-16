package fr.fuzeblocks.homeplugin.tpa;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TpaRequest {
    public final Player sender;
    public final Player target;
    public final BukkitRunnable timeoutTask;

    public TpaRequest(Player sender, Player target, BukkitRunnable timeoutTask) {
        this.sender = sender;
        this.target = target;
        this.timeoutTask = timeoutTask;
    }
}
