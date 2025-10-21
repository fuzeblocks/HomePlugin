package fr.fuzeblocks.homeplugin.tpa;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

/**
 * The type Tpa manager.
 */
public class TpaManager {

    private static final HashMap<UUID, BukkitRunnable> timeoutTasks = new HashMap<>();
    private static final int TIMEOUT_SECONDS = HomePlugin.getConfigurationSection()
            .getInt("Config.Tpa.Tpa-duration", 30);

    /**
     * Send tpa request.
     *
     * @param sender the sender
     * @param target the target
     */
    public static void sendTpaRequest(Player sender, Player target) {
        CacheManager cacheManager = CacheManager.getInstance();
        UUID senderId = sender.getUniqueId();
        UUID targetId = target.getUniqueId();

        if (cacheManager.getTargetWithSender(senderId) != null) {
            sender.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                    "TpaCommand.Tpa-request-exists",
                    "&eUne demande de téléportation existe déjà. Veuillez attendre qu'elle expire ou qu'elle soit acceptée/déclinée."
            ));
            return;
        }

        sender.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                "TpaCommand.Tpa-request-sent",
                "&aDemande de téléportation envoyée à %player%"
        ).replace("%player%", target.getName()));

        target.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                "TpaCommand.Tpa-request-received",
                "&e%player% vous a demandé de vous téléporter. Tapez &6/tpaccept&e pour accepter."
        ).replace("%player%", sender.getName()));

        cacheManager.addTpaRequest(senderId, targetId);


        BukkitRunnable timeoutTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (cacheManager.hasTpaRequest(senderId, targetId)) {
                    cacheManager.removeTpaRequest(senderId, targetId);

                    sender.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                            "TpaCommand.Tpa-request-expired",
                            "&cLa demande de téléportation a expiré."
                    ));

                    target.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                            "TpaCommand.Tpa-request-expired",
                            "&cLa demande de téléportation de %player% a expiré."
                    ).replace("%player%", sender.getName()));

                    timeoutTasks.remove(senderId);
                }
            }
        };

        timeoutTask.runTaskLaterAsynchronously(
                HomePlugin.getPlugin(HomePlugin.class),
                20L * TIMEOUT_SECONDS
        );
        timeoutTasks.put(senderId, timeoutTask);
    }

    /**
     * Cancel request.
     *
     * @param senderId the sender id
     */
    public static void cancelRequest(UUID senderId) {
        CacheManager cacheManager = CacheManager.getInstance();
        UUID targetId = cacheManager.getTargetWithSender(senderId);
        if (targetId != null) {
            cacheManager.removeTpaRequest(senderId, targetId);
        }
        BukkitRunnable task = timeoutTasks.remove(senderId);
        if (task != null) {
            task.cancel();
        }
    }

    /**
     * Gets request.
     *
     * @param targetId the target id
     * @return the request
     */
    public static TpaRequest getRequest(UUID targetId) {
        CacheManager cacheManager = CacheManager.getInstance();
        UUID senderId = cacheManager.getSenderForTarget(targetId);
        if (senderId != null) {
            Player sender = Bukkit.getPlayer(senderId);
            Player target = Bukkit.getPlayer(targetId);
            if (sender != null && target != null) {
                return new TpaRequest(sender, target, timeoutTasks.get(senderId));
            }
        }
        return null;
    }
}
