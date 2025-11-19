package fr.fuzeblocks.homeplugin.listeners;

import fr.fuzeblocks.homeplugin.event.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type On player take damage by another player.
 */
public class OnPlayerTakeDamageByAnotherPlayer implements Listener {

    private static final long _COOLDOWN_MS = 5000; // 5 secondes
    private final Map<UUID, Long> playerDamageTimestamps = new ConcurrentHashMap<>();

    /**
     * On player damage.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        long currentTime = System.currentTimeMillis();
        playerDamageTimestamps.put(event.getEntity().getUniqueId(), currentTime);
        playerDamageTimestamps.put(event.getDamager().getUniqueId(), currentTime);
    }

    /**
     * On spawn teleport.
     *
     * @param event the event
     */
    @EventHandler
    public void onSpawnTeleport(OnSpawnTeleportEvent event) {
        checkCooldown(event);
    }

    /**
     * On home teleport.
     *
     * @param event the event
     */
    @EventHandler
    public void onHomeTeleport(OnHomeTeleportEvent event) {
        checkCooldown(event);
    }

    /**
     * On tpa accepted.
     *
     * @param event the event
     */
    @EventHandler
    public void onTpaAccepted(OnTpaAcceptedEvent event) {
        checkCooldown(event);
    }

    /**
     * On rtp event.
     *
     * @param event the event
     */
    @EventHandler
    public void onRtpEvent(OnRTPEvent event) {
        checkCooldown(event);
    }

    private void checkCooldown(OnEventAction eventAction) {
        Player player = eventAction.getPlayer();
        if (player == null) return;

        UUID uuid = player.getUniqueId();
        Long lastHit = playerDamageTimestamps.get(uuid);
        if (lastHit == null) return;

        long elapsed = System.currentTimeMillis() - lastHit;
        if (elapsed < _COOLDOWN_MS) {
            eventAction.setCancelled(true);
        } else {
            playerDamageTimestamps.remove(uuid);
        }
    }
}
