package fr.fuzeblocks.homeplugin.tpa;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class TpaManager {

    private static final HashMap<UUID, BukkitRunnable> timeoutTasks = new HashMap<>();
    private static final int TIMEOUT_SECONDS = 30;

    public static void sendTpaRequest(Player sender, Player target) {
        CacheManager cacheManager = CacheManager.getInstance();
        UUID senderId = sender.getUniqueId();

        if (cacheManager.hasTpaRequest(senderId)) {
            sender.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                    "TpaCommand.Tpa-request-exists",
                    "&eUne demande de téléportation existe déjà. Veuillez attendre qu'elle expire ou que le joueur l'accepte/décline."
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

        cacheManager.addTpaRequest(senderId, target.getUniqueId());

        BukkitRunnable timeoutTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (cacheManager.hasTpaRequest(senderId)) {
                    cacheManager.removeTpaRequest(senderId);

                    sender.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                            "TpaCommand.Tpa-request-expired",
                            "&cLa demande de téléportation a expiré."
                    ));

                    target.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                            "TpaCommand.Tpa-request-expired",
                            "&cLa demande de téléportation a expiré."
                    ).replace("%player%", sender.getName()));

                    timeoutTasks.remove(senderId);
                }
            }
        };

        timeoutTask.runTaskLaterAsynchronously(HomePlugin.getPlugin(HomePlugin.class), 20L * TIMEOUT_SECONDS);
        timeoutTasks.put(senderId, timeoutTask);
    }

    public static void cancelRequest(UUID senderId) {
        CacheManager cacheManager = CacheManager.getInstance();
        cacheManager.removeTpaRequest(senderId);
        BukkitRunnable task = timeoutTasks.remove(senderId);
        if (task != null) {
            task.cancel();
        }
    }

    public static TpaRequest getRequest(UUID targetId) {
        CacheManager cacheManager = CacheManager.getInstance();

        for (UUID senderId : cacheManager.getAllTpaSenders()) {
            UUID tpaTargetId = cacheManager.getTpaRequest(senderId);
            if (tpaTargetId != null && tpaTargetId.equals(targetId)) {
                Player sender = Bukkit.getPlayer(senderId);
                Player target = Bukkit.getPlayer(targetId);
                if (sender != null && target != null) {
                    return new TpaRequest(sender, target, timeoutTasks.get(senderId));
                }
            }
        }
        return null;
    }
}
