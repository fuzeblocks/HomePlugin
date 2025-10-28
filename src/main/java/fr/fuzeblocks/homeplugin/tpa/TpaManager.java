package fr.fuzeblocks.homeplugin.tpa;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

/**
 * Manager for handling TPA (teleport request) system.
 */
public class TpaManager {

    private static final HashMap<UUID, BukkitRunnable> timeoutTasks = new HashMap<>();
    private static final int TIMEOUT_SECONDS = HomePlugin.getConfigurationSection()
            .getInt("Config.Tpa.Tpa-duration", 30);
    private static final String TPA = "TpaCommand.";

    /**
     * Sends a TPA request from sender to target.
     *
     * @param sender The player sending the request
     * @param target The player receiving the request
     * @return true if request was sent successfully, false otherwise
     */
    public static boolean sendTpaRequest(Player sender, Player target) {
        if (sender == null || target == null || !sender.isOnline() || !target.isOnline()) {
            return false;
        }

        CacheManager cacheManager = CacheManager.getInstance();
        if (cacheManager == null) {
            return false;
        }

        UUID senderId = sender.getUniqueId();
        UUID targetId = target.getUniqueId();
        LanguageManager languageManager = HomePlugin.getLanguageManager();

        if (cacheManager.getTargetWithSender(senderId) != null) {
            sender.sendMessage(languageManager.getStringWithColor(TPA + "Tpa-request-exists"));
            return false;
        }

        sender.sendMessage(languageManager.getStringWithColor(TPA + "Tpa-request-sent")
                .replace("%player%", target.getName()));

        sendRequestNotification(sender, target, languageManager);

        cacheManager.addTpaRequest(senderId, targetId);

        startTimeoutTask(sender, target, senderId, targetId, cacheManager, languageManager);

        return true;
    }

    /**
     * Sends the interactive request notification to the target player.
     *
     * @param sender The sender of the request
     * @param target The receiver of the request
     * @param languageManager The language manager instance
     */
    private static void sendRequestNotification(Player sender, Player target, LanguageManager languageManager) {
        String receivedBase = languageManager.getStringWithColor(TPA + "Tpa-request-received")
                .replace("%player%", sender.getName());
        target.sendMessage(receivedBase);

        Component prompt = Component.text("  ", NamedTextColor.DARK_GRAY);

        Component accept = Component.text("[ᴀᴄᴄᴇᴘᴛ]", NamedTextColor.GREEN, TextDecoration.BOLD)
                .clickEvent(ClickEvent.runCommand("/tpaccept " + sender.getName()))
                .hoverEvent(HoverEvent.showText(Component.text("Click to accept teleport request", NamedTextColor.GRAY)));

        Component separator = Component.text("  ", NamedTextColor.DARK_GRAY);

        Component deny = Component.text("[ᴅᴇɴʏ]", NamedTextColor.RED, TextDecoration.BOLD)
                .clickEvent(ClickEvent.runCommand("/tpdeny " + sender.getName()))
                .hoverEvent(HoverEvent.showText(Component.text("Click to deny teleport request", NamedTextColor.GRAY)));

        HomePlugin.getAdventure().player(target).sendMessage(
                prompt.append(accept).append(separator).append(deny)
        );
    }

    /**
     * Starts the timeout task for a TPA request.
     *
     * @param sender The sender of the request
     * @param target The receiver of the request
     * @param senderId The sender's UUID
     * @param targetId The target's UUID
     * @param cacheManager The cache manager instance
     * @param languageManager The language manager instance
     */
    private static void startTimeoutTask(Player sender, Player target, UUID senderId, UUID targetId,
                                         CacheManager cacheManager, LanguageManager languageManager) {
        BukkitRunnable existingTask = timeoutTasks.get(senderId);
        if (existingTask != null) {
            existingTask.cancel();
        }

        BukkitRunnable timeoutTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (cacheManager.hasTpaRequest(senderId, targetId)) {
                    cacheManager.removeTpaRequest(senderId, targetId);

                    if (sender.isOnline()) {
                        sender.sendMessage(languageManager.getStringWithColor(TPA + "Tpa-request-expired"));
                    }

                    if (target.isOnline()) {
                        target.sendMessage(languageManager.getStringWithColor(TPA + "Tpa-request-expired-target")
                                .replace("%player%", sender.getName()));
                    }

                    timeoutTasks.remove(senderId);
                }
            }
        };

        timeoutTask.runTaskLater(
                HomePlugin.getPlugin(HomePlugin.class),
                20L * TIMEOUT_SECONDS
        );
        timeoutTasks.put(senderId, timeoutTask);
    }

    /**
     * Cancels an active TPA request.
     *
     * @param senderId The UUID of the sender
     */
    public static void cancelRequest(UUID senderId) {
        if (senderId == null) {
            return;
        }

        CacheManager cacheManager = CacheManager.getInstance();
        if (cacheManager != null) {
            UUID targetId = cacheManager.getTargetWithSender(senderId);
            if (targetId != null) {
                cacheManager.removeTpaRequest(senderId, targetId);
            }
        }

        BukkitRunnable task = timeoutTasks.remove(senderId);
        if (task != null) {
            task.cancel();
        }
    }

    /**
     * Gets an active TPA request for a target player.
     *
     * @param targetId The UUID of the target player
     * @return The TpaRequest if found, null otherwise
     */
    public static TpaRequest getRequest(UUID targetId) {
        if (targetId == null) {
            return null;
        }

        CacheManager cacheManager = CacheManager.getInstance();
        if (cacheManager == null) {
            return null;
        }

        UUID senderId = cacheManager.getSenderForTarget(targetId);
        if (senderId == null) {
            return null;
        }

        Player sender = Bukkit.getPlayer(senderId);
        Player target = Bukkit.getPlayer(targetId);

        if (sender != null && target != null && sender.isOnline() && target.isOnline()) {
            return new TpaRequest(sender, target, timeoutTasks.get(senderId));
        }

        return null;
    }

    /**
     * Checks if a player has an active request.
     *
     * @param targetId The UUID of the target player
     * @return true if there's an active request, false otherwise
     */
    public static boolean hasRequest(UUID targetId) {
        if (targetId == null) {
            return false;
        }

        CacheManager cacheManager = CacheManager.getInstance();
        if (cacheManager == null) {
            return false;
        }

        return cacheManager.getSenderForTarget(targetId) != null;
    }
}