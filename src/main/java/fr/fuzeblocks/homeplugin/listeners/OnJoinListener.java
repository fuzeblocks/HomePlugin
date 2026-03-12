package fr.fuzeblocks.homeplugin.listeners;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.spawn.SpawnManager;
import fr.fuzeblocks.homeplugin.update.UpdateChecker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * The type On join listener.
 */
public class OnJoinListener implements Listener {
    /**
     * On join.
     *
     * @param event the event
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.isOp()) {
            if (UpdateChecker.shoudAskForUpdatePlugin()) {
                player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                        "update.plugin",
                        "Please update the plugin to the latest version for a better experience."));
            }
            if (UpdateChecker.shouldAskForUpdateLangFiles()) {
                player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                        "update.lang",
                        "Please run /lang update to update the language files for a better experience."));
            }
        }
        HomeManager homeManager = HomePlugin.getHomeManager();
        if (homeManager.getHomeNumber(player) > 0) {
            CacheManager cacheManager = homeManager.getCacheManager();
            cacheManager.loadAllHomesToCache(player);
        }
        if (!player.hasPlayedBefore()) {
            SpawnManager spawnManager = HomePlugin.getSpawnManager();
            if (spawnManager.hasSpawn(player.getWorld())) {
                player.teleport(spawnManager.getSpawn(player.getWorld()));
            }
        }

    }

}
