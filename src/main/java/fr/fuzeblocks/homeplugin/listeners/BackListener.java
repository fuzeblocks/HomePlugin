package fr.fuzeblocks.homeplugin.listeners;

import fr.fuzeblocks.homeplugin.back.BackManager;
import fr.fuzeblocks.homeplugin.event.OnHomeTeleportEvent;
import fr.fuzeblocks.homeplugin.event.OnRtpEvent;
import fr.fuzeblocks.homeplugin.event.OnSpawnTeleportEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * The type Back listener.
 */
public class BackListener implements Listener {

    private final BackManager backManager = BackManager.getInstance();

    /**
     * Instantiates a new Back listener.
     *
     * @param backManager the back manager
     */


    /**
     * On player teleport.
     *
     * @param event the event
     */
// Track the "from" location for any successful vanilla teleport
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        backManager.setLastLocation(player, event.getFrom());
    }

    /**
     * On player death.
     *
     * @param event the event
     */
// Track the death location so /back returns to where they died
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        backManager.setLastLocation(player, player.getLocation());
    }

    /**
     * On player teleport home.
     *
     * @param event the event
     */
// Track origin for custom Home teleports
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleportHome(OnHomeTeleportEvent event) {
        Player player = event.getPlayer();
        backManager.setLastLocation(player, event.getFrom());
    }

    /**
     * On player rtp.
     *
     * @param event the event
     */
// Track origin for custom RTP teleports
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerRtp(OnRtpEvent event) {
        Player player = event.getPlayer();
        backManager.setLastLocation(player, event.getFrom());
    }

    /**
     * On player spawn teleport.
     *
     * @param event the event
     */
// Track origin for custom Spawn teleports
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerSpawnTeleport(OnSpawnTeleportEvent event) {
        Player player = event.getPlayer();
        backManager.setLastLocation(player, event.getFrom());
    }
}