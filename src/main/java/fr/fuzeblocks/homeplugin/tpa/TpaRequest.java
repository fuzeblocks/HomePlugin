package fr.fuzeblocks.homeplugin.tpa;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * The type Tpa request.
 */
public class TpaRequest {
    /**
     * The Sender.
     */
    public final Player sender;
    /**
     * The Target.
     */
    public final Player target;
    /**
     * The Timeout task.
     */
    public final BukkitRunnable timeoutTask;

    /**
     * Instantiates a new Tpa request.
     *
     * @param sender      the sender
     * @param target      the target
     * @param timeoutTask the timeout task
     */
    public TpaRequest(Player sender, Player target, BukkitRunnable timeoutTask) {
        this.sender = sender;
        this.target = target;
        this.timeoutTask = timeoutTask;
    }
}
